package com.esgi.streamapp.utils.models

import java.io.Serializable

enum class TypeError{Network, API, Player}
data class ErrorHelper(val errorType: TypeError, val errorStatus: Int, val errorMessage: String) : Serializable