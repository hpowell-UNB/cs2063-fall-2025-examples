package mobiledev.unb.ca.roompersistencelibraryexecutorsdemo

import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.entities.Item
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.ui.ItemViewModel
import mobiledev.unb.ca.roompersistencelibraryexecutorsdemo.ui.ItemsAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerView = findViewById(R.id.recyclerView)

        // Add the listener events
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            handleFabButtonPressed()
        }

        itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        itemViewModel.allItems.observe(this) { items ->
            items?.let {
                recyclerView.adapter = ItemsAdapter(items) { item ->
                    handleDeleteItemButtonPressed(item)
                }
            }
        }
    }

    private fun handleFabButtonPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.add_item_dialog_layout, null)

        builder.setView(dialogView)
            .setPositiveButton(getString(R.string.dialog_button_add)) { dialog, id ->
                val itemEditText: EditText = dialogView.findViewById(R.id.itemEditText)
                val numEditText: EditText = dialogView.findViewById(R.id.numberEditText)
                val item = itemEditText.text.toString()
                val num = numEditText.text.toString()
                addItem(item, num)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, id ->
                dialog.cancel()
            }

        val alertDialog = builder.create()
        alertDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        alertDialog.show()
    }

    fun handleDeleteItemButtonPressed(item: Item) {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle(getString(R.string.confirm_delete_title))
        builder.setMessage(getString(R.string.confirm_delete_message))

        builder.setPositiveButton(getString(R.string.dialog_button_delete)) { dialog, id ->
            deleteItem(item)
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, id ->
            dialog.cancel()
        }

        // Create and show the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun addItem(item: String, num: String) {
        itemViewModel.insert(item, num.toInt())
    }

    private fun deleteItem(item: Item) {
        itemViewModel.delete(item)
    }
}