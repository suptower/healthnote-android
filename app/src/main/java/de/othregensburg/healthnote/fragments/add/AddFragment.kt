package de.othregensburg.healthnote.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.data.Medicament
import de.othregensburg.healthnote.data.MedicamentViewModel
import de.othregensburg.healthnote.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private lateinit var mMedViewModel: MedicamentViewModel
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

        return view
    }

    private fun insertDataToDatabase() {
        val name = binding.addMedicineName.text.toString()
        if (inputCheck(name)) {
            val med = Medicament(0, name)
            mMedViewModel.addMed(med)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(name: String) : Boolean {
        return !(TextUtils.isEmpty(name))
    }
}