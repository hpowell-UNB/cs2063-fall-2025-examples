package mobiledev.unb.ca.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SQLiteManager(context: Context?) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    suspend fun listAllRecords(): Cursor? {
        return withContext(Dispatchers.IO) {
            val cursor = openReadOnlyDatabase().query(
                DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COLUMNS,
                null,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            cursor
        }
    }

    suspend fun insertRecord(item: String?, num: String?) {
        withContext(Dispatchers.IO) {
            val contentValue = ContentValues()
            contentValue.put(DatabaseHelper.ITEM, item)
            contentValue.put(DatabaseHelper.NUM, num)
            openWriteDatabase().insert(DatabaseHelper.TABLE_NAME, null, contentValue)
        }
    }

    suspend fun deleteRecord(id: Int) {
        withContext(Dispatchers.IO) {
            openWriteDatabase().delete(
                DatabaseHelper.TABLE_NAME,
                DatabaseHelper.ITEM_ID + "=?", arrayOf(id.toString())
            )
        }
    }

    fun close() {
        dbHelper.close()
    }

    private fun openReadOnlyDatabase(): SQLiteDatabase {
        return dbHelper.readableDatabase
    }

    private fun openWriteDatabase(): SQLiteDatabase {
        return dbHelper.writableDatabase
    }
}