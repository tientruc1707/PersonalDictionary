package com.example.personaldictionary.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "personal_dictionary.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_WORDS = "words"
        const val COLUMN_ID = "id"
        const val COLUMN_WORD = "word"
        const val COLUMN_MEANING = "meaning"
        const val COLUMN_EXAMPLE = "example"
        const val COLUMN_PART_OF_SPEECH = "part_of_speech"
        const val COLUMN_TOPIC = "topic"
        const val COLUMN_AUDIO_PATH = "audio_path"
        const val COLUMN_IS_FAVORITE = "is_favorite"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_WORDS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_WORD TEXT NOT NULL, "
                + "$COLUMN_MEANING TEXT NOT NULL, "
                + "$COLUMN_EXAMPLE TEXT, "
                + "$COLUMN_PART_OF_SPEECH TEXT, "
                + "$COLUMN_TOPIC TEXT, "
                + "$COLUMN_AUDIO_PATH TEXT, "
                + "$COLUMN_IS_FAVORITE INTEGER DEFAULT 0)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORDS")
        onCreate(db)
    }
}
