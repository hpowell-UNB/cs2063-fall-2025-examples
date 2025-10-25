package mobiledev.unb.ca.mapgeointentdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        val addressText: EditText = findViewById(R.id.location)

        // Link UI elements to actions in code
        val button: Button = findViewById(R.id.mapButton)
        button.setOnClickListener {
            try {
                // Process text for network transmission
                var address = addressText.text.toString()
                address = address.replace(' ', '+')

                // Create Intent object for starting Google Maps application
                val geoIntent = Intent(
                    Intent.ACTION_VIEW, "geo:0,0?q=$address".toUri()
                )
                startActivity(geoIntent)
            } catch (e: Exception) {
                // Log any error messages to LogCat using Log.e()
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        private const val TAG = "MapGeoIntentDemo"
    }
}