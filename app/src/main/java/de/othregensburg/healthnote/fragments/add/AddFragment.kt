package de.othregensburg.healthnote.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import de.othregensburg.healthnote.R
import de.othregensburg.healthnote.data.MedicamentViewModel

class AddFragment : Fragment() {

    private lateinit var mMedViewModel: MedicamentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mMedViewModel = ViewModelProvider(this).get(MedicamentViewModel::class.java)

        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val name = na
    }
}