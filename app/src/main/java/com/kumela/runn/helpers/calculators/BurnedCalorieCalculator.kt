package com.kumela.runn.helpers.calculators

import androidx.annotation.FloatRange
import javax.inject.Inject

class BurnedCalorieCalculator @Inject constructor() {

    fun calculateCaloriesBurned(
        @FloatRange(from = 0.0) weightInKgs: Float,
        @FloatRange(from = 0.0) pace: Float,
        @FloatRange(from = 0.0) timeInMinutes: Float,
    ): Float {
        val met = getMetForPhase(pace)
        val caloriesPerMinute = (met * weightInKgs * 3.5f) / 200f

        return caloriesPerMinute * timeInMinutes
    }

    private fun getMetForPhase(@FloatRange(from = 0.0) pace: Float): Float {
        return when {
            pace >= 0f && pace < 6.5f -> MET_6_5_KMH
            pace >= 6.5f && pace < 8f -> MET_8_KMH
            pace >= 8f && pace < 9.6f -> MET_9_6_KMH
            pace >= 9.6f && pace < 11.2f -> MET_11_2_KMH
            pace >= 11.2f && pace < 12.9f -> MET_12_9_KMH
            pace >= 12.9f && pace < 14.5f -> MET_14_5_KMH
            pace >= 14.5f && pace < 16.1f -> MET_16_1_KMH
            pace >= 16.1f && pace < 17.7f -> MET_17_7_KMH
            pace >= 17.7f && pace < 19.3f -> MET_19_3_KMH
            pace >= 19.3f && pace < 22.5f -> MET_20_9_KMH
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