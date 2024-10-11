package com.example.tk3_mobileprogramming

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "covid_survey.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "surveys"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_SYMPTOMS = "symptoms"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_SYMPTOMS TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addSurvey(survey: Survey): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, survey.name)
            put(COLUMN_AGE, survey.age)
            put(COLUMN_ADDRESS, survey.address)
            put(COLUMN_SYMPTOMS, survey.symptoms)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllSurveys(): List<Survey> {
        val surveys = mutableListOf<Survey>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val age = it.getInt(it.getColumnIndexOrThrow(COLUMN_AGE))
                val address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val symptoms = it.getString(it.getColumnIndexOrThrow(COLUMN_SYMPTOMS))
                surveys.add(Survey(id, name, age, address, symptoms))
            }
        }
        return surveys
    }

    fun getSurvey(id: Long): Survey {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val age = it.getInt(it.getColumnIndexOrThrow(COLUMN_AGE))
                val address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                val symptoms = it.getString(it.getColumnIndexOrThrow(COLUMN_SYMPTOMS))
                return Survey(id, name, age, address, symptoms)
            }
        }
        throw IllegalArgumentException("Survey with id $id not found")
    }

    fun updateSurvey(survey: Survey): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, survey.name)
            put(COLUMN_AGE, survey.age)
            put(COLUMN_ADDRESS, survey.address)
            put(COLUMN_SYMPTOMS, survey.symptoms)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(survey.id.toString()))
    }

    fun deleteSurvey(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}