package com.esgi.streamapp.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

enum class TypeError{Network, API, Player}

@Parcelize
data class ErrorHelper(var errorType: Int, var errorStatus: Int, var errorMessage: String) : Parcelable