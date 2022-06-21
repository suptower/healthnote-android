package de.othregensburg.healthnote.fragments.add

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.othregensburg.healthnote.AlarmReceiver
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentAddBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AddFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var mMedViewModel: MedicamentViewModel
    private lateinit var repeatInterval: String
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private var savedPhotoPath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]

        binding.checkMarkButton.setOnClickListener {
            insertDataToDatabase()
        }

        binding.editTextTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.editTextTime.text = SimpleDateFormat("HH:mm", Locale.GERMANY).format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val spinner : Spinner = binding.repeatDropDown
        ArrayAdapter.createFromResource(requireContext(), R.array.repeat_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this


        // Set up the listeners for take photo and video capture buttons
        binding.takeMedPhotoBtn.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()


        binding.addMedPhotoBtn.setOnClickListener {
            // Request camera permissions
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        binding.addMedPhotoBtn.visibility = View.VISIBLE
        binding.takeMedPhotoBtn.visibility = View.INVISIBLE
        binding.viewFinder.visibility = View.VISIBLE
        binding.medPhoto.visibility = View.INVISIBLE

        setHasOptionsMenu(true)

        return view
    }

    private fun insertDataToDatabase() {
        val name = binding.addMedicineName.text.toString()
        val form = binding.addMedicineForm.text.toString()
        val dose = binding.addMedicineDose.text.toString()
        val time = binding.editTextTime.text.toString()
        val alertBoolean = binding.fireAlertSwitch.isChecked
        if (inputCheck(name, form, dose, time)) {
            val med = Medicament(0, name, time, form, dose, alertBoolean, repeatInterval, savedPhotoPath.toString())
            mMedViewModel.addMed(med)
            if (med.alert) {
                // Schedule alert
                val alarmManager : AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
                alarmIntent.putExtra("MED_ID", med.id)
                alarmIntent.putExtra("MED_NAME", med.name)
                val pendingIntent = PendingIntent.getBroadcast(requireContext(), med.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
                val cal = Calendar.getInstance()
                val hour = med.time.substring(0,2).toInt()
                val min = med.time.substring(3,5).toInt()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, min)
                cal.set(Calendar.SECOND, 0)
                val repeatArr = resources.getStringArray(R.array.repeat_array)
                if (med.repeatSetting == repeatArr[0]) {
                    // Daily
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 86400000, pendingIntent)
                }
                else {
                    // Weekly
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 604800000, pendingIntent)
                }
            }

            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(name: String, form: String, dose: String, time: String) : Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(form) || TextUtils.isEmpty(dose) || TextUtils.isEmpty(time) || TextUtils.equals(time, "SET TIME"))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        return true
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 != null) {
            repeatInterval = p0.getItemAtPosition(p2).toString()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(requireContext(), "Please fill out the repeat dropdown.", Toast.LENGTH_SHORT).show()
    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val resolver = requireContext().contentResolver
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                resolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    savedPhotoPath = output.savedUri
                    binding.viewFinder.visibility = View.INVISIBLE
                    binding.medPhoto.visibility = View.VISIBLE
                    binding.medPhoto.setImageURI(savedPhotoPath)
                    binding.takeMedPhotoBtn.visibility = View.INVISIBLE
                    Log.d(TAG, msg)
                }
            }
        )
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        binding.addMedPhotoBtn.visibility = View.INVISIBLE
        binding.takeMedPhotoBtn.visibility = View.VISIBLE


        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }

}