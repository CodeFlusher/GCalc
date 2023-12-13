package me.codeflusher.gcalc.config

import java.io.Serializable

/**
 * Configuration class for GCalc
 * @author CodeFlusher
 * @version 1.0
 * **/
data class Config(
    val antialiasingSamples: Int,
    val vSync: Boolean,
    val latestPrompt: String,
    val aSliderState: Int,
    val rangeA: ParamRange,
    val rangeX: ParamRange,
    val rangeY: ParamRange,
    val resolutionX: Int,
    val resolutionY: Int,
    val staticMeshResolution: Int,
    val dynamicMeshResolution: Int,
    val debug: Boolean,
) : Serializable
