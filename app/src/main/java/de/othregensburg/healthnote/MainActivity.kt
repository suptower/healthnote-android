package de.othregensburg.healthnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exampleMedicaments = ArrayList<Medicament>()
        exampleMedicaments.add(Medicament(getString(R.string.ibuprofen)))
        exampleMedicaments.add(Medicament(getString(R.string.levothyroxine)))
        exampleMedicaments.add(Medicament(getString(R.string.pantoprazole)))
        exampleMedicaments.add(Medicament(getString(R.string.metamizole)))
        exampleMedicaments.add(Medicament(getString(R.string.ramipril)))

        if (intent.getSerializableExtra("PUT_EXTRA_MEDICAMENT") != null) {
            exampleMedicaments.add(intent.getSerializableExtra("PUT_EXTRA_MEDICAMENT") as Medicament)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ListAdapter(exampleMedicaments)

        val addButton = findViewById<Button>(R.id.add_button)

        addButton.setOnClickListener {
            val intent = Intent(this, AddMedicamentActivity::class.java)
            startActivity(intent)
        }
    }
}