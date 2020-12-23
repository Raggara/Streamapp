package com.esgi.streamapp.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.esgi.streamapp.utils.models.ErrorHelper
import org.jetbrains.anko.doAsync
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

object Constants {
    // URL de l'API
    const val URL_SERV: String = "http://91.121.135.19:4040/movies"
    const val URL_STREAM: String = "http://91.121.135.19:8020/"
    const val EXTRA_IDMOV: String = "idMovie"
    const val EXTRA_ERRMESS: String = "errorMessage"
    const val EXTRA_ERRSTATUS: String = "errorStatus"
    const val EXTRA_ERRTYPE: String = "errorType"
    const val EXTRA_PATHMOV: String = "pathMovie"
    const val EXTRA_NAMECATEG: String = "categoryName"
    const val ERRMSG_NET: String = "Vous n'êtes pas connecté à internet. Connectez vous à internet et rechargez la page pour accéder à l'application."
    const val ERRMSG_API: String = "Le serveur est actuellement en maintenance. Veuillez réessayer plus tard."
    const val ERRMSG_PLAYER: String = "Une erreur s'est produite lors de la lecture. Veuillez réessayer plus tard."
    const val EXTRA_PIP: String = "pipMode"

    private var Status: Int? = null

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

    fun isApiAvailable() : Boolean{
        doAsync {
            val url = URL(URL_SERV)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            Status = urlConnection.responseCode
        }
        Log.d("status", Status.toString())
        return true
    }

}