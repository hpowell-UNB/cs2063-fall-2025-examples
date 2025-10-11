package mobiledev.unb.ca.httpurlconnectiondemo.utils

import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ApiHelper {
    fun fetchDataFromUrl(url: String) : String? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                return when (connection.responseCode) {
                    HttpURLConnection.HTTP_OK -> connection.inputStream.bufferedReader()
                        .use { it.readText() }

                    else -> throw Exception("HTTP error code: $connection.responseCode")
                }
            } finally {
                connection.disconnect()
            }
        } catch (e: IOException) {
            Log.e("ApiHelper", "Error executing  api call", e)
            null
        }
    }
}