package de.othregensburg.healthnote.fragments.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.othregensburg.healthnote.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

        private var _binding: FragmentCalendarBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentCalendarBinding.inflate(inflater, container, false)
            val root: View = binding.root

            return root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }