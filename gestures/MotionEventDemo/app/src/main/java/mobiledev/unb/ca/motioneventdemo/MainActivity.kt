package mobiledev.unb.ca.motioneventdemo

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView = findViewById(R.id.textview)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        // Extract the action from the event
        return when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                val msg = getString(R.string.action_down)
                Log.i(TAG, msg)
                textView.text = msg
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val msg = getString(R.string.action_move)
                Log.i(TAG, msg)
                textView.text = msg
                true
            }
            MotionEvent.ACTION_UP -> {
                val msg = getString(R.string.action_up)
                Log.i(TAG, msg)
                textView.text = msg
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                val msg = getString(R.string.action_cancel)
                Log.i(TAG, msg)
                textView.text = msg
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                val msg = getString(R.string.action_outside)
                Log.i(TAG, msg)
                textView.text = msg
                true
            }
            else -> super.onTouchEvent(motionEvent)
        }
    }

    companion object {
        private const val TAG = "MotionEvent"
    }
}