package com.example.personaldictionary.controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.personaldictionary.model.DatabaseHelper
import com.example.personaldictionary.model.Word

class WordController(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // UC01: Add word
    fun addWord(word: Word): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_WORD, word.word)
            put(DatabaseHelper.COLUMN_MEANING, word.meaning)
            put(DatabaseHelper.COLUMN_EXAMPLE, word.example)
            put(DatabaseHelper.COLUMN_PART_OF_SPEECH, word.partOfSpeech)
            put(DatabaseHelper.COLUMN_TOPIC, word.topic)
            put(DatabaseHelper.COLUMN_AUDIO_PATH, word.audioPath)
            put(DatabaseHelper.COLUMN_IS_FAVORITE, if (word.isFavorite) 1 else 0)
        }
        return db.insert(DatabaseHelper.TABLE_WORDS, null, values)
    }

    // UC02: Update word
    fun updateWord(word: Word): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_WORD, word.word)
            put(DatabaseHelper.COLUMN_MEANING, word.meaning)
            put(DatabaseHelper.COLUMN_EXAMPLE, word.example)
            put(DatabaseHelper.COLUMN_PART_OF_SPEECH, word.partOfSpeech)
            put(DatabaseHelper.COLUMN_TOPIC, word.topic)
            put(DatabaseHelper.COLUMN_AUDIO_PATH, word.audioPath)
            put(DatabaseHelper.COLUMN_IS_FAVORITE, if (word.isFavorite) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_WORDS,
            values,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(word.id.toString())
        )
    }

    // UC03: Delete word
    fun deleteWord(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_WORDS,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // UC04: Get all words
    fun getAllWords(): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_WORDS,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_ID} DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                wordList.add(cursorToWord(it))
            }
        }
        return wordList
    }

    // UC07: Search words
    fun searchWords(query: String): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = dbHelper.readableDatabase
        val selection = "${DatabaseHelper.COLUMN_WORD} LIKE ? OR ${DatabaseHelper.COLUMN_MEANING} LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%")
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_WORDS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "${DatabaseHelper.COLUMN_ID} DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                wordList.add(cursorToWord(it))
            }
        }
        return wordList
    }

    // UC08 & UC09: Favorite toggle and get favorites
    fun setFavorite(id: Long, isFavorite: Boolean): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_IS_FAVORITE, if (isFavorite) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_WORDS,
            values,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun getFavoriteWords(): List<Word> {
        val wordList = mutableListOf<Word>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_WORDS,
            null,
            "${DatabaseHelper.COLUMN_IS_FAVORITE} = ?",
            arrayOf("1"),
            null,
            null,
            "${DatabaseHelper.COLUMN_ID} DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                wordList.add(cursorToWord(it))
            }
        }
        return wordList
    }

    // UC11: Statistics (Total words, favorite count)
    fun getTotalWordCount(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseHelper.TABLE_WORDS}", null)
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(0)
            }
        }
        return 0
    }

    private fun cursorToWord(cursor: Cursor): Word {
        return Word(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
            word = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORD)),
            meaning = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEANING)),
            example = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXAMPLE)),
            partOfSpeech = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PART_OF_SPEECH)),
            topic = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOPIC)),
            audioPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUDIO_PATH)),
            isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_FAVORITE)) == 1
        )
    }
}
