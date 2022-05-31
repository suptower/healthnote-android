package de.othregensburg.healthnote.fragments.add

import android.app.Activity
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentAddBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var outputFileUri : Uri

    private lateinit var mMedViewModel: MedicamentViewModel
    private lateinit var repeatInterval: String
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
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

        binding.selectImage.setOnClickListener {
            openImageIntent()
        }

        val spinner : Spinner = binding.repeatDropDown
        ArrayAdapter.createFromResource(requireContext(), R.array.repeat_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this



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
            val med = Medicament(0, name, time, form, dose, alertBoolean, repeatInterval)
            mMedViewModel.addMed(med)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(name: String, form: String, dose: String, time: String) : Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(form) || TextUtils.isEmpty(dose) || TextUtils.isEmpty(time) || TextUtils.equals(time, "SET TIME"))
    }

    private fun openImageIntent() {
        val fileRoot = File(Environment.getExternalStorageDirectory(), File.separator + "HealthNote" + File.separator)
        fileRoot.mkdirs()
        val fileName = "img_" + System.currentTimeMillis() + ".jpg"
        val sdImageMainDirectory = File(fileRoot, fileName)
        outputFileUri = Uri.fromFile(sdImageMainDirectory)

        val cameraIntents = ArrayList<Intent>()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = requireContext().packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component = ComponentName(packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            cameraIntents.add(intent)
        }

        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        val chooserIntent = Intent.createChooser(galleryIntent, "Select Source")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray())

        // TODO Register Activity for Result and handle parameters/uri
        // https://stackoverflow.com/questions/4455558/allow-user-to-select-camera-or-gallery-for-image/12347567#12347567
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

        resultLauncher.launch(chooserIntent)
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
}
