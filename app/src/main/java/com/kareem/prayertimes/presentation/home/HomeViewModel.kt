package com.kareem.prayertimes.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareem.prayertimes.data.model.PrayerTimeRes
import com.kareem.prayertimes.domain.usecase.PrayerUseCases
import com.kareem.prayertimes.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prayerUseCases: PrayerUseCases
) : ViewModel() {

    private val _getPrayerTimeState = MutableLiveData<Resource<PrayerTimeRes>>()
    val getPrayerTimeState: LiveData<Resource<PrayerTimeRes>> = _getPrayerTimeState

    //latitude
    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double> = _lat


    //Longitude
    private val _long = MutableLiveData<Double>()
    val long: LiveData<Double> = _long


    private val _day = MutableLiveData<Int>()


    private val _month = MutableLiveData<Int>()


    private val _year = MutableLiveData<Int>()

    private val _prayerTimes = MutableLiveData<List<String>>()
    var prayerTimes: LiveData<List<String>> = _prayerTimes


    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index


    fun setLat(latitude: Double) {
        _lat.value = latitude
    }

    fun setLong(longitude: Double) {
        _long.value = longitude
    }

    fun setDay(day: Int) {
        _day.postValue(day)
    }

    fun setMonth(month: Int) {
        _month.postValue(month)
    }

    fun setYear(year: Int) {
        _year.postValue(year)
    }

    fun setPrayerTimes(prayerTimes: List<String>) {
        _prayerTimes.postValue(prayerTimes)
    }

    fun setIndex(index: Int) {
        _index.postValue(index)
    }


    fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        method: Int,
    ) {
        viewModelScope.launch {

            try {

                val response =
                    prayerUseCases.getPrayerTimes(latitude,longitude,month,year,method)
                _getPrayerTimeState.value = getPrayerTimeHandler(response)

            } catch (t: Throwable) {

                _getPrayerTimeState.postValue(Resource.Error(t.message, null))

            }


        }

    }

    private fun getPrayerTimeHandler(response: Response<PrayerTimeRes>): Resource<PrayerTimeRes> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }


    // cashing
    fun saveAllPrayersTimes(response: PrayerTimeRes) =
        viewModelScope.launch {
            prayerUseCases.savePrayerTimesUseCase(response)

        }


      fun getAllPrayersTimes(): LiveData<PrayerTimeRes> {

        return prayerUseCases.getAllPrayersTimesUseCase()
    }

    fun deleteAll() {
        viewModelScope.launch {
            prayerUseCases.deletePrayerTimesUseCase()
        }
    }

}