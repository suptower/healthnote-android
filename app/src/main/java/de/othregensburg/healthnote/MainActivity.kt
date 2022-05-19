package de.othregensburg.healthnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.othregensburg.healthnote.data.MedicamentViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mMedViewModel: MedicamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.fragment_main_containerview))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]
        mMedViewModel.readAllData.observe(this) { meds ->
            adapter.setData(meds)
        }

        val addButton = findViewById<Button>(R.id.add_button)

        addButton.setOnClickListener {
            val intent = Intent(this, AddMedicamentActivity::class.java)
            startActivity(intent)
        }
    }
}