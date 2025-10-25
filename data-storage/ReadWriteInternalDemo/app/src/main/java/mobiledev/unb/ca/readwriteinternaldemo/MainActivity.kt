package mobiledev.unb.ca.readwriteinternaldemo

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    private lateinit var fileUtils: FileUtils
    private lateinit var fileNameEditText: EditText
    private lateinit var fileDataEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fileUtils = FileUtils(applicationContext)
        fileNameEditText = findViewById(R.id.editFile)
        fileDataEditText = findViewById(R.id.editData)

        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            handleSaveButtonPressed(fileUtils)
        }

        val btnView: Button = findViewById(R.id.btnView)
        btnView.setOnClickListener {
            handleViewButtonPress(fileUtils)
        }
    }

    private fun handleSaveButtonPressed(fileUtils: FileUtils) {
        val fileName = fileNameEditText.text.toString()
        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(applicationContext,
                getString(R.string.err_file_name),
                Toast.LENGTH_SHORT).show()
            return@handleSaveButtonPressed
        }

        val data = fileDataEditText.text.toString()
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(applicationContext,
                getString(R.string.err_file_data),
                Toast.LENGTH_SHORT).show()
            return@handleSaveButtonPressed
        }

        lifecycleScope.launch {
            // Write the results to the file
            fileUtils.writeDataToFile(fileName, data)
        }

        // Notification and clean up
        Toast.makeText(applicationContext, getString(R.string.lbl_data_saved), Toast.LENGTH_LONG).show()
        fileNameEditText.text.clear()
        fileDataEditText.text.clear()

    }
    private fun handleViewButtonPress(fileUtils: FileUtils) {
        val fileName = fileNameEditText.text.toString()
        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(applicationContext,
                getString(R.string.err_file_name),
                Toast.LENGTH_SHORT).show()
            return@handleViewButtonPress
        }

        lifecycleScope.launch {
            // Read the file details
            val data = fileUtils.readDataFromFile(fileName)

            //Displaying data on EditText
            fileDataEditText.setText(data)
        }
    }
}