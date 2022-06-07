package de.othregensburg.healthnote.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.othregensburg.healthnote.MainActivity
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentPinBinding
import de.othregensburg.healthnote.viewmodel.SettingsViewModel

class PinFragment : Fragment() {
    private lateinit var svmodel: SettingsViewModel
    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private lateinit var pin: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        val view = binding.root
        svmodel = ViewModelProvider(this)[SettingsViewModel::class.java]
        svmodel.readAllData.observe(viewLifecycleOwner) { settings ->
            val mainAct = activity as MainActivity
            mainAct.prepareSettings(settings, svmodel)
            pin = settings[0].code
            if (!settings[0].usePIN) {
                findNavController().navigate(R.id.action_pinFragment_to_listFragment)
            }
        }

        val mainAct = activity as MainActivity
        mainAct.supportActionBar?.setDisplayShowHomeEnabled(false)
        mainAct.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mainAct.supportActionBar?.setHomeButtonEnabled(false)



        binding.checkPin.setOnClickListener {
            if (pin == binding.enterPassword.text.toString()) {
                findNavController().navigate(R.id.action_pinFragment_to_listFragment)
            } else {
                Toast.makeText(requireContext(), "False PIN", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}