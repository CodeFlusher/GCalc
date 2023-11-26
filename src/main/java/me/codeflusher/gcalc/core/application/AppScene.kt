package me.codeflusher.gcalc.core.application

import me.codeflusher.gcalc.user.Camera

data class AppScene(
    val map: RenderMap,
    val camera: Camera,
)
