package de.othregensburg.healthnote.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.othregensburg.healthnote.R
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
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        mMedicamentViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]

        binding.updateMedicineName.setText(args.currentMed.name)

        binding.updateCheckMarkButton.setOnClickListener {
            updateItem()
        }

        setHasOptionsMenu(true)


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteMed()
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