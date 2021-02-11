package com.esgi.streamapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    // URL de l'API
    const val URL_SERV: String = "http://91.121.135.19:4040/movies"
    const val URL_STREAM: String = "http://91.121.135.19:8020/"
    const val URL_APK: String = "http://91.121.135.19:4040/apk"
    const val EXTRA_IDMOV: String = "idMovie"
    const val EXTRA_ERRTYPE: String = "errorType"
    const val EXTRA_PATHMOV: String = "pathMovie"
    const val EXTRA_NAMECATEG: String = "categoryName"
    const val ERRMSG_NET: String = "Pas de connexion internet. Essaye de recharger la page après t'être connecté."
    const val ERRMSG_API: String = "Le serveur est actuellement en maintenance. Veuillez réessayer plus tard."
    const val ERRMSG_PLAYER: String = "Une erreur s'est produite lors de la lecture. Veuillez réessayer plus tard."
    const val EXTRA_PIP: String = "pipMode"


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork  = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }

}