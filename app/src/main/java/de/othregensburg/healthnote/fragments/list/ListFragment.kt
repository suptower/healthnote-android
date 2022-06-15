package de.othregensburg.healthnote.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import de.othregensburg.healthnote.viewmodel.SettingsViewModel

class ListFragment : Fragment() {

    private lateinit var mMedViewModel: MedicamentViewModel
    private lateinit var svmodel: SettingsViewModel
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
        }
        // Prepare settings
        svmodel = ViewModelProvider(this)[SettingsViewModel::class.java]
        svmodel.readAllData.observe(viewLifecycleOwner) { settings ->
            val mainAct = activity as MainActivity
            mainAct.prepareSettings(settings, svmodel)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }




        setHasOptionsMenu(true)

        return view
    }

}