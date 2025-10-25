package mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.dao.ItemDao
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.database.AppDatabase
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.database.AppDatabase.Companion.getDatabase
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.entities.Item
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Business logic layer handling calls to the database
 */
class ItemRepository(application: Application) {
    private val itemDao: ItemDao? = getDatabase(application).itemDao()

    // LiveData is an observable data holder class
    val allItems: LiveData<List<Item>> = itemDao!!.listAllRecords()

    fun insertRecord(name: String?, num: Int) {
        val newItem = Item()
        newItem.name = name
        newItem.num = num
        insert(newItem)
    }

    private fun insert(item: Item) {
        // Using a Runnable thread object as there are no return values
        AppDatabase.databaseWriterExecutor.execute { itemDao?.insert(item) }
    }

    fun deleteRecord(item: Item): Int {
        // Using a Callable thread object as there are return values
        val dataReadFuture: Future<Int> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                itemDao?.deleteItem(item)
            })
        return try {
            while (!dataReadFuture.isDone) {
                // Simulating another task
                TimeUnit.SECONDS.sleep(1)
            }
            dataReadFuture.get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
            0
        } catch (e: InterruptedException) {
            e.printStackTrace()
            0
        }
    }
}
