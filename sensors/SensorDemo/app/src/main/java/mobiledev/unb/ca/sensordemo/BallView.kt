package mobiledev.unb.ca.sensordemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.core.graphics.withSave
import androidx.core.graphics.scale
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BallView(context: Context) : View(context) {
    private lateinit var listener: BallListener

    // Reference to the scaled bitmap object
    private var scaledBitmap: Bitmap

    private val painter = Paint()
    private var periodicJob: Job? = null

    // location and direction of the ball
    private var xPosition: Float = 0.0f
    private var yPosition: Float = 0.0f
    private val radius: Float = SCALED_BITMAP_SIZE / 2.0f

    // Display dimensions
    private var displayWidth = 0
    private var displayHeight = 0

    // Speed of the ball
    private var mDx: Float
    private var mDy: Float

    // Rotation and speed of rotation of the ball
    private var rotate: Long = 0
    private val speedOfRotation: Long

    fun setListener(listener: BallListener) {
        this.listener = listener
    }

    // setSpeedAndDirection called by onSensorChanged(), values based on
    // accelerometer data and scaled
    fun setSpeedAndDirection(x: Float, y: Float) {
        // Make the ball go faster
        mDx = 2 * y
        mDy = 2 * x

        // Uncomment this to make the ball accelerate based on sensor input
        // mDx += y
        // mDy += x
    }

    // Start moving the BallView & update the display
    fun startMovement(startXPosition: Float,
                      startYPosition: Float) {
        // Adjust position to center the ball under user's finger
        xPosition = startXPosition - radius
        yPosition = startYPosition - radius

        // Launch a new coroutine tied to the service lifecycle
        periodicJob = CoroutineScope(Dispatchers.IO + CoroutineName("BallMovement")).launch {
            while (true) { // Check for cancellation
                try {
                    doMove()
                    this@BallView.postInvalidate()
                } catch (e: Exception) {
                    println("Error moving the ball: ${e.message}")
                }
                // Wait for a fixed delay between the end of one task and the start of the next
                delay(REFRESH_RATE.toLong()) // 5 seconds
            }
        }
    }

    fun stopMovement() {
        periodicJob?.cancel()
        listener.onBallViewRemoved(this)
    }

    // Return true if x and y intersect the position of the ball
    @Synchronized
    fun intersects(x: Float, y: Float): Boolean {
        val centerX = xPosition + radius
        val centerY = yPosition + radius
        return sqrt((centerX - x).toDouble().pow(2.0) + (centerY - y).toDouble().pow(2.0)) <= radius
    }

    // Draw the ball at its current location
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            rotate += speedOfRotation
            rotate(rotate.toFloat(), xPosition + radius, yPosition + radius)
            drawBitmap(scaledBitmap, xPosition, yPosition, painter)
        }
    }

    // Move the ball around the screen
    @Synchronized
    private fun doMove() {
        // Don't let the ball go beyond the edge of the screen
        // Set the speed to 0 if the ball hits an edge.
        xPosition += mDx
        if (xPosition >= displayWidth) {
            xPosition = displayWidth.toFloat()
            mDx = 0f
        } else if (xPosition <= 0) {
            xPosition = 0f
            mDx = 0f
        }

        yPosition += mDy
        if (yPosition >= displayHeight) {
            yPosition = displayHeight.toFloat()
            mDy = 0f
        } else if (yPosition <= 0) {
            yPosition = 0f
            mDy = 0f
        }
    }

    init {
        // Read in the ball bitmap image
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ball)
        scaledBitmap = bitmap.scale(
            SCALED_BITMAP_SIZE,
            SCALED_BITMAP_SIZE
        )

        // Subtract diameter of the ball from width and height
        displayWidth = context.displayWidth - SCALED_BITMAP_SIZE
        displayHeight = context.displayHeight - SCALED_BITMAP_SIZE

        // Set speed to 0 initially; it will be updated soon based on sensor input
        mDx = 0f
        mDy = 0f

        // Set speed of rotation to 5
        speedOfRotation = 5
        painter.isAntiAlias = true
    }

    companion object {
        private const val BITMAP_SIZE = 128
        private const val REFRESH_RATE = 5
        private const val SCALED_BITMAP_SIZE = BITMAP_SIZE * 2
    }
}