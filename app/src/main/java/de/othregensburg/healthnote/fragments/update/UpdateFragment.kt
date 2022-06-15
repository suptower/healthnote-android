package de.othregensburg.healthnote.fragments.update

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.othregensburg.healthnote.AlarmReceiver
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentUpdateBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mMedicamentViewModel: MedicamentViewModel
    private lateinit var repeatInterval: String
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        mMedicamentViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]

        binding.addMedicineName.setText(args.currentMed.name)
        binding.addMedicineDose.setText(args.currentMed.dose)
        binding.addMedicineForm.setText(args.currentMed.form)
        binding.editTextTime.text = args.currentMed.time
        binding.fireAlertSwitch.isChecked = args.currentMed.alert

        binding.editTextTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.editTextTime.text = SimpleDateFormat("HH:mm", Locale.GERMANY).format(cal.time).toString()
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
        }

        val spinner : Spinner = binding.repeatDropDown
        ArrayAdapter.createFromResource(requireContext(), R.array.repeat_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        binding.checkMarkButton.setOnClickListener {
            updateItem()
        }

        setHasOptionsMenu(true)


        return view

    }

    private fun updateItem() {
        val name = binding.addMedicineName.text.toString()
        val form = binding.addMedicineForm.text.toString()
        val dose = binding.addMedicineDose.text.toString()
        val time = binding.editTextTime.text.toString()
        val alertBoolean = binding.fireAlertSwitch.isChecked
        if (inputCheck(name, form, dose, time)) {
            val med = Medicament(args.currentMed.id, name, time, form, dose, alertBoolean, repeatInterval)
            mMedicamentViewModel.updateMed(med)
            if (args.currentMed.alert) {
                val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
                alarmIntent.putExtra("MED_ID", args.currentMed.id)
                alarmIntent.putExtra("MED_NAME", args.currentMed.name)
                val pendingIntent = PendingIntent.getBroadcast(requireContext(), args.currentMed.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
            }
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
            Toast.makeText(requireContext(),"Updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }

    }

    private fun inputCheck(name: String, form: String, dose: String, time: String) : Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(form) || TextUtils.isEmpty(dose) || TextUtils.isEmpty(time) || TextUtils.equals(time, "SET TIME"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else if (item.itemId == R.id.menu_delete) {
            deleteMed()
        }
        return true
    }


    private fun deleteMed() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            mMedicamentViewModel.deleteMed(args.currentMed)
            val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
            alarmIntent.putExtra("MED_ID", args.currentMed.id)
            alarmIntent.putExtra("MED_NAME", args.currentMed.name)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), args.currentMed.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentMed.name}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_, _ -> }
        builder.setTitle("Delete ${args.currentMed.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentMed.name}?")
        builder.create().show()
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