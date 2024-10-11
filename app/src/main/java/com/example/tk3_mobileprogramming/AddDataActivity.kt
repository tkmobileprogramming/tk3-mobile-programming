package com.example.tk3_mobileprogramming

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddDataActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dbHelper = DatabaseHelper(this)

        val nameInput: TextInputEditText = findViewById(R.id.nameInput)
        val ageInput: TextInputEditText = findViewById(R.id.ageInput)
        val addressInput: TextInputEditText = findViewById(R.id.addressInput)
        val symptomsInput: TextInputEditText = findViewById(R.id.symptomsInput)

        val submitButton: MaterialButton = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            val address = addressInput.text.toString()
            val symptoms = symptomsInput.text.toString()

            if (name.isNotEmpty() && address.isNotEmpty() && symptoms.isNotEmpty()) {
                val survey = Survey(0, name, age, address, symptoms)
                dbHelper.addSurvey(survey)
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}