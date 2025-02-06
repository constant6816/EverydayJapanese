package com.constant.japaneseeveryday.singleton

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class DebugVariable {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: DebugVariable

        fun getInstance(): DebugVariable {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = DebugVariable()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    // Public Variable
    public var isLogEnable: Boolean = false
    public var isShowDebugText: Boolean = false
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    // Private Method
}
