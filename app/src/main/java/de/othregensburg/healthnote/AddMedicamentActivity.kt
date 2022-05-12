package de.othregensburg.healthnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class AddMedicamentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicament)

        val nameTextBox = findViewById<EditText>(R.id.addMedicineName)

        nameTextBox.setOnClickListener {
            nameTextBox.setText("")
        }
    }
}