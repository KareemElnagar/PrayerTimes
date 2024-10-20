package com.kareem.prayertimes.domain.usecase

import com.kareem.prayertimes.data.model.qibla.QiblaResponse
import com.kareem.prayertimes.domain.repo.repo
import retrofit2.Response
import javax.inject.Inject

class GetQiblaDirectionUseCase @Inject constructor(private val repo: repo) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Response<QiblaResponse> = repo.getQiblaDirection(latitude, longitude)


}