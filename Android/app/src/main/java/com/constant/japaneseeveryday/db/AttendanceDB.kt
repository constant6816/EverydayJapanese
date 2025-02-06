package com.constant.japaneseeveryday.db

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object AttendanceDB {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    private val AUTHORITY = "com.constant.attendance.AttendanceProvider"

    const val COLUMN_NAME_ANNOUNCE = "announce"

    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun getAnnounceFlag(
        context: Context,
        memberId: Int,
        id: Int,
    ): Boolean {
        return Global.getBoolean(context.contentResolver, memberId, String.format("%s_%d", COLUMN_NAME_ANNOUNCE, id))
    }

    fun setAnnounceFlag(
        context: Context,
        memberId: Int,
        id: Int,
        value: Boolean,
    ) {
        Global.putBoolean(context.contentResolver, memberId, String.format("%s_%d", COLUMN_NAME_ANNOUNCE, id), value)
    }
    // Private Method

    object Global : BaseColumns {
        const val TABLE_NAME = "global"
        val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME)
        const val COLUMN_MEMBER_ID = "member_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_VALUE = "value"
        const val COLUMN_NAME_NICKNAME = "nickname"

        private const val DATA_SELECTION = COLUMN_MEMBER_ID + "=? AND " + COLUMN_NAME + "=?"

        fun getString(
            cr: ContentResolver,
            memberId: Int,
            name: String,
        ): String {
            var c: Cursor? = null
            try {
                c =
                    cr.query(
                        CONTENT_URI,
                        arrayOf(COLUMN_VALUE),
                        DATA_SELECTION,
                        arrayOf(memberId.toString(), name),
                        null,
                    )
                if (c != null && c.moveToNext()) {
                    return c.getString(0)
                }
            } finally {
                if (c != null) {
                    c.close()
                    c = null
                }
            }
            // if there is no field return "";
            return ""
        }

        @Throws(IllegalArgumentException::class)
        fun putString(
            cr: ContentResolver,
            memberId: Int,
            name: String,
            value: String?,
        ): Boolean {
            var c: Cursor? = null
            val values = ContentValues()
            return try {
                c =
                    cr.query(
                        CONTENT_URI,
                        arrayOf(COLUMN_VALUE),
                        DATA_SELECTION,
                        arrayOf(memberId.toString(), name),
                        null,
                    )
                if (c != null && c.moveToNext()) {
                    values.put(COLUMN_VALUE, value)
                    val i = cr.update(CONTENT_URI, values, DATA_SELECTION, arrayOf(memberId.toString(), name))
                    require(i != -1)
                    true
                } else {
                    values.put(COLUMN_MEMBER_ID, memberId)
                    values.put(COLUMN_NAME, name)
                    values.put(COLUMN_VALUE, value)
                    cr.insert(CONTENT_URI, values)
                    true
                }
            } finally {
                if (c != null) {
                    c.close()
                    c = null
                }
            }
        }

        fun getInt(
            cr: ContentResolver,
            memberId: Int,
            name: String,
        ): Int {
            val v = getString(cr, memberId, name)
            return try {
                v.toInt()
            } catch (e: NumberFormatException) {
                throw RuntimeException()
            }
        }

        fun putInt(
            cr: ContentResolver,
            memberId: Int,
            name: String,
            value: Int,
        ): Boolean {
            return putString(cr, memberId, name, Integer.toString(value))
        }

        fun getBoolean(
            cr: ContentResolver,
            memberId: Int,
            name: String,
        ): Boolean {
            val v = getString(cr, memberId, name)
            return java.lang.Boolean.parseBoolean(v)
        }

        fun putBoolean(
            cr: ContentResolver,
            memberId: Int,
            name: String,
            value: Boolean,
        ): Boolean {
            HHLog.d(TAG, "putBoolean=$value")
            return putString(cr, memberId, name, java.lang.Boolean.toString(value))
        }
    }
}
