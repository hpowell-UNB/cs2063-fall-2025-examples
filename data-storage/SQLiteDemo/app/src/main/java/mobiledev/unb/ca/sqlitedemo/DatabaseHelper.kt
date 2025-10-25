package mobiledev.unb.ca.sqlitedemo

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    companion object {
        // Database information
        private const val DATABASE_NAME = "ITEM_TEST.DB"
        private const val DATABASE_VERSION = 1

        // Table columns
        const val ITEM_ID = "_id"
        const val ITEM = "item"
        const val NUM = "number"

        @JvmField
        var COLUMNS = arrayOf(ITEM_ID, ITEM, NUM)

        // Table creation statement
        const val TABLE_NAME = "item_db"
        private const val TABLE_CREATE = ("CREATE TABLE " + TABLE_NAME + " (" + ITEM_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM + " TEXT, " + NUM + " INT);")
    }
}