package mobiledev.unb.ca.temperatureconverterdemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mobiledev.unb.ca.temperatureconverterdemo.utils.ConverterUtils

class MainActivity : AppCompatActivity() {
    private lateinit var enterTempValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        enterTempValue = findViewById(R.id.enterTempValue)
        val calculateButton: Button = findViewById(R.id.btnCalculate)
        calculateButton.setOnClickListener {
            btnCalculateOnClickHandler()
        }
    }

    fun btnCalculateOnClickHandler() {
            val textStr = enterTempValue.text.toString()
            if (textStr.isEmpty()) {
                Toast.makeText(this, "Please enter a valid number",
                    Toast.LENGTH_LONG).show()
            }
            val inputValue = getInputValue(textStr)
            val celsiusButton: RadioButton = findViewById(R.id.rbCelcius)
            val fahrenheitButton: RadioButton = findViewById(R.id.rbFahrenhiet)

            if (celsiusButton.isChecked) {
                enterTempValue.setText(ConverterUtils.convertCelsiusToFahrenheit(inputValue).toString())
                celsiusButton.isChecked = false
                fahrenheitButton.isChecked = true
            } else {
                enterTempValue.setText(ConverterUtils.convertFahrenheitToCelsius(inputValue).toString())
                fahrenheitButton.isChecked = false
                celsiusButton.isChecked = true
            }
    }

    private fun getInputValue(textStr: String): Float {
        return textStr.toFloat()
    }
}