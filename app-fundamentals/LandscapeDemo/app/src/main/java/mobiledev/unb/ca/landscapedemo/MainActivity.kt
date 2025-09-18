package mobiledev.unb.ca.landscapedemo

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // findViewById uses app context to lookup the button object
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val context = applicationContext
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Toast.makeText(
                    context,
                    getString(R.string.running_in_portrait_mode),  // getString makes use of context to read in resource string
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.running_in_landscape_mode),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}