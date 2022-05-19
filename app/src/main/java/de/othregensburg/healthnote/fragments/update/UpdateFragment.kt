package de.othregensburg.healthnote.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentListBinding
import de.othregensburg.healthnote.databinding.FragmentUpdateBinding
import de.othregensburg.healthnote.model.Medicament
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mMedicamentViewModel: MedicamentViewModel
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        mMedicamentViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]

        binding.updateMedicineName.setText(args.currentMed.name)

        binding.updateCheckMarkButton.setOnClickListener {
            updateItem()
        }


        return view

    }

    private fun updateItem() {
        val name = binding.updateMedicineName.text.toString()

        if (inputCheck(name)) {
            val updatedMed = Medicament(args.currentMed.id, name)
            mMedicamentViewModel.updateMed(updatedMed)
            Toast.makeText(requireContext(),"Updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }

    }

    private fun inputCheck(name: String) : Boolean {
        return !(TextUtils.isEmpty(name))
    }

}