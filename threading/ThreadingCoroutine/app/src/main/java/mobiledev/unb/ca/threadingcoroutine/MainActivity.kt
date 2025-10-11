package mobiledev.unb.ca.threadingcoroutine

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var loadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadButton = findViewById(R.id.loadButton)
        loadButton.setOnClickListener { onClickLoadButton() }

        val otherButton: Button = findViewById(R.id.otherButton)
        otherButton.setOnClickListener { onClickOtherButton() }
    }

    fun onClickLoadButton() {
        loadButton.isEnabled = false
        val loadIconTask: LoadIconTask = LoadIconTask(applicationContext)
            .setImageView(findViewById(R.id.imageView))
            .setProgressBar(findViewById(R.id.progressBar))
        loadIconTask.execute()
    }

    fun onClickOtherButton() {
        Toast.makeText(this@MainActivity, "I'm Working",
            Toast.LENGTH_SHORT).show()
    }
}