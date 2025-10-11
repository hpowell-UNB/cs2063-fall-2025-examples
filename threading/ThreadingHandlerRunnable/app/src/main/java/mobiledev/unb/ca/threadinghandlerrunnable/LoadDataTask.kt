package  mobiledev.unb.ca.threadinghandlerrunnable

import android.content.Context
import android.widget.ProgressBar
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

class LoadIconTask (private val appContext: Context) : Thread() {
    private var imageView: ImageView? = null
    private var progressBar: ProgressBar? = null
    private val bitmapResID = R.drawable.painter

    fun setImageView(imageView: ImageView?): LoadIconTask {
        this.imageView = imageView
        return this
    }

    fun setProgressBar(progressBar: ProgressBar?): LoadIconTask {
        this.progressBar = progressBar
        return this
    }

    override fun run() {
        Handler(Looper.getMainLooper()).also { handler ->
            handler.post { progressBar?.visibility = ProgressBar.VISIBLE }

            // Simulating long-running operation
            for (i in 1..10) {
                sleep()
                handler.post { progressBar?.progress = i * 10 }
            }
            handler.post {
                imageView?.setImageBitmap(
                    BitmapFactory.decodeResource(appContext.resources, bitmapResID)
                )
            }
            handler.post { progressBar?.visibility = ProgressBar.INVISIBLE }
        }
    }

    private fun sleep() {
        try {
            sleep(DELAY_TIME.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val DELAY_TIME = 500
    }
}