package com.example.personaldictionary.model

data class Word(
    val id: Long = -1,
    val word: String,
    val meaning: String,
    val example: String,
    val partOfSpeech: String,
    val topic: String,
    val audioPath: String? = null,
    val isFavorite: Boolean = false
)
