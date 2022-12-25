package weather.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsBinding
import weather.model.Weather

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

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
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)

        if (weather != null) {
            binding.cityName.text = weather.city.city

            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.lat.toString(),
                weather.city.lon.toString()
            )

            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}