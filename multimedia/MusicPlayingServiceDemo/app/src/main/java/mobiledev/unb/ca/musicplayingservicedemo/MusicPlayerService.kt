package mobiledev.unb.ca.musicplayingservicedemo

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var mStartId = 0

    override fun onCreate() {
        super.onCreate()

        // Set up the Media Player
        mediaPlayer = MediaPlayer.create(this, R.raw.badnews)
        mediaPlayer?.apply {
            isLooping = false
            setOnCompletionListener {
                stopSelf(mStartId)
            }
        }

        startForegroundService(applicationContext, applicationContext.notificationChannelId)
    }

    override fun onBind(intent: Intent): IBinder? {
        // Can't bind to this Service
        return null
    }

    override fun onDestroy() {
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaPlayer?.apply {
            // ID for this start command
            mStartId = startId
            if (isPlaying) {
                // Rewind to the beginning of the song
                seekTo(0)
            } else {
                // Start playing song
                start()
            }
        }

        // Don't automatically restart this Service if it is killed
        return START_NOT_STICKY
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        // Set the tap action
        val notificationIntent = Intent(
            context,
            MainActivity::class.java
        )

        return PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startForegroundService(context: Context, channelId: String) {
        val pendingIntent: PendingIntent = getPendingIntent(context)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setContentIntent(pendingIntent)
            .build()

        // Put this Service in a foreground state, so it won't
        // readily be killed by the system
        ServiceCompat.startForeground(
            this,  // service
            NOTIFICATION_ID, // id; cannot be 0
            notification,  // notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            },  // foregroundServiceType
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}