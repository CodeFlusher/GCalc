package me.codeflusher.gcalc.config

data class ParamRange(
    val max: Int,
    val isInfinite: Boolean,
) {
    companion object {
        @JvmStatic
        fun infiniteRange(): ParamRange {
            return ParamRange(0, true)
        }
    }
}
