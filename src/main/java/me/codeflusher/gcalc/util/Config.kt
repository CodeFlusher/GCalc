package me.codeflusher.gcalc.util

import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.Serializable

data class Config(
    val vSync: Boolean,

) : Serializable