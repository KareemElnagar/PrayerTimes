package com.kareem.prayertimes.presentation.util

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.kareem.prayertimes.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun String.formatTimeTo12Hour(): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return try {
        val date = inputFormat.parse(this)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        this // Return the original string if parsing fails
    }

    }

fun ImageView.compassHandler(azimuth: Float, qiblaDirection : Double ){

    val azimuthNormalized = (azimuth + 360) % 360 // Convert to a positive value
    val angleToQibla = (azimuthNormalized - qiblaDirection + 360) % 360
    if (angleToQibla < 10f || angleToQibla > 350f) {
        this.setImageResource(R.drawable.baseline_explore_24)
    } else {
        this.setImageResource(R.drawable.baseline_explore_25)
    }
}