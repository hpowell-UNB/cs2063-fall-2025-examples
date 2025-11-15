package mobiledev.unb.ca.musicplayingservicedemo

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

val Context.notificationChannelId: String
    get() = "$packageName.channel_01"

class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionResultLauncher: ActivityResultLauncher<String>
    private lateinit var musicServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkNotificationPermissions()

        // Intent used for starting the MusicService
        musicServiceIntent = Intent(
            applicationContext,
            MusicPlayerService::class.java
        )

        val startButton: Button = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            startMusicService()
        }

        val stopButton: Button = findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            stopMusicService()
        }
    }

    // Permissions functions
    private fun setRequestPermissionResultLauncher() {
        // Handle the request permission launcher result
        requestPermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "Permission granted")
                createNotificationChannel()
            } else {
                Log.i(TAG, "Permission denied")
                showToast(getString(R.string.permission_denied))
            }
        }
    }

    private fun checkNotificationPermissions() {
        setRequestPermissionResultLauncher()

        // Check and request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33)
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    createNotificationChannel()
                }
                else -> {
                    // Request permission access
                    requestPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No runtime permission is needed for notifications when using Android versions below 13
            createNotificationChannel()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun startMusicService() {
        // Start the service using the intent
        startService(musicServiceIntent)
    }

    private fun stopMusicService() {
        // Stop the service using the intent
        stopService(musicServiceIntent)
    }

    private fun createNotificationChannel() {
        val context = applicationContext
        // Set the user visible channel name
        val name: CharSequence = context.getString(R.string.channel_name)

        // Set the user visible channel description
        val description = context.getString(R.string.channel_description)

        // Set the channel importance
        val importance = NotificationManager.IMPORTANCE_NONE

        // Create the NotificationChannel object
        val channel = NotificationChannel(context.notificationChannelId, name, importance)
        channel.description = description
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.enableLights(true)
        channel.enableVibration(false)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        channel.lightColor = Color.GREEN

        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}