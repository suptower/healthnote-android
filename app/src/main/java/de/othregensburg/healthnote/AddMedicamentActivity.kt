package de.othregensburg.healthnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddMedicamentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicament)

        val nameTextBox = findViewById<EditText>(R.id.addMedicineName)
        val checkButton = findViewById<Button>(R.id.checkMarkButton)
        checkButton.setOnClickListener {
            val text: String = nameTextBox.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PUT_EXTRA_MEDICAMENT", Medicament(text))
            startActivity(intent)
        }
    }
}