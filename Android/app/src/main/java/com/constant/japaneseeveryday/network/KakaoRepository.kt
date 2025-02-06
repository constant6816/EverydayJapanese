package com.constant.japaneseeveryday.network

import android.content.Context
import com.constant.japaneseeveryday.network.model.AddressModel
import com.constant.japaneseeveryday.util.HHStyle
import com.constant.japaneseeveryday.util.nonNull
import io.reactivex.rxjava3.core.Observable

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class KakaoRepository {
    // Public Inner Class, Struct, Enum, Interface
    class Style {
        companion object {
            val none = 0
            val loadingSpinner = 1 shl 0
            val showErrorDialog = 1 shl 1
        }
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    // Public Variable

    // ----------------------------------------------------
    // Private Variable
    private var style: HHStyle

    // ----------------------------------------------------
    // Override Method or Basic Method
    constructor(style: HHStyle) {
        this.style = style
    }

    // ----------------------------------------------------
    // Public Method
    fun getReverseGeoCoding(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): Observable<AddressModel> {
        return KakaoAPI(context, style).getReverseGeoCoding(latitude, longitude)
            .map { addressResponseEntity ->
                nonNull(addressResponseEntity.documents).forEach { addressEntity ->
                    if (nonNull(addressEntity.region_type).equals("H")) {
                        return@map AddressModel(addressEntity)
                    }
                }
                return@map AddressModel()
            }
    }

    // Private Method
}
