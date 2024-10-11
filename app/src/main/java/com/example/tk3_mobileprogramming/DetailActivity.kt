package com.example.tk3_mobileprogramming

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class DetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var surveyId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dbHelper = DatabaseHelper(this)
        surveyId = intent.getLongExtra("SURVEY_ID", 0)

        val nameInput: TextInputEditText = findViewById(R.id.nameInput)
        val ageInput: TextInputEditText = findViewById(R.id.ageInput)
        val addressInput: TextInputEditText = findViewById(R.id.addressInput)
        val symptomsInput: TextInputEditText = findViewById(R.id.symptomsInput)

        val survey = dbHelper.getSurvey(surveyId)
        nameInput.setText(survey.name)
        ageInput.setText(survey.age.toString())
        addressInput.setText(survey.address)
        symptomsInput.setText(survey.symptoms)

        val updateButton: MaterialButton = findViewById(R.id.updateButton)
        updateButton.setOnClickListener {
            val updatedSurvey = Survey(
                surveyId,
                nameInput.text.toString(),
                ageInput.text.toString().toIntOrNull() ?: 0,
                addressInput.text.toString(),
                symptomsInput.text.toString()
            )
            dbHelper.updateSurvey(updatedSurvey)
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        }

        val deleteButton: MaterialButton = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_survey))
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    dbHelper.deleteSurvey(surveyId)
                    Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}