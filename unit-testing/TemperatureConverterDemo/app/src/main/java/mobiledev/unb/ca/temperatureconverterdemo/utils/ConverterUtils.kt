package mobiledev.unb.ca.temperatureconverterdemo.utils
object ConverterUtils {
    @JvmStatic
    fun convertFahrenheitToCelsius(fahrenheit: Float?): Float {
        requireNotNull(fahrenheit) { "fahrenheit is missing" }
        return (fahrenheit - 32) * 5 / 9
    }

    @JvmStatic
    fun convertCelsiusToFahrenheit(celsius: Float?): Float {
        requireNotNull(celsius) { "celsius is missing" }
        return celsius * 9 / 5 + 32
    }
}