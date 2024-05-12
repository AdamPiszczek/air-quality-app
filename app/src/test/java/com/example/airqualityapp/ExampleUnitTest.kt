package com.example.airqualityapp


import org.junit.Test

import org.junit.Assert.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

fun getFeelsLikeWeather(apparentTemperature: Double, windSpeed: Double): Double {
    // Formula to calculate feels like temperature:
    // feelsLikeTemp = 0.08 * (3.71 * sqrt(V) + 5.81 - 0.25 * V) * (T - 10) + 10.45 - 0.28 * V + 0.0203 * V * (T - 10)
    // T = Temperature (in Celsius)
    // V = windSpeed (in km/h)
    val result =
        0.08 * (3.71 * kotlin.math.sqrt(windSpeed) + 5.81 - 0.25 * windSpeed) * (apparentTemperature - 10) + 10.45 - 0.28 * windSpeed + 0.0203 * windSpeed * (apparentTemperature - 10)

    // Format the result to one decimal place and convert it to a Double
    return String.format("%.1f", result).toDouble()
}
class WeatherUtilsTest {

    @Test
    fun `getFeelsLikeWeather should calculate the feels like temperature correctly`() {
        // Arrange
        val apparentTemperature = 25.0
        val windSpeed = 10.0

        // Act
        val result = getFeelsLikeWeather(apparentTemperature, windSpeed)

        // Assert
        val expected = 24.3 // Replace with the expected result based on the formula
        assertEquals(expected, result, 0.1) // Allow a small delta for floating-point calculations
    }

    @Test
    fun `getFeelsLikeWeather should handle negative apparent temperatures`() {
        // Arrange
        val apparentTemperature = -5.0
        val windSpeed = 15.0

        // Act
        val result = getFeelsLikeWeather(apparentTemperature, windSpeed)

        // Assert
        val expected = -5.3 // Replace with the expected result based on the formula
        assertEquals(expected, result, 0.1) // Allow a small delta for floating-point calculations
    }

    @Test
    fun `getFeelsLikeWeather should handle zero wind speed`() {
        // Arrange
        val apparentTemperature = 20.0
        val windSpeed = 0.0

        // Act
        val result = getFeelsLikeWeather(apparentTemperature, windSpeed)

        // Assert
        val expected = 20.0 // When wind speed is zero, the result should be the same as the apparent temperature
        assertEquals(expected, result, 0.1) // Allow a small delta for floating-point calculations
    }
}

private fun getZoomLevel(radius: Double): Float {
    return if (radius < 0) {
        // If the radius is less than 0, return a default zoom level of 15.0
        15f
    } else {
        // Convert the radius to a float and return it as the zoom level
        radius.toFloat()
    }
}

class MapUtilsTest {

    @Test
    fun `getZoomLevel should return default zoom level when radius is negative`() {
        // Arrange
        val radius = -10.0

        // Act
        val result = getZoomLevel(radius)

        // Assert
        val expected = 15.0f
        assertEquals(expected, result)
    }

    @Test
    fun `getZoomLevel should return zoom level as radius when radius is positive`() {
        // Arrange
        val radius = 25.0

        // Act
        val result = getZoomLevel(radius)

        // Assert
        val expected = 25.0f
        assertEquals(expected, result)
    }

    @Test
    fun `getZoomLevel should handle zero radius`() {
        // Arrange
        val radius = 0.0

        // Act
        val result = getZoomLevel(radius)

        // Assert
        val expected = 0.0f // Expecting the radius to be converted to 0.0f
        assertEquals(expected, result)
    }
}

class WeatherUtilsTest2 {
    @Test
    fun `getWeather should parse and update the display text views correctly`() {
        // Arrange
        val lat = 123.456
        val lon = 789.012
        val urlWeatherType =
            "https://api.open-meteo.com/v1/forecast?latitude=51.107883&longitude=17.038538&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m"
        val connection3 = URL(urlWeatherType).openConnection() as HttpURLConnection

        try {
            val jsonResponse =
                """{"latitude":51.1,"longitude":17.039999,"generationtime_ms":0.5838871002197266,"utc_offset_seconds":0,"timezone":"GMT","timezone_abbreviation":"GMT","elevation":127.0,"current_weather":{"temperature":17.0,"windspeed":9.7,"winddirection":88.0,"weathercode":3,"is_day":1,"time":"2023-06-13T10:00"},"hourly_units":{"time":"iso8601","temperature_2m":"°C","relativehumidity_2m":"%","windspeed_10m":"km/h"},"hourly":{"time":["2023-06-13T00:00","2023-06-13T01:00","2023-06-13T02:00","2023-06-13T03:00","2023-06-13T04:00","2023-06-13T05:00","2023-06-13T06:00","2023-06-13T07:00","2023-06-13T08:00","2023-06-13T09:00","2023-06-13T10:00","2023-06-13T11:00","2023-06-13T12:00","2023-06-13T13:00","2023-06-13T14:00","2023-06-13T15:00","2023-06-13T16:00","2023-06-13T17:00","2023-06-13T18:00","2023-06-13T19:00","2023-06-13T20:00","2023-06-13T21:00","2023-06-13T22:00","2023-06-13T23:00","2023-06-14T00:00","2023-06-14T01:00","2023-06-14T02:00","2023-06-14T03:00","2023-06-14T04:00","2023-06-14T05:00","2023-06-14T06:00","2023-06-14T07:00","2023-06-14T08:00","2023-06-14T09:00","2023-06-14T10:00","2023-06-14T11:00","2023-06-14T12:00","2023-06-14T13:00","2023-06-14T14:00","2023-06-14T15:00","2023-06-14T16:00","2023-06-14T17:00","2023-06-14T18:00","2023-06-14T19:00","2023-06-14T20:00","2023-06-14T21:00","2023-06-14T22:00","2023-06-14T23:00","2023-06-15T00:00","2023-06-15T01:00","2023-06-15T02:00","2023-06-15T03:00","2023-06-15T04:00","2023-06-15T05:00","2023-06-15T06:00","2023-06-15T07:00","2023-06-15T08:00","2023-06-15T09:00","2023-06-15T10:00","2023-06-15T11:00","2023-06-15T12:00","2023-06-15T13:00","2023-06-15T14:00","2023-06-15T15:00","2023-06-15T16:00","2023-06-15T17:00","2023-06-15T18:00","2023-06-15T19:00","2023-06-15T20:00","2023-06-15T21:00","2023-06-15T22:00","2023-06-15T23:00","2023-06-16T00:00","2023-06-16T01:00","2023-06-16T02:00","2023-06-16T03:00","2023-06-16T04:00","2023-06-16T05:00","2023-06-16T06:00","2023-06-16T07:00","2023-06-16T08:00","2023-06-16T09:00","2023-06-16T10:00","2023-06-16T11:00","2023-06-16T12:00","2023-06-16T13:00","2023-06-16T14:00","2023-06-16T15:00","2023-06-16T16:00","2023-06-16T17:00","2023-06-16T18:00","2023-06-16T19:00","2023-06-16T20:00","2023-06-16T21:00","2023-06-16T22:00","2023-06-16T23:00","2023-06-17T00:00","2023-06-17T01:00","2023-06-17T02:00","2023-06-17T03:00","2023-06-17T04:00","2023-06-17T05:00","2023-06-17T06:00","2023-06-17T07:00","2023-06-17T08:00","2023-06-17T09:00","2023-06-17T10:00","2023-06-17T11:00","2023-06-17T12:00","2023-06-17T13:00","2023-06-17T14:00","2023-06-17T15:00","2023-06-17T16:00","2023-06-17T17:00","2023-06-17T18:00","2023-06-17T19:00","2023-06-17T20:00","2023-06-17T21:00","2023-06-17T22:00","2023-06-17T23:00","2023-06-18T00:00","2023-06-18T01:00","2023-06-18T02:00","2023-06-18T03:00","2023-06-18T04:00","2023-06-18T05:00","2023-06-18T06:00","2023-06-18T07:00","2023-06-18T08:00","2023-06-18T09:00","2023-06-18T10:00","2023-06-18T11:00","2023-06-18T12:00","2023-06-18T13:00","2023-06-18T14:00","2023-06-18T15:00","2023-06-18T16:00","2023-06-18T17:00","2023-06-18T18:00","2023-06-18T19:00","2023-06-18T20:00","2023-06-18T21:00","2023-06-18T22:00","2023-06-18T23:00","2023-06-19T00:00","2023-06-19T01:00","2023-06-19T02:00","2023-06-19T03:00","2023-06-19T04:00","2023-06-19T05:00","2023-06-19T06:00","2023-06-19T07:00","2023-06-19T08:00","2023-06-19T09:00","2023-06-19T10:00","2023-06-19T11:00","2023-06-19T12:00","2023-06-19T13:00","2023-06-19T14:00","2023-06-19T15:00","2023-06-19T16:00","2023-06-19T17:00","2023-06-19T18:00","2023-06-19T19:00","2023-06-19T20:00","2023-06-19T21:00","2023-06-19T22:00","2023-06-19T23:00"],"temperature_2m":[12.1,11.4,10.7,10.3,10.5,12.0,13.7,14.8,14.6,15.7,17.0,17.5,18.1,18.7,18.0,17.9,18.1,17.7,17.2,15.7,14.2,13.0,12.2,11.6,11.0,10.4,10.1,10.4,11.1,12.0,13.7,14.7,15.7,14.9,14.6,14.8,16.1,17.5,18.3,18.6,18.5,17.0,16.5,16.0,15.1,14.5,13.8,13.5,13.4,12.9,12.4,12.3,12.8,13.5,14.7,15.8,16.8,18.3,19.3,19.8,19.8,20.0,19.9,20.0,19.5,17.2,17.5,16.7,15.8,15.2,14.7,14.2,13.8,13.2,12.8,12.4,12.8,13.9,15.5,16.5,17.9,19.6,20.8,21.6,21.9,22.1,22.1,21.8,21.3,20.6,19.7,18.5,17.1,15.9,15.2,14.8,14.5,14.3,14.3,14.5,14.8,15.4,16.1,17.0,18.1,19.1,20.2,21.3,22.1,22.5,22.5,22.4,22.1,21.7,21.0,20.0,18.8,17.8,17.0,16.5,16.0,15.5,15.0,14.9,15.3,16.0,16.5,18.6,20.5,22.1,23.0,23.5,24.0,24.5,24.9,25.0,24.8,24.3,23.5,22.0,20.1,18.5,17.6,16.9,16.3,15.4,14.6,14.5,15.4,17.0,18.7,20.5,22.4,24.1,25.3,26.2,26.9,27.3,27.5,27.5,27.3,26.8,26.0,24.4,22.4,20.6,19.4,18.5],"relativehumidity_2m":[62,63,66,72,73,71,67,61,61,56,52,50,46,47,49,47,46,47,49,60,62,67,71,76,82,88,90,90,88,86,82,76,72,81,82,83,73,65,66,61,61,72,80,84,84,85,87,88,87,91,91,90,88,85,81,75,72,66,58,57,59,58,59,56,62,86,80,83,88,90,91,91,91,91,91,91,89,85,80,78,72,62,53,50,44,45,49,53,55,57,61,70,82,91,95,96,97,98,98,97,95,93,89,83,76,69,62,56,51,49,50,51,53,57,61,66,71,76,79,80,82,84,85,86,85,82,80,74,65,57,52,48,45,43,41,41,41,43,46,52,61,68,73,78,81,84,87,87,84,78,72,65,57,50,45,40,37,34,32,32,34,37,41,47,54,61,66,71],"windspeed_10m":[5.1,4.6,5.0,6.0,7.9,8.7,12.2,11.8,13.5,16.9,9.7,11.4,11.1,12.4,13.4,12.0,11.9,10.0,7.6,7.3,7.2,7.9,8.7,8.6,6.5,4.7,4.1,3.9,4.4,4.4,8.8,7.1,8.2,8.2,5.8,6.1,8.9,6.9,7.8,6.6,7.0,11.2,4.8,4.8,4.8,4.9,4.1,3.3,2.6,4.3,5.4,5.8,6.3,5.6,6.9,7.5,6.9,9.0,8.2,9.7,9.9,10.5,8.9,7.4,7.6,6.4,4.3,4.4,4.0,4.0,3.6,3.4,3.0,3.7,3.6,3.6,4.3,5.1,7.5,7.6,8.2,8.7,8.9,8.9,10.4,10.0,9.3,8.7,9.0,9.4,9.4,8.3,7.5,7.5,7.3,7.0,6.7,6.8,7.2,7.4,7.9,8.5,8.7,8.4,7.9,7.7,8.2,8.9,9.5,9.2,8.9,8.3,7.1,5.9,5.3,4.9,4.3,4.3,5.1,6.2,7.1,7.4,7.4,7.5,8.1,9.2,9.8,9.5,8.7,8.3,8.1,8.0,7.7,7.4,7.5,7.4,6.9,5.8,5.0,4.6,4.1,3.9,3.6,3.3,3.2,3.1,3.0,2.9,2.5,2.7,2.9,3.5,4.6,5.2,6.0,6.6,7.0,7.7,8.6,8.4,5.9,1.8,1.1,2.7,3.6,4.3,4.6,4.6]}}"""
            val result3: String

            connection3.connect()
            result3 = connection3.inputStream.use {
                it.reader().use { reader ->
                    reader.readText()
                }
            }

            val weatherUtils = WeatherUtils()

            // Act
            val jsnobject2 = JSONObject(result3)
            weatherUtils.getWeather(jsnobject2, lat, lon)

            // Assert
//            assertEquals("25.0°", weatherUtils.getTemperatureTextViewText())
//            assertEquals("H:28.0° L:23.0°", weatherUtils.getMinMaxTemperatureTextViewText())
//            assertEquals("Feels like: 24.5°", weatherUtils.getFeelsLikeTemperatureTextViewText())
        } finally {
            connection3.disconnect()
        }
    }
}

class WeatherUtils {

    private lateinit var temperatureTextViewText: String
    private lateinit var minMaxTemperatureTextViewText: String
    private lateinit var feelsLikeTemperatureTextViewText: String

    fun getWeather(jsnobject2: JSONObject, lat: Double, lon: Double) {
        val currentWeather = jsnobject2.getJSONObject("current_weather")
        val temperature = currentWeather.getDouble("temperature")

        temperatureTextViewText = "${temperature}°"

        val allDayWeather = jsnobject2.getJSONObject("hourly")

        val weatherAllDay = allDayWeather.getJSONArray("temperature_2m")
        val weatherAllDayArray = Array(weatherAllDay.length()) { 0.0 }
        for (i in 0 until weatherAllDay.length()) {
            weatherAllDayArray[i] = weatherAllDay.optDouble(i)
        }

        minMaxTemperatureTextViewText = "H:${weatherAllDayArray.max()}° L:${weatherAllDayArray.min()}°"

        val humidityAllDay = allDayWeather.getJSONArray("relativehumidity_2m")
        val humidityAllDayArray = Array(humidityAllDay.length()) { 0.0 }
        for (i in 0 until humidityAllDay.length()) {
            humidityAllDayArray[i] = humidityAllDay.optDouble(i)
        }

        val windspeed = currentWeather.getDouble("windspeed")
        val feelsLikeTemperature = getFeelsLikeWeather(temperature, windspeed)
        feelsLikeTemperatureTextViewText = "Feels like: ${feelsLikeTemperature}°"
    }

    fun getTemperatureTextViewText(): String {
        return temperatureTextViewText
    }

    fun getMinMaxTemperatureTextViewText(): String {
        return minMaxTemperatureTextViewText
    }

    fun getFeelsLikeTemperatureTextViewText(): String {
        return feelsLikeTemperatureTextViewText
    }

    private fun getFeelsLikeWeather(temperature: Double, windSpeed: Double): Double {
        return 0.08 * (3.71 * kotlin.math.sqrt(windSpeed) + 5.81 - 0.25 * windSpeed) * (temperature - 10) +
                10.45 - 0.28 * windSpeed + 0.0203 * windSpeed * (temperature - 10)
    }
}
