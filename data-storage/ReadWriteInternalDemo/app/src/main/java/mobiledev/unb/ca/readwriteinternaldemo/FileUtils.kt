package mobiledev.unb.ca.readwriteinternaldemo

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException

class FileUtils(private val appContext: Context) {
    suspend fun writeDataToFile(fileName: String, fileData: String) {
        withContext(Dispatchers.IO) {
            try {
                appContext.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                    fos.write(fileData.toByteArray())
                }
            } catch (e: IOException) {
                when (e) {
                    is FileNotFoundException -> Log.e(
                        TAG,
                        "writeDataToFile - IO Exception: ${e.message}"
                    )

                    else -> e.printStackTrace()
                }
            }
        }
    }

    suspend fun readDataFromFile(fileName: String): String {
        return withContext(Dispatchers.IO) {
            try {
                appContext.openFileInput(fileName).bufferedReader().use { reader ->
                    reader.readText()
                }
            } catch (e: IOException) {
                when (e) {
                    is FileNotFoundException -> Log.e(
                        TAG,
                        "readDataFromFile - File not found: ${e.message}"
                    )

                    else -> e.printStackTrace()
                }
                ""
            }
        }
    }

    companion object {
        private const val TAG = "FileUtils"
    }
}