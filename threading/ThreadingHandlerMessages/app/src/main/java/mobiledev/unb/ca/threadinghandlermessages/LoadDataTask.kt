package mobiledev.unb.ca.threadinghandlermessages

import android.content.Context
import android.widget.ProgressBar
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView

class LoadIconTask (private val appContext: Context) : Thread() {
    private val handler: UIHandler = UIHandler(Looper.getMainLooper())

    override fun run() {
        // Show the progress bar
        var message = handler.obtainMessage(
            SET_PROGRESS_BAR_VISIBILITY,
            ProgressBar.VISIBLE)
        handler.sendMessage(message)

        // Load the image from resources; update the progress bar
        val painterResId = R.drawable.painter
        val tmp = BitmapFactory.decodeResource(appContext.resources, painterResId)
        for (i in 1..10) {
            sleep()
            message = handler.obtainMessage(PROGRESS_UPDATE, i * 10)
            handler.sendMessage(message)
        }

        // Show the image
        message = handler.obtainMessage(SET_BITMAP, tmp)
        handler.sendMessage(message)

        // Hide the progress bar
        message = handler.obtainMessage(
            SET_PROGRESS_BAR_VISIBILITY,
            ProgressBar.INVISIBLE)
        handler.sendMessage(message)
    }

    fun setImageView(imageView: ImageView?): LoadIconTask {
        handler.setImageView(imageView)
        return this
    }

    fun setProgressBar(progressBar: ProgressBar?): LoadIconTask {
        handler.setProgressBar(progressBar)
        return this
    }

    private fun sleep() {
        try {
            sleep(DELAY_TIME.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    internal class UIHandler(looper: Looper) : Handler(looper) {
        private var imageView: ImageView? = null
        private var progressBar: ProgressBar? = null

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SET_PROGRESS_BAR_VISIBILITY -> {
                    progressBar?.visibility = (msg.obj as Int)
                }
                PROGRESS_UPDATE -> {
                    progressBar?.progress = (msg.obj as Int)
                }
                SET_BITMAP -> {
                    imageView?.setImageBitmap(msg.obj as Bitmap)
                }
            }
        }

        fun setImageView(imageView: ImageView?) {
            this.imageView = imageView
        }

        fun setProgressBar(progressBar: ProgressBar?) {
            this.progressBar = progressBar
        }
    }

    companion object {
        const val DELAY_TIME = 500
        const val SET_PROGRESS_BAR_VISIBILITY = 0
        const val PROGRESS_UPDATE = 1
        const val SET_BITMAP = 2
    }
}