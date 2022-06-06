package de.othregensburg.healthnote.fragments.settings

import android.app.AlertDialog
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
import de.othregensburg.healthnote.databinding.FragmentSettingsBinding
import de.othregensburg.healthnote.model.Settings
import de.othregensburg.healthnote.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var svmodel: SettingsViewModel
    private lateinit var settingsData: List<Settings>
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
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
                var text = ""
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
                    text = editText.text.toString()
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
            var text = ""
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
                text = editText.text.toString()
                Toast.makeText(requireContext(), "Successfully set PIN", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
            val s2 = Settings(settingsData[0].id, settingsData[0].usePIN, text, binding.enableBiometrics.isChecked)
            svmodel.updateSetting(s2)
        }

        return view
    }

}