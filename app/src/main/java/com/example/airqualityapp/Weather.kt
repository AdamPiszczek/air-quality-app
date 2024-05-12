package com.example.airqualityapp

import org.json.JSONArray
import java.io.File
import java.lang.Exception
import java.net.URL

class Weather(private val cityName: String) {
    private val token = File("src/main/kotlin/token.csv").inputStream().readBytes().toString(Charsets.UTF_8)
    private var status: String = ""
    private var data: String = ""

    private fun downloadData() {
        val json = URL("https://api.waqi.info/feed/$cityName/?token=$token").readText()
        val jsonRaw = JSONArray(json).getJSONObject(0)
        status = jsonRaw.getString("status")
        data = jsonRaw.getString("data")
    }
    fun printCurrentWeather(){
        try {
            this.downloadData()
            println(this.data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}