package com.example.airqualityapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.airqualityapp.databinding.ActivityMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * City Lists
 *
 * Los Angeles
 * Mumbai
 * Shanghai
 * London
 * Warsaw
 * Wroclaw
 * Beijing
 * Shanghai
 * Gdynia
 * Katowice
 * Opole
 *
 */

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMaps: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentPosition = LatLng(51.107883, 17.038538) // set fixed location to Wrocław
    private var temperature = 0.0
    private var lat = 51.107883
    private var lon = 17.038538
    private var city = "Wrocław"
    private var imgShareUrl =
        "https://aqicn.org/snapshot/poland/dolnoslaskie/wroclaw-korzeniowskiego/20230520-19/pl/air-quality.png"

    /**
     * Displays a pop-up dialog to inform the user that they have entered the wrong input.
     *
     * @param context The context object to create the dialog.
     */
    private fun showWrongInputPopup(context: Context, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("An error occurred while downloading data!")
        alertDialogBuilder.setMessage(message)

        // Set the positive button text and action
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Dismiss the dialog when the user taps "OK"
        }

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    /**
     * Called when the activity is starting or being recreated.
     * Sets up the activity layout, initializes map fragment, handles search button click,
     * retrieves weather information, and updates the display with corresponding weather icons.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable sending requests in main thread
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)

        // Make the window full-screen
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        // Inflate the activity layout using View Binding
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set click listener for the search button
        val searchButton = findViewById<ImageButton>(R.id.searchIcon)
        searchButton.setOnClickListener {
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Enter city name!")
            val input = EditText(binding.root.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK", null)
            builder.setOnDismissListener {
                if (input.text.toString().isNotEmpty())
                    city = input.text.toString()

                refreshMap(input.text.toString())

                // Clear the displayed weather GIF
                val displayGif = findViewById<ImageView>(R.id.gifImageView)
                displayGif.setImageDrawable(null)

                // Retrieve weather information from the OpenWeatherMap API
                val urlWeatherType =
                    "..."
                val result3: String
                val connection3 = URL(urlWeatherType).openConnection() as HttpURLConnection
                try {
                    connection3.connect()
                    result3 = connection3.inputStream.use {
                        it.reader().use { reader ->
                            reader.readText()
                        }
                    }
                } finally {
                    connection3.disconnect()
                }
                try {
                    // Parse the JSON response to get weather details
                    val jsnobject3 = JSONObject(result3)
                    val weatherType = jsnobject3.getJSONArray("weather")
                    val weatherTypeMain = weatherType.getJSONObject(0)
                    val weatherMain = weatherTypeMain.getString("main")
                    val weatherTypeDescription = weatherType.getJSONObject(0)
                    val weatherDescription = weatherTypeDescription.getString("description")

                    // Update the weather icon based on the weather conditions
                    when (weatherMain) {
                        "Thunderstorm" -> displayGif.setImageResource(R.drawable.icon_11d)
                        "Drizzle" -> displayGif.setImageResource(R.drawable.icon_09d)
                        "Rain" -> {
                            when (weatherDescription) {
                                "light rain" -> displayGif.setImageResource(R.drawable.icon_10d)
                                "moderate rain" -> displayGif.setImageResource(R.drawable.icon_10d)
                                "heavy intensity rain" -> displayGif.setImageResource(R.drawable.icon_10d)
                                "very heavy rain" -> displayGif.setImageResource(R.drawable.icon_10d)
                                "extreme rain" -> displayGif.setImageResource(R.drawable.icon_10d)
                                "freezing rain" -> displayGif.setImageResource(R.drawable.icon_13d)
                                "light intensity shower rain" -> displayGif.setImageResource(R.drawable.icon_09d)
                                "shower rain" -> displayGif.setImageResource(R.drawable.icon_09d)
                                "heavy intensity shower rain" -> displayGif.setImageResource(R.drawable.icon_09d)
                                "ragged shower rain" -> displayGif.setImageResource(R.drawable.icon_09d)
                            }
                        }
                        "Snow" -> displayGif.setImageResource(R.drawable.icon_13d)
                        "Atmosphere" -> displayGif.setImageResource(R.drawable.icon_50d)
                        "Clear" -> displayGif.setImageResource(R.drawable.icon_01d)
                        "Clouds" -> when (weatherDescription) {
                            "few clouds" -> displayGif.setImageResource(R.drawable.icon_02d)
                            "scattered clouds" -> displayGif.setImageResource(R.drawable.icon_03d)
                            "broken clouds" -> displayGif.setImageResource(R.drawable.icon_04d)
                            "overcast clouds" -> displayGif.setImageResource(R.drawable.icon_04d)
                        }
                    }
                } catch (exception : Exception) {
                    // Log an error message if the city name is incorrect or not found
                    Log.d("Error", "Error while finding city, wrong city name inserted!")
                    lat = 51.107883
                    lon = 17.038538
                    city = "Wroclaw"
                    val message = "The specified city {$input.text.toString()} was not found in the database. The base city of Wrocław has been selected instead."
                    showWrongInputPopup(this, message)
                }

                // Retrieve air quality information
                getAirQualityInfo()
            }
            builder.show()
        }

        // Set click listener for the share button
        val shareButton = findViewById<ImageButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            openShareDialog()
        }

        // Update weather for Wrocław on the start up
        getWeather(51.107883, 17.038538)
    }

    /**
     * Opens a share dialog for sharing an image.
     * Suppresses deprecation warnings related to the usage of certain APIs.
     */
    @Suppress("DEPRECATION")
    private fun openShareDialog() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND

        // Fetch the image from the specified URL
        val url = URL(imgShareUrl)
        val connection = url.openConnection()
        connection.connect()

        val inputStream = connection.getInputStream()
        val bufferedInputStream = BufferedInputStream(inputStream)
        val b = BitmapFactory.decodeStream(bufferedInputStream)

        // Set the share intent type to "image/png"
        shareIntent.type = "image/png"

        // Compress the bitmap image
        b.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())

        // Insert the compressed image into MediaStore to get the image URI
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, b, "Title", null)
        val imageUri = Uri.parse(path)

        // Set the image URI as an extra in the share intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        // Start the share dialog with the share intent and allow the user to select an app for sharing
        startActivity(Intent.createChooser(shareIntent, "Select"))
    }

    /**
     * Checks if the necessary Google services are available and up-to-date.
     *
     * @return true if Google Play services are available and up-to-date, false otherwise.
     */
    private fun checkIfServicesAreOK(): Boolean {
        Log.d("SERVICE", "isServicesOK: checking google services version")

        // Check the availability of Google Play services
        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("SERVICE", "isServicesOK: Google Play is working correctly!")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // An error occured but we can resolve it
            Log.d("SERVICE", "isServicesOK: an error occured during contacting with Google Play")

            // Display an error dialog to resolve the issue
            GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001)
        } else {
            // Google Play services are not available, show a toast message
            Toast.makeText(this, "You can't make map  at the moment", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    /**
     * Calculates the zoom level for the map based on the given radius.
     *
     * @param radius The radius value in meters.
     * @return The zoom level as a float value. If the radius is less than 0, a default zoom level of 15.0 is returned.
     */
    private fun getZoomLevel(radius: Double): Float {
        return if (radius < 0) {
            // If the radius is less than 0, return a default zoom level of 15.0
            15f
        } else {
            // Convert the radius to a float and return it as the zoom level
            radius.toFloat()
        }
    }

    /**
     * Retrieves weather information from the specified latitude and longitude coordinates.
     *
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     */
    @SuppressLint("SetTextI18n")
    private fun getWeather(lat: Double, lon: Double) {
        val weatherUrl =
            "https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m"

        // Make a network request to retrieve the weather data
        val result2: String
        val connection2 = URL(weatherUrl).openConnection() as HttpURLConnection
        try {
            connection2.connect()

            // Read the response and store it as a string
            result2 = connection2.inputStream.use {
                it.reader().use { reader ->
                    reader.readText()
                }
            }
        } finally {
            connection2.disconnect()
        }

        // Parse the JSON response
        val jsnobject2 = JSONObject(result2)
        val currentWeather = jsnobject2.getJSONObject("current_weather")
        temperature = currentWeather.getString("temperature").toDouble()

        // Update the display text view with the current temperature
        val displayTextView = findViewById<TextView>(R.id.textView1)
        displayTextView.text = "${temperature}°"

        val allDayWeather = jsnobject2.getJSONObject("hourly")

        // Retrieve and process the temperature data for the whole day
        val weatherAllDay = allDayWeather.getJSONArray("temperature_2m")
        val weatherAllDayArray = Array(weatherAllDay.length()) { 0.0 }
        for (i in 0 until weatherAllDay.length()) {
            weatherAllDayArray[i] = weatherAllDay.optDouble(i)
        }

        // Update the display text view with the highest and lowest temperatures
        val displayTextView2 = findViewById<TextView>(R.id.textView2)
        displayTextView2.text = "H:${weatherAllDayArray.max()}° L:${weatherAllDayArray.min()}°"

        // Retrieve and process the humidity data for the whole day
        val displayTextView3 = findViewById<TextView>(R.id.textView3)
        val humidityAllDay = allDayWeather.getJSONArray("relativehumidity_2m")
        val humidityAllDayArray = Array(humidityAllDay.length()) { 0.0 }
        for (i in 0 until humidityAllDay.length()) {
            humidityAllDayArray[i] = humidityAllDay.optDouble(i)
        }

        // Calculate and display the "feels like" temperature
        displayTextView3.text = "Feels like: ${
            getFeelsLikeWeather(
                temperature,
                currentWeather.getString("windspeed").toDouble()
            )
        }°"
    }

    /**
     * Calculates the "feels like" temperature based on the apparent temperature and wind speed.
     *
     * @param apparentTemperature The apparent temperature in Celsius.
     * @param windSpeed The wind speed in km/h.
     * @return The calculated "feels like" temperature in Celsius.
     */
    private fun getFeelsLikeWeather(apparentTemperature: Double, windSpeed: Double): Double {
        // Formula to calculate feels like temperature:
        // feelsLikeTemp = 0.08 * (3.71 * sqrt(V) + 5.81 - 0.25 * V) * (T - 10) + 10.45 - 0.28 * V + 0.0203 * V * (T - 10)
        // T = Temperature (in Celsius)
        // V = windSpeed (in km/h)
        val result =
            0.08 * (3.71 * kotlin.math.sqrt(windSpeed) + 5.81 - 0.25 * windSpeed) * (apparentTemperature - 10) + 10.45 - 0.28 * windSpeed + 0.0203 * windSpeed * (apparentTemperature - 10)

        // Format the result to one decimal place and convert it to a Double
        return String.format("%.1f", result).toDouble()
    }

    /**
     * Refreshes the map based on the specified city name.
     *
     * @param cityName The name of the city to search for. If empty, defaults to "Wrocław".
     */
    private fun refreshMap(cityName: String) {
        var cityToSearch = cityName
        if (cityName.isEmpty()) {
            cityToSearch = "Wrocław"
        }

        // Retrieve the location information for the specified city
        val cityLocationUrl =
            "https://api.geoapify.com/v1/geocode/search?text=${cityToSearch}&lang=en&limit=10&type=city&apiKey=86c8140ad1074ab0ad60515b1930a481"
        val result: String
        val connection = URL(cityLocationUrl).openConnection() as HttpURLConnection
        try {
            connection.connect()
            result = connection.inputStream.use {
                it.reader().use { reader ->
                    reader.readText()
                }
            }
        } finally {
            connection.disconnect()
        }

        val jsnobject = JSONObject(result)
        try {
            // Extract the longitude and latitude coordinates from the API response
            val features = jsnobject.getJSONArray("features")
            val feature = features.getJSONObject(0)
            val properties = feature.getJSONObject("properties")
            this.lon = properties.getString("lon").toDouble()
            this.lat = properties.getString("lat").toDouble()
        } catch (e: Exception) {
            // If there are problems with getting location set back position to Wrocław
            lat = 51.107883
            lon = 17.038538
            city = "Wroclaw"
            val message = "City {$cityName} location not found in the database! The base city of Wrocław has been selected instead."
            showWrongInputPopup(this, message)
        }

        // Update the weather information and map display
        getWeather(lat, lon)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.currentPosition = LatLng(lat, lon)

        // Update the display text view with the current city
        val concatenatedString = StringBuilder()
        for (value in city) {
            concatenatedString.append(value)
            if (concatenatedString.length >= 10) {
                break
            }
        }
        val cityDisplay = concatenatedString.toString()
        val displayTextView4 = findViewById<TextView>(R.id.textView4)
        displayTextView4.text = cityDisplay
    }

    /**
     * Retrieves and updates the air quality information for the specified city.
     * The air quality information includes the AQI (Air Quality Index) and displays it on the map.
     * Additionally, it retrieves and sets the URL for sharing the air quality snapshot image.
     */
    @SuppressLint("SimpleDateFormat")
    private fun getAirQualityInfo() {
        val urlAqiIndex =
            "https://api.waqi.info/feed/${city}/?token="
        val result: String
        val connection = URL(urlAqiIndex).openConnection() as HttpURLConnection
        try {
            connection.connect()
            result = connection.inputStream.use {
                it.reader().use { reader ->
                    reader.readText()
                }
            }
        } finally {
            connection.disconnect()
        }
        try {
            val jsnobject = JSONObject(result)
            val data = jsnobject.getJSONObject("data")
            val aqi = data.getInt("aqi")
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyyMMdd-HH")
            val current = formatter.format(time)
            imgShareUrl =
                data.getJSONObject("city").getString("url") + "/" + current + "/air-quality.png"
            imgShareUrl = imgShareUrl.replace("city", "snapshot")

            // Create a marker with the AQI value and set its icon based on the AQI level
            val marker = MarkerOptions().position(LatLng(lat, lon)).title("$aqi")
            val urlW = "https://aqicn.org/mapicon/${aqi}.65.png"
            val url = URL(urlW)
            val connection2 = url.openConnection()
            connection2.connect()
            val inputStream = connection2.getInputStream()
            val bufferedInputStream = BufferedInputStream(inputStream)
            val b = BitmapFactory.decodeStream(bufferedInputStream)
            b.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
            marker.icon(BitmapDescriptorFactory.fromBitmap((b)))
            this.googleMaps.addMarker(marker)
        } catch(exception: Exception) {
            Log.d("Error", "Error while loading data for searched city!")
            lat = 51.107883
            lon = 17.038538
            val message = "Unable to download up-to-date air quality data for the city of {$city}."
            city = "Wroclaw"
            showWrongInputPopup(this, message)
        }
    }

    /**
     * Refreshes the map tiles by adding a tile overlay for displaying air quality information.
     * The tile overlay uses a custom tile provider that retrieves tiles from the AQICN (Air Quality Index) tile server.
     * The tiles are displayed based on the specified zoom level and coordinates (x, y).
     * Only tiles within the zoom range of 12 to 16 (inclusive) are considered valid.
     * The tile provider generates tile URLs based on the zoom level, x-coordinate, and y-coordinate.
     */
    private fun refreshTiles(){
        val tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {

                val url = "https://tiles.aqicn.org/tiles/usepa-pm25/${zoom}/${x}/${y}.png"
                return if (!checkTileExists(zoom)) {
                    null
                } else try {
                    URL(url)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
            }

            private fun checkTileExists(zoom: Int): Boolean {
                val minZoom = 12
                val maxZoom = 16
                return zoom in minZoom..maxZoom
            }
        }

        // Add the tile overlay to the Google Map using the tile provider
        googleMaps.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(tileProvider)
        )
    }

    /**
     * Callback method invoked when the map is ready to be used.
     * Initializes the map settings and adds necessary event listeners.
     *
     * @param googleMap The GoogleMap instance representing the initialized map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener {
            refreshTiles()
        }

        checkIfServicesAreOK()
        googleMaps = googleMap

        googleMaps.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentPosition,
                getZoomLevel(13.0)
            )
        )

        refreshTiles()
        googleMaps.setOnMapClickListener {
            refreshTiles()
        }
    }
}

