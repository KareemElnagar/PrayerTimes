package com.kareem.prayertimes.presentation.qibla

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareem.prayertimes.data.model.qibla.QiblaResponse
import com.kareem.prayertimes.domain.usecase.PrayerUseCases
import com.kareem.prayertimes.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(
  private val prayersUseCases: PrayerUseCases
) : ViewModel() {


    //livedata objects
    private val _qiblaDirection = MutableLiveData<Resource<QiblaResponse>>()
    val qiblaDirection: LiveData<Resource<QiblaResponse>> = _qiblaDirection

    fun getDirection(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val response = prayersUseCases.getQiblaDirectionUseCase(lat, long)
                _qiblaDirection.postValue(getDirectionHandler(response))
            } catch (t: Throwable) {
                _qiblaDirection.postValue(Resource.Error(t.message, null))
            }
        }
    }


    private fun getDirectionHandler(response: Response<QiblaResponse>): Resource<QiblaResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }
}