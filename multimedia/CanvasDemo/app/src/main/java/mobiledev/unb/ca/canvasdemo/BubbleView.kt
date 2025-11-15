package mobiledev.unb.ca.canvasdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.graphics.scale
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BubbleView(context: Context) : View(context) {
    private lateinit var listener: BubbleListener

    // Reference to the thread job
    private var periodicJob: Job? = null

    // Painter object to redraw
    private val painter = Paint()

    // How fast we are moving
    private var mStepX: Int
    private var mStepY: Int

    // Display dimensions
    private var xPosition: Float = context.displayWidth / 2.0f
    private var yPosition: Float = context.displayHeight / 2.0f

    // Reference to the scaled bitmap object
    private var scaledBitmap: Bitmap
    private var radius: Float = BITMAP_SIZE / 2.0f

    init {
        // Set the bubble image from the drawable resource
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.b64)
        scaledBitmap = bitmap.scale(BITMAP_SIZE, BITMAP_SIZE, false)

        // Pick a random x and y step
        mStepX = generateRandomNumberInRange(-10, 10)
        mStepY = generateRandomNumberInRange(-10, 10)

        // Smooth out the edges
        painter.isAntiAlias = true
    }

    fun setListener(listener: BubbleListener) {
        this.listener = listener
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(scaledBitmap, xPosition, yPosition, painter)
    }

    fun startMovement(startXPosition: Float,
                      startYPosition: Float) {
        // Adjust position to center the bubble under user's finger
         xPosition = startXPosition - radius
         yPosition = startYPosition - radius

        // Launch a new coroutine tied to the service lifecycle
        periodicJob = CoroutineScope(Dispatchers.IO + CoroutineName("BubbleMovement")).launch {
            while (true) { // Check for cancellation
                val stillOnScreen = moveWhileOnScreen()
                if (stillOnScreen) {
                    this@BubbleView.postInvalidate()
                } else {
                    stopMovement()
                }

                // Wait for a fixed delay between the end of one task and the start of the next
                delay(REFRESH_RATE.toLong()) // 5 seconds
            }
        }
    }

    private fun stopMovement() {
        periodicJob?.cancel()
        listener.onBubbleViewRemoved(bubbleView = this)
    }

    private fun moveWhileOnScreen(): Boolean {
        xPosition += mStepX
        yPosition += mStepY

        // Return true if the BubbleView is on the screen
        return xPosition <= context.displayWidth
                && xPosition + BITMAP_SIZE >= 0
                && yPosition <= context.displayHeight
                && yPosition + BITMAP_SIZE >= 0
    }

    private fun generateRandomNumberInRange(min: Int, max: Int): Int {
        return (min..max).random()
    }

    companion object {
        private const val BITMAP_SIZE = 64
        private const val REFRESH_RATE = 40
    }
}