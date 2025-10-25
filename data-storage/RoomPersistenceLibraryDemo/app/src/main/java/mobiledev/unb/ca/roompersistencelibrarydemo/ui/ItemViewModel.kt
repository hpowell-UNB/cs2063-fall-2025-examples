package mobiledev.unb.ca.roompersistencelibrarydemo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mobiledev.unb.ca.roompersistencelibrarydemo.db.ItemRoomDatabase
import mobiledev.unb.ca.roompersistencelibrarydemo.entities.Item
import mobiledev.unb.ca.roompersistencelibrarydemo.repositories.ItemRepository

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val itemRepository: ItemRepository
    val allItems: LiveData<List<Item>>

    init {
        val itemDao = ItemRoomDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = ItemRepository(itemDao)
        allItems = itemRepository.allItems
    }

   fun insert(name: String?, num: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.insertRecord(name, num)
        }
    }

    fun delete(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteRecord(item)
        }
    }
}