package com.constant.japaneseeveryday.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.constant.japaneseeveryday.db.AttendanceDB
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class AttendanceProvider : ContentProvider() {
    // Public Inner Class, Struct, Enum, Interface
    internal inner class QDebugProviderOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
        private val mContext: Context

        init {
            mContext = context
            HHLog.w(TAG, "QDebugProviderOpenHelper()")
        }

        override fun onCreate(db: SQLiteDatabase) {
            HHLog.w(TAG, "onCreate DB Version = " + db.version)
            val c1: Cursor =
                db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='" + AttendanceDB.Global.TABLE_NAME.toString() + "'",
                    null,
                )
            try {
                if (c1.getCount() === 0) {
                    db.execSQL(
                        "CREATE TABLE " + AttendanceDB.Global.TABLE_NAME.toString() + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, member_id INTEGER, name TEXT, value TEXT);",
                    )
                    db.execSQL(
                        "INSERT INTO " + AttendanceDB.Global.TABLE_NAME.toString() + " VALUES(null, 1, '" + AttendanceDB.Global.COLUMN_NAME_NICKNAME.toString() + "','" + "kihoon.kim" + "');",
                    )
                }
            } finally {
                c1.close()
            }
        }

        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int,
        ) {
            HHLog.w(
                TAG,
                "Upgrading database - current DB Version = " + db.version + " , oldVersion = " + oldVersion + " , newVersion = " + newVersion,
            )
            if (newVersion < 100) {
                db.execSQL("DROP TABLE IF EXISTS " + AttendanceDB.Global.TABLE_NAME.toString() + ";")
                onCreate(db)
            }
        }

        override fun onDowngrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int,
        ) {
            if (newVersion < 100) {
                db.execSQL("DROP TABLE IF EXISTS " + AttendanceDB.Global.TABLE_NAME.toString() + ";")
                onCreate(db)
            }
        }
    }

    // companion object
    // Public Constant
    // Private Constant
    private val DATABASE_NAME = "attendance.db"
    private val TAG = nonNull(this::class.simpleName)
    private val VERSION = 1

    // Public Variable
    // Private Variable
    private var mDB: SQLiteDatabase? = null
    private var mOpenHelper: QDebugProviderOpenHelper? = null
    // Override Method or Basic Method

    override fun onCreate(): Boolean {
        mOpenHelper = QDebugProviderOpenHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sort: String?,
    ): Cursor {
        val strTable: String = uri.getPathSegments().get(0)
        mDB = mOpenHelper!!.readableDatabase
        val c: Cursor = mDB!!.query(strTable, projection, selection, selectionArgs, null, null, null)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun delete(
        uri: Uri,
        arg1: String?,
        arg2: Array<String?>?,
    ): Int {
        val strTable: String = uri.getPathSegments().get(0)
        mDB = mOpenHelper!!.writableDatabase
        val rowID = mDB!!.delete(strTable, arg1, arg2)

        // String num = uri.getPathSegments().get(2);
        // Uri returi=ContentUris.withAppendedId(uri, Integer.parseInt(num));
        context!!.contentResolver.notifyChange(uri, null)
        return rowID
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        val rowID: Long
        val strTable: String = uri.getPathSegments().get(0)
        mDB = mOpenHelper!!.writableDatabase
        rowID = mDB!!.insert(strTable, null, values)
        if (rowID <= 0) return null

        // Uri returi=ContentUris.withAppendedId(uri, rowID);
        context!!.contentResolver.notifyChange(uri, null)
        return uri
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String?>?,
    ): Int {
        val rowID: Int
        val strTable: String = uri.getPathSegments().get(0)
        mDB = mOpenHelper!!.writableDatabase
        rowID = mDB!!.update(strTable, values, selection, selectionArgs)

        // String num = uri.getPathSegments().get(2);
        // Uri returi=ContentUris.withAppendedId(uri, Integer.parseInt(num));
        context!!.contentResolver.notifyChange(uri, null)
        return rowID
    }
    // Public Method
    // Private Method
}
