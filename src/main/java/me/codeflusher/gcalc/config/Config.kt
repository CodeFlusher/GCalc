package me.codeflusher.gcalc.config

import me.codeflusher.gcalc.config.ParamRange
import java.io.Serializable

data class Config(
    val vSync: Boolean,
    val latestPrompt: String,
    val aSliderState: Int,
    val rangeX: ParamRange,
    val rangeY: ParamRange,
    val rangeZ: ParamRange,
    val resolutionX: Int,
    val resolutionY: Int,
    val debug: Boolean
    ) : Serializable
