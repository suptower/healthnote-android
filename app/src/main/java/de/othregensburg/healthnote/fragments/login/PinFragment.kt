package de.othregensburg.healthnote.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentListBinding
import de.othregensburg.healthnote.databinding.FragmentPinBinding
import de.othregensburg.healthnote.databinding.FragmentUpdateBinding
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import de.othregensburg.healthnote.viewmodel.SettingsViewModel

class PinFragment : Fragment() {
    private lateinit var mMedViewModel: MedicamentViewModel
    private lateinit var svmodel: SettingsViewModel
    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!
    private lateinit var pin: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        val view = binding.root
        svmodel.readAllData.observe(viewLifecycleOwner) { settings ->
            pin = settings[0].code
        }

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