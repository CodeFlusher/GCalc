package me.codeflusher.gcalc.core.application

import me.codeflusher.gcalc.user.Camera

/**
 * Holder class for RenderMap and Camera
 * **/
data class AppScene(
    val map: RenderMap,
    val camera: Camera,
)
