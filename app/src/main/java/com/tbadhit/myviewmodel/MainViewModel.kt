package com.tbadhit.myviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

// (1) extend ViewModel()
/*
Dengan menambahkan turunan kelas ViewModel ke kelas MainViewModel menandakan bahwa kelas tersebut
sebagai kelas ViewModel. Segala sesuatu yang ada di kelas tersebut akan terjaga selama Activity
masih dalam keadaan aktif. Pada kelas MainViewModel, listWeathers akan selalu dipertahankan selama
kelas tersebut masih terikat dengan Activity.
 */
class MainViewModel: ViewModel() {

    // (1)
    val listWeathers = MutableLiveData<ArrayList<WeatherItems>>()

    // (1)
    fun setWeather(cities: String) {
        val listItems = ArrayList<WeatherItems>()

        val apiKey = "23373b345890d776e2d25e7e052061c1"
        val url = "https://api.openweathermap.org/data/2.5/group?id=${cities}&units=metric&appid=${apiKey}"

        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // Parsing JSON
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("list")

                    for (i in 0 until list.length()) {
                        val weather = list.getJSONObject(i)
                        val weatherItems = WeatherItems()
                        weatherItems.id = weather.getInt("id")
                        weatherItems.name = weather.getString("name")
                        weatherItems.currentWeather = weather.getJSONArray("weather").getJSONObject(0).getString("main")
                        weatherItems.description = weather.getJSONArray("weather").getJSONObject(0).getString("description")
                        val tempInKelvin = weather.getJSONObject("main").getDouble("temp")
                        val tempInCelsius = tempInKelvin - 273
                        weatherItems.temperature = DecimalFormat("##.##").format(tempInCelsius)
                        listItems.add(weatherItems)
                    }
                    listWeathers.postValue(listItems)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    // (1)
    fun getWeathers(): LiveData<ArrayList<WeatherItems>> {
        return listWeathers
    }

}