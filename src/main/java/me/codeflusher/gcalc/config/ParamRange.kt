package me.codeflusher.gcalc.config


/**
 * Used to represent maximum and minimum range [-max, max]
 * **/
data class ParamRange(
    val max: Int,
    val isInfinite: Boolean,
)
