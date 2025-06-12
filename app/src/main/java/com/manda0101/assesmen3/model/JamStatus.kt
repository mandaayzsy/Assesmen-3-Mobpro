package com.manda0101.assesmen3.model

data class JamStatus(
    val success: Boolean,
    val message: String,
    val data: List<Jam>
)
