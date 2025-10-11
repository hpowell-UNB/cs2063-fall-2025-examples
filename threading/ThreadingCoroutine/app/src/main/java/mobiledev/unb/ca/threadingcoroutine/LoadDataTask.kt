package mobiledev.unb.ca.threadingcoroutine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadIconTask(private val appContext: Context) {
    private var imageView: ImageView? = null
    private var progressBar: ProgressBar? = null

    fun setImageView(imageView: ImageView?): LoadIconTask {
        this.imageView = imageView
        return this
    }

    fun setProgressBar(progressBar: ProgressBar?): LoadIconTask {
        this.progressBar = progressBar
        return this
    }

    fun execute() {
        MainScope().launch(Dispatchers.IO) {
            // Decode the image
            val bitmap = BitmapFactory.decodeResource(appContext.resources, PAINTER)

            launch(Dispatchers.Main) {
                // Show the progress bar
                progressBar?.visibility = ProgressBar.VISIBLE

                // Simulating long-running operation
                for (i in 0..DOWNLOAD_TIME) {
                    sleep()
                    progressBar?.progress = i * 10
                }

                updateDisplay(bitmap)
            }
        }
    }

    private fun updateDisplay(bitmap: Bitmap) {
        // Show the icon
        // NOTE: Already included in handler.post operation
        imageView?.setImageBitmap(bitmap)

        //  Reset the progress bar, and make it disappear
        // NOTE: Already included in handler.post operation
        progressBar?.visibility = ProgressBar.INVISIBLE

        //  Create a Toast indicating that the image is loaded
        Toast.makeText(
            appContext,
            "Image Loaded",
            Toast.LENGTH_SHORT
        ).show()
    }

    private suspend fun sleep() {
        delay(500)
    }

    companion object {
        private val PAINTER = R.drawable.painter
        private const val DOWNLOAD_TIME = 10 // Download time simulation
    }
}