package mobiledev.unb.ca.roompersistencelibrarydemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mobiledev.unb.ca.roompersistencelibrarydemo.dao.ItemDao
import mobiledev.unb.ca.roompersistencelibrarydemo.entities.Item

/**
 * Database layer in top of the SQLite database
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    private class ItemDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        // Method to prepopulate the database on first app launch
        // (onOpen deletes all content every time and repopulates)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.itemDao()
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ItemRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                ).addCallback(ItemDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}