package mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String? = null,
    var num: Int = 0
)