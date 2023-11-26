package me.codeflusher.gcalc.config

import java.io.Serializable

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
