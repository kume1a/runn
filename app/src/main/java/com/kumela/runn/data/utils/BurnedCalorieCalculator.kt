package com.kumela.runn.data.utils

import androidx.annotation.FloatRange

class BurnedCalorieCalculator {

    fun calculateCaloriesBurned(
        @FloatRange(from = 0.0) weightInKgs: Float,
        @FloatRange(from = 0.0) phase: Float,
        @FloatRange(from = 0.0) timeInMinutes: Float,
    ): Float {
        val met = getMetForPhase(phase)
        val caloriesPerMinute = (met * weightInKgs * 3.5f) / 200f
        
        return caloriesPerMinute * timeInMinutes
    }

    private fun getMetForPhase(@FloatRange(from = 0.0) phase: Float): Float {
        return when {
            phase >= 0f && phase < 6.5f -> MET_6_5_KMH
            phase >= 6.5f && phase < 8f -> MET_8_KMH
            phase >= 8f && phase < 9.6f -> MET_9_6_KMH
            phase >= 9.6f && phase < 11.2f -> MET_11_2_KMH
            phase >= 11.2f && phase < 12.9f -> MET_12_9_KMH
            phase >= 12.9f && phase < 14.5f -> MET_14_5_KMH
            phase >= 14.5f && phase < 16.1f -> MET_16_1_KMH
            phase >= 16.1f && phase < 17.7f -> MET_17_7_KMH
            phase >= 17.7f && phase < 19.3f -> MET_19_3_KMH
            phase >= 19.3f && phase < 22.5f -> MET_20_9_KMH
            else -> MET_22_5_KMH
        }
    }

    /**
     *  Metabolic Equivalent
     *  1 MET = 1 cal/kg/hr
     *
     *  measurement of the energy
     *  cost of physical activity for a period of time
     */
    companion object MET {
        private const val MET_6_5_KMH = 5f       // 4 m/h
        private const val MET_8_KMH = 8.3f       // 5 m/h
        private const val MET_9_6_KMH = 9.8f     // 6 m/h
        private const val MET_11_2_KMH = 11f     // 7 m/h
        private const val MET_12_9_KMH = 11.8f   // 8 m/h
        private const val MET_14_5_KMH = 12.8f   // 9 m/h
        private const val MET_16_1_KMH = 14.5f   // 10 m/h
        private const val MET_17_7_KMH = 16f     // 11 m/h
        private const val MET_19_3_KMH = 19f     // 12 m/h
        private const val MET_20_9_KMH = 19.8f   // 13 m/h
        private const val MET_22_5_KMH = 23f     // 14 m/h
    }
}