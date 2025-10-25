package mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.entities.Item

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
interface ItemDao {
    @Query("SELECT * from item_table ORDER BY id ASC")
    fun listAllRecords(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Item)

    @Delete
    infix fun deleteItem(item: Item): Int
}