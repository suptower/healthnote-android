package de.othregensburg.healthnote.fragments.update

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentUpdateBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mMedicamentViewModel: MedicamentViewModel
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

        binding.checkMarkButton.setOnClickListener {
            updateItem()
        }

        binding.editTextTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.editTextTime.text = SimpleDateFormat("HH:mm", Locale.GERMANY).format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
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
            val med = Medicament(0, name, time, form, dose, alertBoolean)
            mMedicamentViewModel.updateMed(med)
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
        if (item.itemId == R.id.menu_delete) {
            deleteMed()
        }
        else if (item.itemId == android.R.id.home) {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteMed() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            mMedicamentViewModel.deleteMed(args.currentMed)
            Toast.makeText(requireContext(), "Successfully removed: ${args.currentMed.name}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_, _ -> }
        builder.setTitle("Delete ${args.currentMed.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentMed.name}?")
        builder.create().show()
    }

}