package com.example.tk3_mobileprogramming

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SurveyAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var surveyResultsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        surveyResultsText = findViewById(R.id.surveyResultsText)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    private fun loadData() {
        val data = dbHelper.getAllSurveys()
        adapter = SurveyAdapter(data) { survey ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("SURVEY_ID", survey.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Show or hide the "Survey results" text based on data
        if (data.isNotEmpty()) {
            surveyResultsText.visibility = View.VISIBLE
        } else {
            surveyResultsText.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}