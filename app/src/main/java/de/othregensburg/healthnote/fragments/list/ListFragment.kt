package de.othregensburg.healthnote.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.othregensburg.healthnote.ListAdapter
import de.othregensburg.healthnote.MainActivity
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.databinding.FragmentListBinding
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel

class ListFragment : Fragment() {

    private lateinit var mMedViewModel: MedicamentViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root

        val adapter = ListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)
            ?.let { divider.setDrawable(it) }
        recyclerView.addItemDecoration(divider)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]
        // Cancel all alerts and then reschedule them so there are no double or triple executions etc.
        mMedViewModel.readAllData.observe(viewLifecycleOwner) { meds ->
            adapter.setData(meds)
            val mainAct = activity as MainActivity
            mainAct.cancelAlerts()
            mainAct.scheduleAlerts(meds)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }




        setHasOptionsMenu(true)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllMeds()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllMeds() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            mMedViewModel.deleteAllMeds()
            Toast.makeText(requireContext(), "Successfully removed everything", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {_, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }


}