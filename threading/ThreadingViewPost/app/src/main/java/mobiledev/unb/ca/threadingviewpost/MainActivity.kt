package mobiledev.unb.ca.threadingviewpost

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.imageView)

        val loadButton: Button = findViewById(R.id.loadButton)
        loadButton.setOnClickListener { onClickLoadButton() }

        val otherButton: Button = findViewById(R.id.otherButton)
        otherButton.setOnClickListener { onClickOtherButton() }
    }

    fun onClickLoadButton() {
        Thread {
            try {
                Thread.sleep(DELAY_TIME.toLong())
            } catch (e: InterruptedException) {
                Log.e(TAG, e.toString())
            }

            // Using View.post() to access the thread
            imageView?.post {
                imageView?.setImageBitmap(BitmapFactory.decodeResource(resources,
                    R.drawable.painter))
            }
        }.start()
    }

    fun onClickOtherButton() {
        Toast.makeText(
            this@MainActivity, "I'm Working",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val TAG = "ThreadingViewPost"
        private const val DELAY_TIME = 5000
    }
}