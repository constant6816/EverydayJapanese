package com.constant.japaneseeveryday.singleton

import android.location.Location

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class LocationManager {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: LocationManager

        fun getInstance(): LocationManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = LocationManager()
                }
                return instance
            }
        }
    }
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method

    // withinDistance 단위는 미터
    fun isWithin(
        location1: Location,
        location2: Location,
        withinDistance: Double,
    ): Boolean {
        val distance = location1.distanceTo(location2)
        return distance <= withinDistance
    }
    // Private Method
}
