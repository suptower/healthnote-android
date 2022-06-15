package de.othregensburg.healthnote.fragments.settings

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.othregensburg.healthnote.AlarmReceiver
import de.othregensburg.healthnote.MainActivity
import de.othregensburg.healthnote.databinding.FragmentSettingsBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.model.Settings
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import de.othregensburg.healthnote.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var svmodel: SettingsViewModel
    private lateinit var mMedViewModel: MedicamentViewModel
    private lateinit var settingsData: List<Settings>
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]
        svmodel = ViewModelProvider(this)[SettingsViewModel::class.java]

        svmodel.readAllData.observe(viewLifecycleOwner) { settings ->
            settingsData = settings
            binding.enablePIN.isChecked = settingsData[0].usePIN
            binding.enableBiometrics.isChecked = settingsData[0].useBiometrics
            binding.changePIN.isClickable = settingsData[0].usePIN
            if (binding.enablePIN.isChecked) {
                binding.changePIN.setTextColor(Color.BLACK)
            } else {
                binding.changePIN.setTextColor(Color.LTGRAY)
            }
        }

        binding.enablePIN.setOnClickListener {
            binding.changePIN.isClickable = settingsData[0].usePIN
            if (binding.enablePIN.isChecked) {
                binding.changePIN.setTextColor(Color.BLACK)
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Enter a PIN")
                val editText = EditText(requireContext())
                editText.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                editText.hint = "Enter your desired PIN"
                val layout = LinearLayout(requireContext())
                layout.gravity = Gravity.CENTER_HORIZONTAL
                layout.setPadding(16, 16, 16, 16)
                layout.addView(editText)
                builder.setView(layout)
                builder.setPositiveButton("OK") { _, _ ->
                    val text = editText.text.toString()
                    Toast.makeText(requireContext(), "Successfully set PIN", Toast.LENGTH_SHORT).show()
                    val s2 = Settings(settingsData[0].id, binding.enablePIN.isChecked, text, settingsData[0].useBiometrics)
                    svmodel.updateSetting(s2)
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                    val s2 = Settings(settingsData[0].id, false, "0", settingsData[0].useBiometrics)
                    svmodel.updateSetting(s2)
                    binding.enablePIN.isChecked = false
                    binding.changePIN.setTextColor(Color.LTGRAY)
                    binding.changePIN.isClickable = false
                }
                builder.show()
            } else {
                binding.changePIN.setTextColor(Color.LTGRAY)
                val s2 = Settings(settingsData[0].id, binding.enablePIN.isChecked, settingsData[0].code, settingsData[0].useBiometrics)
                svmodel.updateSetting(s2)
            }
        }

        binding.enableBiometrics.setOnClickListener {
            val s2 = Settings(settingsData[0].id, settingsData[0].usePIN, settingsData[0].code, binding.enableBiometrics.isChecked)
            svmodel.updateSetting(s2)
        }

        binding.changePIN.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Enter a PIN")
            val editText = EditText(requireContext())
            editText.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
            editText.hint = "Enter your desired PIN"
            val layout = LinearLayout(requireContext())
            layout.gravity = Gravity.CENTER_HORIZONTAL
            layout.setPadding(16, 16, 16, 16)
            layout.addView(editText)
            builder.setView(layout)
            builder.setPositiveButton("OK") { _, _ ->
                val text = editText.text.toString()
                Toast.makeText(requireContext(), "Successfully set PIN", Toast.LENGTH_SHORT).show()
                val s2 = Settings(settingsData[0].id, settingsData[0].usePIN, text, binding.enableBiometrics.isChecked)
                svmodel.updateSetting(s2)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        binding.deleteMedicaments.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") {_, _ ->
                mMedViewModel.deleteAllMeds()
                mMedViewModel.readAllData.observe(viewLifecycleOwner) { meds ->
                    cancelAlerts(meds)
                }
                Toast.makeText(requireContext(), "Successfully removed everything", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") {_, _ -> }
            builder.setTitle("Delete everything?")
            builder.setMessage("Are you sure you want to delete everything?")
            builder.create().show()
        }

        return view
    }

    private fun cancelAlerts(meds: List<Medicament>?) {
        if (meds != null) {
            for (med in meds) {
                if (med.alert) {
                    val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
                    alarmIntent.putExtra("MED_ID", med.id)
                    alarmIntent.putExtra("MED_NAME", med.name)
                    val pendingIntent = PendingIntent.getBroadcast(requireContext(), med.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
                    val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }

}