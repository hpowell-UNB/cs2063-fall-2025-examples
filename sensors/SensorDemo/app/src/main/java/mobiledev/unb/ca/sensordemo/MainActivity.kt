package mobiledev.unb.ca.sensordemo

import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SensorEventListener, BallListener {
    // References to SensorManager and accelerometer
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Attribute to storing the last sensor update
    private var lastUpdateMillis: Long = 0

    // The Main view
    private lateinit var mainViewFrame: RelativeLayout

    // Gesture Detector
    private lateinit var gestureDetector: GestureDetector

    // A TextView to hold the player message
    private lateinit var playerMessage: TextView
    private var currFilter = NO_FILTER

    // Arrays for storing filtered values
    private val gravity = FloatArray(3)
    private val acceleration = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up user interface
        mainViewFrame = findViewById(R.id.mainViewFrame)

        // Set up text view
        playerMessage = findViewById(R.id.playerMessage)

        // Initialize the sensor for use
        initSensor()

        // Lock the display in landscape
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun initSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (null == accelerometer) {
            Toast.makeText(applicationContext,
                getString(R.string.accelerometer_error),
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // Register a listener for the accelerometer
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        lastUpdateMillis = System.currentTimeMillis()

        // Setup the gesture listener
        setupGestureDetector()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Apply a low- and high-pass filter to the raw sensor values
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val actualTime = System.currentTimeMillis()
            if (actualTime - lastUpdateMillis > REFRESH_RATE) {
                lastUpdateMillis = actualTime
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = applyLowPassFilter(x, gravity[0])
                gravity[1] = applyLowPassFilter(y, gravity[1])
                gravity[2] = applyLowPassFilter(z, gravity[2])

                // Remove the gravity contribution with the high-pass filter.
                acceleration[0] = applyHighPassFilter(x, gravity[0])
                acceleration[1] = applyHighPassFilter(y, gravity[1])
                acceleration[2] = applyHighPassFilter(z, gravity[2])

                // If there is a BallView, use its setSpeedAndDirection() method
                // to set its speed and direction based on the sensor values and the
                // current setting of mFilter, which will be one of NO_FILTER, LOW_PASS_FILTER, or HIGH_PASS_FILTER.
                val ballView = mainViewFrame.getChildAt(0) as? BallView
                if (null != ballView) {
                    when (currFilter) {
                        NO_FILTER -> {
                            ballView.setSpeedAndDirection(x, y)
                        }
                        LOW_PASS_FILTER -> {
                            ballView.setSpeedAndDirection(gravity[0], gravity[1])
                        }
                        HIGH_PASS_FILTER -> {
                            ballView.setSpeedAndDirection(acceleration[0], acceleration[1])
                        }
                        else -> {
                            Log.w(TAG, "Unknown filter type $currFilter.  No action taken")
                        }
                    }
                }
            }
        }
    }

    // Nothing to do here, just note that onAccuracyChanged must be implemented
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        // For now we will leave this unimplemented.
    }

    // De-emphasize transient forces
    private fun applyLowPassFilter(current: Float, gravity: Float): Float {
        val alpha = 0.8f
        return gravity * alpha + current * (1 - alpha)
    }

    // De-emphasize constant forces
    private fun applyHighPassFilter(current: Float, gravity: Float): Float {
        return current - gravity
    }

    // Set up GestureDetector
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // If there is a BallView, and a single tap intersects it, remove it.
            // If there are no BallViews, create a new BallView at the tap's location
            // and add it to mFrame.
            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                val x = event.rawX
                val y = event.rawY
                val numBalls = mainViewFrame.childCount
                if (numBalls != 0) {
                    val bv = mainViewFrame.getChildAt(0) as BallView
                    if (bv.intersects(x, y)) {
                        bv.stopMovement()
                    }
                } else {
                    val context = mainViewFrame.context
                    val ballView = BallView(context)
                    ballView.setListener(this@MainActivity)
                    mainViewFrame.addView(ballView)
                    ballView.startMovement(x,y)
                    setPlayerMessage()
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                // Cycle through the filter options
                currFilter = (currFilter + 1) % 3
                setPlayerMessage()
            }
        })
    }

    // Update the message that is displayed to the player
    fun setPlayerMessage() {
        val message: String = if (mainViewFrame.isEmpty()) {
            getString(R.string.lbl_tap_to_create_ball)
        } else {
            when (currFilter) {
                NO_FILTER -> {
                    getString(R.string.no_filter_message)
                }
                LOW_PASS_FILTER -> {
                    getString(R.string.lowpass_filter_message)
                }
                else -> {
                    getString(R.string.highpass_filter_message)
                }
            }
        }

        playerMessage.text = message
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()

        // Unregister the sensor event listener
        sensorManager.unregisterListener(this)
    }

    override fun onBallViewRemoved(ballView: BallView) {
        lifecycleScope.launch {
            mainViewFrame.removeView(ballView)
            setPlayerMessage()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val NO_FILTER = 0
        private const val LOW_PASS_FILTER = 1
        private const val HIGH_PASS_FILTER = 2
        private const val REFRESH_RATE = 5
    }
}