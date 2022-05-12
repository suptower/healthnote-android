package de.othregensburg.healthnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val randomValues = ArrayList<medicament>()

        for (i in 0..5) {
            randomValues.add(medicament(Random.nextInt(10).toString()))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ListAdapter(randomValues)

        val addButton = findViewById<Button>(R.id.add_button)

        addButton.setOnClickListener {
            val intent = Intent(this, AddMedicamentActivity::class.java)
            startActivity(intent)
        }
    }


}