class DataMockup(private val temperature: Double, private val humidity: Double) {

    // Method that returns the average temperature and humidity
    fun getAverage(): Pair<Double, Double> {
        val avgTemp = temperature
        val avgHumidity = humidity
        return Pair(avgTemp, avgHumidity)
    }

    // Method that calculates the heat index based on temperature and humidity
    fun calculateHeatIndex(): Double {
        val c1 = -42.379
        val c2 = 2.04901523
        val c3 = 10.14333127
        val c4 = -0.22475541
        val c5 = -0.00683783
        val c6 = -0.05481717
        val c7 = 0.00122874
        val c8 = 0.00085282
        val c9 = -0.00000199

        val t = temperature
        val rh = humidity

        val heatIndex =
            c1 + c2 * t + c3 * rh + c4 * t * rh + c5 * t * t + c6 * rh * rh + c7 * t * t * rh + c8 * t * rh * rh + c9 * t * t * rh * rh

        return heatIndex
    }

    // Method that returns a weather description based on temperature and humidity
    fun getWeatherDescription(): String {
        val temp = temperature

        return when {
            temp >= 32 -> "It's very hot and humid."
            temp >= 27 -> "It's hot and humid."
            temp >= 21 -> "It's warm and humid."
            temp >= 16 -> "It's cool and humid."
            else -> "It's cold and humid."
        }
    }
}