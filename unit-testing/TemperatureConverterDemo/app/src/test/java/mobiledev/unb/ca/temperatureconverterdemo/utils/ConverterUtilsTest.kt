package mobiledev.unb.ca.temperatureconverterdemo.utils

import mobiledev.unb.ca.temperatureconverterdemo.utils.ConverterUtils.convertCelsiusToFahrenheit
import mobiledev.unb.ca.temperatureconverterdemo.utils.ConverterUtils.convertFahrenheitToCelsius
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class ConverterUtilsTest {
    // convertFahrenheitToCelsius tests
    @Test(expected = IllegalArgumentException::class)
    fun testConvertFahrenheitToCelsius_WhenFahrenheitIsNull() {
        convertFahrenheitToCelsius(null)
    }

    @Test
    fun testConvertFahrenheitToCelsius_WhenFahrenheitIsLessThanZero() {
        val expected = -3.88f
        val actual = convertFahrenheitToCelsius(25.0f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }

    @Test
    fun testConvertFahrenheitToCelsius_WhenFahrenheitIsZero() {
        val expected = 0.0f
        val actual = convertFahrenheitToCelsius(32.0f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }

    @Test
    fun testConvertFahrenheitToCelsius_WhenFahrenheitIsGreaterThanZero() {
        val expected = 4.44f
        val actual = convertFahrenheitToCelsius(40.0f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }

    // convertCelsiusToFahrenheit tests
    @Test(expected = IllegalArgumentException::class)
    fun testConvertCelsiusToFahrenheit_WhenCelsiusIsNull() {
        convertCelsiusToFahrenheit(null)
    }

    @Test
    fun testConvertCelsiusToFahrenheit_WhenCelsiusIsLessThanZero() {
        val expected = 25.016f
        val actual = convertCelsiusToFahrenheit(-3.88f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }

    @Test
    fun testConvertCelsiusToFahrenheit_WhenCelsiusIsZero() {
        val expected = 32.0f
        val actual = convertCelsiusToFahrenheit(0.0f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }

    @Test
    fun testConvertCelsiusToFahrenheit_WhenCelsiusIsGreaterThanZero() {
        val expected = 40.0f
        val actual = convertCelsiusToFahrenheit(4.44f)
        Assert.assertEquals(actual.toDouble(), expected.toDouble(), 0.01)
    }
}