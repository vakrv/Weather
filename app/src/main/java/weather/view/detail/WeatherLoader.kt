package weather.view.detail

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather.BuildConfig
import com.google.gson.Gson
import weather.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(
    private val listener: WeatherLoaderListener,
    private val lat: Double,
    private val lon: Double,
    private val  WEATHER_API_KEY: String = "fed66a19-9144-4fc4-bb72-cd687dd16181"
) {
    interface WeatherLoaderListener {
        fun onLoaded(weather: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() =

        try {

            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val handler = Handler()
            Thread {

                lateinit var urlConnection: HttpsURLConnection

                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty("X-Yandex-API-Key", WEATHER_API_KEY)
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)

                    val weatherDTO : WeatherDTO = Gson().fromJson(response, WeatherDTO::class.java)

                    handler.post {
                        listener.onLoaded(weatherDTO)
                    }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()

                    handler.post {
                        listener.onFailed(e)
                    }
                } finally {
                    urlConnection.disconnect()
                }
            }.start()

        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
}

@RequiresApi(Build.VERSION_CODES.N)
private fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}

