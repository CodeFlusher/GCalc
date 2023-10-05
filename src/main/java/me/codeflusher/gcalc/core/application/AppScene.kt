package me.codeflusher.gcalc.core.application

import me.codeflusher.gcalc.core.application.Map
import me.codeflusher.gcalc.user.Camera

data class AppScene(
    val map : Map,
    val camera: Camera
)
