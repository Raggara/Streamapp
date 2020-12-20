package com.esgi.streamapp.utils.models

import java.io.Serializable

enum class TypeError{Network, API, Player}
data class ErrorHelper(var errorType: TypeError, var errorStatus: Int, var errorMessage: String) : Serializable