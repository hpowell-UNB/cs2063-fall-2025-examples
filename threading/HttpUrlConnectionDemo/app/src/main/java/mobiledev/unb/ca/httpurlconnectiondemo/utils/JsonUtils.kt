package mobiledev.unb.ca.httpurlconnectiondemo.utils

import mobiledev.unb.ca.httpurlconnectiondemo.models.CountryItem
import org.json.JSONArray
import org.json.JSONObject

class JsonUtils {
    lateinit var countryItemList: ArrayList<CountryItem>
        private set

    private fun processJSON() {
        countryItemList = ArrayList()

        val jsonString = loadJsonFromURL()

        jsonString?.let { jsonString ->
            val jsonArray = JSONArray(jsonString.trimIndent())
            (0 until jsonArray.length()).forEach { index ->
                val element: JSONObject = jsonArray.getJSONObject(index)
                val flagPic: String = element.getString("flag")
                val nameObject: JSONObject = element.getJSONObject("name")
                val countryName: String = nameObject.getString("official")
                val capitalArray: JSONArray = element.getJSONArray("capital")
                val capitalName: String = capitalArray.getString(0)
                val continentsArray: JSONArray = element.getJSONArray("continents")
                val continentName = continentsArray.getString(0)

                val countryItem = CountryItem(
                    name=countryName,
                    flagPic = flagPic,
                    capital = capitalName,
                    continent = continentName
                )

                countryItemList.add(countryItem)
            }
        }
    }

    private fun loadJsonFromURL(): String? {
        return ApiHelper.fetchDataFromUrl(REQUEST_URL)
    }

    // Initializer to read the data from the API endpoint
    init {
        processJSON()
    }

    companion object {
        private const val REQUEST_URL =
            "https://restcountries.com/v3.1/region/Americas?fields=name,capital,continents,flags,flag"
    }
}