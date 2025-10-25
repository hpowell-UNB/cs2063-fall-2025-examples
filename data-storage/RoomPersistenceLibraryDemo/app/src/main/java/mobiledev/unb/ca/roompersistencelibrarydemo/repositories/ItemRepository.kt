package mobiledev.unb.ca.roompersistencelibrarydemo.repositories

import androidx.lifecycle.LiveData
import mobiledev.unb.ca.roompersistencelibrarydemo.dao.ItemDao
import mobiledev.unb.ca.roompersistencelibrarydemo.entities.Item

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: LiveData<List<Item>> = itemDao.listAllRecords()

    fun insertRecord(name: String?, num: Int) {
        val newItem = Item()
        newItem.name = name
        newItem.num = num
        insert(newItem)
    }

    private fun insert(item: Item) {
        itemDao.insert(item)
    }

    fun deleteRecord(item: Item) {
        itemDao.deleteItem(item)
    }
}