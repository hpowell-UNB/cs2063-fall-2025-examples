package mobiledev.unb.ca.sqlitedemo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var sqLiteManager: SQLiteManager
    private lateinit var listView: ListView

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

        listView = findViewById(R.id.listView)
        listView.setOnItemLongClickListener { _: AdapterView<*>?, _: View?, _: Int, id: Long ->
            deleteItem(
                id.toInt())
            true
        }


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            handleFabButtonPressed()
        }

        sqLiteManager = SQLiteManager(applicationContext)
        updateListView()
    }

    override fun onDestroy() {
        super.onDestroy()
        sqLiteManager.close()
    }

    private fun handleFabButtonPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        builder.setView(dialogView)
            .setPositiveButton(getString(R.string.dialog_button_add)) { _: DialogInterface?, _: Int ->
                val itemEditText: EditText = dialogView.findViewById(R.id.itemEditText)
                val numEditText: EditText = dialogView.findViewById(R.id.numberEditText)
                val item = itemEditText.text.toString()
                val num = numEditText.text.toString()
                addItem(item, num)
            }
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { _: DialogInterface?, _: Int -> }

        val alertDialog = builder.create()
        alertDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        alertDialog.show()
    }

    private fun updateListView() {
        // Cursor query attributes
        val queryFrom = arrayOf(DatabaseHelper.ITEM, DatabaseHelper.NUM)
        val queryTo = intArrayOf(R.id.itemText, R.id.numberText)

        lifecycleScope.launch {
            // Perform background call to retrieve the records
            val cursor = sqLiteManager.listAllRecords()

            // Update the UI with the results
            val adapter = SimpleCursorAdapter(
                applicationContext,
                R.layout.list_layout,
                cursor,
                queryFrom,
                queryTo,
                0
            )
            adapter.notifyDataSetChanged()
            listView.adapter = adapter
        }
    }

    private fun addItem(item: String, num: String) {
        lifecycleScope.launch {
            // Perform background call to save the record
            sqLiteManager.insertRecord(item, num)
            updateListView()
        }
    }

    private fun deleteItem(id: Int) {
        lifecycleScope.launch {
            // Perform background call to remove the record
            sqLiteManager.deleteRecord(id)
            updateListView()
        }
    }
}