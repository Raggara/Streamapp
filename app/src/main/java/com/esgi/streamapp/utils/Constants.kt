package com.esgi.streamapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import org.jetbrains.anko.doAsync
import java.net.HttpURLConnection
import java.net.URL

object Constants {
    // URL de l'API
    const val URL_SERV: String = "http://91.121.135.19:4040/movies"
    const val URL_STREAM: String = "http://91.121.135.19:8020/"
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

        //return when(Status){
        //    200 -> true
        //    else -> false
        //}
        return true
    }

}