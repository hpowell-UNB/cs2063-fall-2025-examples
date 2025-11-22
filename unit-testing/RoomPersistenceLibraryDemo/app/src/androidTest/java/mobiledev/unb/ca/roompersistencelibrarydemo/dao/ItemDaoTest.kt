package mobiledev.unb.ca.roompersistencelibrarydemo.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import mobiledev.unb.ca.roompersistencelibrarydemo.entities.Item
import mobiledev.unb.ca.roompersistencelibrarydemo.extensions.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


class ItemDaoTest: DatabaseTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var itemDao: ItemDao

    @Before
    fun setup() {
        itemDao = appDatabase.itemDao()
    }

    @Test
    fun testInsertAndGetItem() = runBlocking {
        val item = getTestItem()
        val id = itemDao.insert(item)

        val actual = itemDao.getItemById(id)
        Assert.assertNotNull(actual)
        Assert.assertEquals(item.name, actual?.name)
        Assert.assertEquals(item.num, actual?.num)
    }

    @Test
    fun testDeleteItem() {
        val item = getTestItem()
        val id = itemDao.insert(item)

        val dbItem = itemDao.getItemById(id)
        Assert.assertNotNull(dbItem)

        itemDao.deleteItem(dbItem!!)

        val actual = itemDao.getItemById(id)
        Assert.assertNull(actual)
    }

    @Test
    fun testGetItemAsLiveDataTest() = runBlocking {
        val itemName = getRandomString(10)
        val item = Item(name = itemName, num = Random.Default.nextInt(5))
        itemDao.insert(item)

        val allItemsLiveData = itemDao.listAllRecords().getOrAwaitValue()
        val itemFromDb = allItemsLiveData?.first()
        Assert.assertEquals(item.name, itemFromDb?.name)
    }

    fun getTestItem(): Item {
        return Item(name = getRandomString(10), num = Random.nextInt(5))
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random(Random.Default) } // Use Random.Default for a default random number generator
            .joinToString("")
    }
}