package weather.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsBinding
import weather.model.Weather
import weather.model.WeatherDTO

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!


    private lateinit var weatherBundle : Weather

    private val onLoaderListener : WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weather: WeatherDTO) {
                displayWeather(weather)
            }
            override fun onFailed(throwable: Throwable) {
                //
            }
        }

    companion object {
        const val BUNDLE_EXTRA = "Weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?: Weather()

        with(binding){
            mainView.visibility = View.GONE
            ibSync.visibility = View.VISIBLE
        }
        val loader = WeatherLoader(onLoaderListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun displayWeather( weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            ibSync.visibility = View.GONE

            weatherBundle.city.also {
                cityName.text = it.city
                cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    it.lat.toString(),
                    it.lon.toString()
                )
            }
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }
}