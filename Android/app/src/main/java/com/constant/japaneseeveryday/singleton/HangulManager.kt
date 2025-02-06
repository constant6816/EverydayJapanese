package com.constant.japaneseeveryday.singleton

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class HangulManager {
    // Public Inner Class, Struct, Enum, Interface

    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: HangulManager

        fun getInstance(): HangulManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = HangulManager()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val HANGUL_START_CODE = 0xAC00
    private val HANGUL_END_CODE = 0xD79F
    private val jungsungNumber = 21
    private val jongsungNumber = 28
    private val chosung =
        listOf(
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ",
            "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ",
            "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ",
        )

    private val jungsung =
        listOf(
            "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ",
            "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ",
            "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ",
            "ㅡ", "ㅢ", "ㅣ",
        )

    private val jongsung =
        listOf(
            "", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ",
            "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ",
            "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ",
            "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ",
        )
    // ----------------------------------------------------
    // Public Variable

    // ----------------------------------------------------
    // Private Variable
    // Override Method or Basic Method
    constructor() {
    }

    // Public Method
    // Checks if a string contains another string
    fun contains(
        source: String,
        findString: String,
    ): Boolean {
        val lowerSource = source.lowercase()
        val lowerFindString = findString.lowercase()
        val findStrings = lowerFindString.split(" ")
        for (find in findStrings) {
            if (find.length == 0) {
                return false
            }
            if (lowerSource.contains(find)) {
                return true
            }
            for (i in lowerSource.indices) {
                for (j in find.indices) {
                    val sourceIndex = i + j
                    if (sourceIndex >= lowerSource.length) break
                    val sv = lowerSource[sourceIndex].code
                    val svc = toChosung(sv)
                    val fv = find[j].code
                    if (isChosung(fv)) {
                        if (svc != fv) break
                    } else {
                        if (sv != fv) break
                    }
                    if (j == find.lastIndex) return true
                }
            }
        }
        return false
    }

    // Returns the found part of a string based on the given pattern
    fun getContainString(
        source: String,
        findString: String,
    ): String {
        val lowerSource = source.lowercase()
        val lowerFindString = findString.lowercase()
        val findStrings = lowerFindString.split(" ")
        for (find in findStrings) {
            if (lowerSource.contains(find)) {
                val startIndex = lowerSource.indexOf(find)
                val endIndex = startIndex + find.length
                return source.substring(startIndex, endIndex)
            }

            for (i in lowerSource.indices) {
                for (j in find.indices) {
                    val sourceIndex = i + j
                    if (sourceIndex >= lowerSource.length) break
                    val sv = lowerSource[sourceIndex].code
                    val svc = toChosung(sv)
                    val fv = find[j].code
                    if (isChosung(fv)) {
                        if (svc != fv) break
                    } else {
                        if (sv != fv) break
                    }
                    if (j == find.lastIndex) {
                        println("source = $source, i = $i, find.count = ${find.length}")
                        return source.substring(i, i + find.length)
                    }
                }
            }
        }
        return ""
    }

    // Returns the chosung (initial consonant) from a string
    fun getChosungString(source: String): String {
        val result = StringBuilder()
        for (i in source.indices) {
            val unicodeChar = source[i].code
            if (HANGUL_START_CODE <= unicodeChar && unicodeChar <= HANGUL_END_CODE) {
                val chosungIndex = (unicodeChar - HANGUL_START_CODE) / (jungsungNumber * jongsungNumber)
                result.append(chosung[chosungIndex])
            } else if (unicodeChar == 32 && result.isNotEmpty()) {
                result.append(" ")
            } else if (unicodeChar == 47) {
                result.append("/")
            } else {
                for (c in chosung) {
                    if (unicodeChar == c[0].code) {
                        result.append(c)
                    }
                }
            }
        }
        return result.toString()
    }

    // Private Method
    // Check if a character is a chosung (initial consonant)
    private fun isChosung(char: Int): Boolean {
        return chosung.any { it[0].code == char }
    }

    // Convert a character to chosung
    private fun toChosung(char: Int): Int {
        if (HANGUL_START_CODE <= char && char <= HANGUL_END_CODE) {
            val chosungIndex = (char - HANGUL_START_CODE) / (jungsungNumber * jongsungNumber)
            return chosung[chosungIndex][0].code
        }
        return 0
    }
}
