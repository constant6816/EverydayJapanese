package com.constant.japaneseeveryday.util

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class HHStyle(var value: Int) {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun addStyle(addValue: Int) {
        value = value or addValue
    }

    fun removeStyle(removeValue: Int) {
        value = value and removeValue.inv()
    }

    fun setStyle(value: Int) {
        this.value = value
    }

    fun isInclude(value: Int): Boolean {
        if (this.value and value == 0) {
            return false
        } else {
            return true
        }
    }
    // Private Method
}
