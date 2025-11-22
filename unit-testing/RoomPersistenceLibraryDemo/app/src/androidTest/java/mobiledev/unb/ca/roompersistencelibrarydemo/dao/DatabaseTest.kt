package mobiledev.unb.ca.roompersistencelibrarydemo.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import mobiledev.unb.ca.roompersistencelibrarydemo.db.AppDatabase
import org.junit.After
import org.junit.Before

abstract class DatabaseTest {
    protected lateinit var appDatabase: AppDatabase

    @Before
    fun createDb() {
        // Create an in-memory database
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java)
            .allowMainThreadQueries() // Allow queries on the main thread for testing
            .build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }
}