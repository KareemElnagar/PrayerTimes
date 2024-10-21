package com.kareem.prayertimes.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.kareem.prayertimes.R
import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.databinding.FragmentHomeBinding
import com.kareem.prayertimes.presentation.util.Resource
import com.kareem.prayertimes.presentation.util.formatTimeTo12Hour
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val currentDate = LocalDate.now()
    private var currentDay = currentDate.dayOfMonth
    private var currentMonth = currentDate.month
    private var currentYear = currentDate.year
    private lateinit var prayersTimes: PrayerTimeRes
    private lateinit var prayerTimeList: List<String>
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var firstTime = 0
    private var index = 0
    private lateinit var locationHelper: LocationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationHelper = LocationHelper(requireActivity() as AppCompatActivity)
        getUserLocation()
        calendarHandler()
        initObservation()

        binding.QiblaBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_qiblaFragment)
        }
    }

    private fun getUserLocation() {
        locationHelper.fetchLocation(object : LocationHelper.OnLocationFetchedListener {
            override fun onLocationFetched(location: Location?, address: String?) {
                viewModel.setLat(location!!.latitude)
                viewModel.setLong(location.longitude)
                binding.textLocation.text = address

            }

            @SuppressLint("SetTextI18n")
            override fun onError(error: String?) {
                binding.textLocation.text = "$error please restart the app"
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initObservation() {

        with(viewModel) {
            getPrayerTimeState.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {

                            val prayTime = it.data[currentDay - 1].timings
                            prayerTimeList = listOf(
                                prayTime.Fajr.formatTimeTo12Hour(),
                                prayTime.Sunrise.formatTimeTo12Hour(),
                                prayTime.Dhuhr.formatTimeTo12Hour(),
                                prayTime.Asr.formatTimeTo12Hour(),
                                prayTime.Maghrib.formatTimeTo12Hour(),
                                prayTime.Isha.formatTimeTo12Hour()
                            )
                            viewModel.setPrayerTimes(prayerTimeList)
                            updateUI(it)
                            viewModel.deleteAll()
                            viewModel.saveAllPrayersTimes(it)

                        }
                    }

                    is Resource.Error -> {
                        updateUI(prayersTimes)
                    }

                    is Resource.Loading -> {}

                }
            }
                viewModel.getAllPrayersTimes().observe(viewLifecycleOwner) { response ->
                    if (response != null)
                        prayersTimes = response
                }

            viewModel.prayerTimes.observe(viewLifecycleOwner) {
                if (firstTime == 0) {
                    firstTime++
                    //get next prayer time
                    val time = nextPrayer(it)
                    saveCountdownEndTime(requireContext(), time)

                    startCountdown(time / 1000)
                }

            }

            viewModel.lat.observe(viewLifecycleOwner) { lat ->
                viewModel.long.observe(viewLifecycleOwner) { long ->
                    viewModel.getPrayerTimes(currentYear, currentMonth.value, lat, long, 1)
                    latitude = lat
                    longitude = long

                }

            }

            // to handle count down
            viewModel.index.observe(viewLifecycleOwner) { index ->

                when (index) {
                    1 -> {
                        binding.nextPrayer.text = "Fajr"
                    }

                    2 -> {
                        binding.nextPrayer.text = "Sunrise"
                    }

                    3 -> {
                        binding.nextPrayer.text = "Duhr"
                    }

                    4 -> {
                        binding.nextPrayer.text = "Asr"
                    }

                    5 -> {
                        binding.nextPrayer.text = "Maghrib"
                    }

                    6 -> {
                        binding.nextPrayer.text = "Isha"
                    }
                }
            }
        }
    }


    @SuppressLint("DefaultLocale")
    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secondsRemaining = seconds % 60
        return String.format("%02d hr %02d min %02d sec", hours, minutes, secondsRemaining)
    }

    private fun convertTimeToMilliseconds(timeString: String): Long {
        return try {
            val format = SimpleDateFormat("hh:mm a", Locale.US)
            val date = format.parse(timeString)
            date?.time ?: -1
        } catch (e: Exception) {
            -1
        }
    }

    @SuppressLint("SetTextI18n")
    fun startCountdown(totalSeconds: Long) {
        lifecycleScope.launch(Dispatchers.Main) {
            for (seconds in totalSeconds downTo 0) {
                val formattedTime = formatTime(seconds)
                binding.tvCountdown.text = "          Time Left \n $formattedTime"
                delay(1000)
            }
            binding.tvCountdown.text = "Countdown finished for today will start at 12 am"
        }
    }

    internal fun saveCountdownEndTime(context: Context, endTimeMillis: Long) {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putLong(COUNTDOWN_TIME_KEY, endTimeMillis).apply()
    }

    private fun nextPrayer(times: List<String>): Long {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)
        val newTime = convertTimeToMilliseconds(formattedTime)
        for (i in times) {
            val x = convertTimeToMilliseconds(i)

            index++

            if (newTime < x) {
                val result = x - newTime
                viewModel.setIndex(index)
                return result
            }
        }
        return 0
    }


    private fun updateUI(data: PrayerTimeRes) {
        with(binding) {
            val prayTime = data.data[currentDay - 1].timings
            tvFajrTime.text = prayTime.Fajr.formatTimeTo12Hour()
            tvDhuhrTime.text = prayTime.Dhuhr.formatTimeTo12Hour()
            tvAsrTime.text = prayTime.Asr.formatTimeTo12Hour()
            tvMaghribTime.text = prayTime.Maghrib.formatTimeTo12Hour()
            tvIshaTime.text = prayTime.Isha.formatTimeTo12Hour()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun incrementLogic() {
        if (currentDay < currentMonth.maxLength()) {
            currentDay += 1
            binding.textDate.text = "$currentMonth  $currentDay $currentYear"
            viewModel.setDay(currentDay)
        } else {

            if (currentMonth == Month.DECEMBER && currentDay == currentMonth.maxLength()) {
                currentYear += 1
                viewModel.setYear(currentYear)
            }

            currentMonth += 1
            viewModel.setMonth(currentMonth.value)
            currentDay = 1
            viewModel.setDay(currentDay)
            binding.textDate.text = "$currentMonth  $currentDay $currentYear"

        }
    }

    @SuppressLint("SetTextI18n")
    private fun decrementLogic() {
        if (currentDay > 1) {
            currentDay -= 1
            binding.textDate.text = "$currentMonth $currentDay $currentYear"
            viewModel.setDay(currentDay)

        } else {

            if (currentMonth == Month.JANUARY && currentDay == 1) {
                currentYear -= 1
                viewModel.setYear(currentYear)
            }
            currentMonth -= 1
            viewModel.setMonth(currentMonth.value)
            currentDay = currentMonth.maxLength()
            viewModel.setDay(currentDay)
            binding.textDate.text = "$currentMonth  $currentDay $currentYear"

        }
    }

    @SuppressLint("SetTextI18n")
    fun calendarHandler() {
        binding.textDate.text = "$currentMonth $currentDay $currentYear"
        binding.buttonIncrement.setOnClickListener {
            viewModel.getPrayerTimes(currentYear, currentMonth.value, latitude, longitude, 1)
            incrementLogic()
        }
        binding.buttonDecrement.setOnClickListener {
            viewModel.getPrayerTimes(currentYear, currentMonth.value, latitude, longitude, 1)
            decrementLogic()

        }
    }

    companion object {
        const val COUNTDOWN_TIME_KEY = "countdown_end_time"

    }

}