package de.othregensburg.healthnote.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.othregensburg.healthnote.ListAdapter
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.viewmodel.MedicamentViewModel
import de.othregensburg.healthnote.databinding.FragmentListBinding

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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]
        mMedViewModel.readAllData.observe(viewLifecycleOwner) { meds ->
            adapter.setData(meds)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return view
    }
}