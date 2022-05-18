package de.othregensburg.healthnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import de.othregensburg.healthnote.data.Medicament
import de.othregensburg.healthnote.data.MedicamentViewModel

class AddMedicamentActivity : AppCompatActivity() {

    private lateinit var mMedViewModel: MedicamentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicament)

        mMedViewModel = ViewModelProvider(this)[MedicamentViewModel::class.java]

        val nameTextBox = findViewById<EditText>(R.id.addMedicineName)
        nameTextBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val intent = Intent(this, MainActivity::class.java)
                insertDataToDatabase()
                startActivity(intent)
                return@OnKeyListener true
            }
            false
        })
        val checkButton = findViewById<Button>(R.id.checkMarkButton)
        checkButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            insertDataToDatabase()
            startActivity(intent)
        }
    }

    // TODO Test adding and show on Main Activity

    private fun insertDataToDatabase() {
        val name = findViewById<EditText>(R.id.addMedicineName).text.toString()
        if (inputCheck(name)) {
            val med = Medicament(0, name)
            mMedViewModel.addMed(med)
            Toast.makeText(this, "Successfully added!",  Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Error when adding", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}