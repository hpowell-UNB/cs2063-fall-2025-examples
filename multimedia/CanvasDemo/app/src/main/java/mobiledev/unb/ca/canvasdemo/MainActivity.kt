package mobiledev.unb.ca.canvasdemo

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), BubbleListener {
    private lateinit var mainViewFrame: RelativeLayout

    // Gesture Detector
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewFrame = findViewById(R.id.mainViewFrame)
    }

    override fun onResume() {
        super.onResume()

        // Setup the gesture listener
        setupGestureDetector()
    }

    override fun onBubbleViewRemoved(bubbleView: BubbleView) {
        lifecycleScope.launch {
            mainViewFrame.removeView(bubbleView)
        }
    }

    // Set up GestureDetector
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(motionEvent: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                val x = event.rawX
                val y = event.rawY

                val numBubbles = 5
                repeat(numBubbles) {
                    val bubbleView = BubbleView(applicationContext)
                    bubbleView.setListener(this@MainActivity)
                    bubbleView.startMovement(startXPosition = x, startYPosition = y)
                    mainViewFrame.addView(bubbleView)
                }
                return true
            }
        })
    }
}