package mobiledev.unb.ca.pinchandzoomdemo

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    // ScaleGestureListener is used to handle pinch gestures
    private var scaleGestureDetector: ScaleGestureDetector? = null

    private var imageView: ImageView? = null
    private var pinchToZoom = false

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
        imageView?.setOnTouchListener(onTouchListener())

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(imageView!!))
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        scaleGestureDetector?.onTouchEvent(ev)
        return true
    }

    private suspend fun resetFlag() {
        delay(500) // Delay for half a second
        withContext(Dispatchers.Main) {
           pinchToZoom = false
        }
    }

    internal inner class ScaleListener internal constructor(private var mImageView: ImageView): SimpleOnScaleGestureListener() {
        private var scaleFactor = 1.0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector!!.scaleFactor
            scaleFactor = max(0.5f, min(scaleFactor, 2.0f))
            mImageView.scaleX = scaleFactor
            mImageView.scaleY = scaleFactor
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            pinchToZoom = true
            return super.onScaleBegin(detector)
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            lifecycleScope.launch {
                resetFlag()
            }
            super.onScaleEnd(detector)
        }
    }

    private fun onTouchListener(): OnTouchListener {
        return object : OnTouchListener {
            var dX: Float = 0f
            var dY: Float = 0f
            var lastAction: Int = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                    }

                    MotionEvent.ACTION_MOVE -> {
                        onTouchEvent(event)
                        if (!pinchToZoom) {
                            view.y = event.rawY + dY
                            view.x = event.rawX + dX
                            lastAction = MotionEvent.ACTION_MOVE
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        pinchToZoom = false
                    }

                    MotionEvent.ACTION_POINTER_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                        lastAction = MotionEvent.ACTION_POINTER_DOWN
                    }

                    else -> return false
                }
                return true
            }
        }
    }
}