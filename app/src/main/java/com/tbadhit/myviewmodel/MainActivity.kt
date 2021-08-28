package com.tbadhit.myviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.tbadhit.myviewmodel.databinding.ActivityMainBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

// ViewModel menjadi solusi untuk menjaga data walaupun lifecycle berubah (semisal di rotate)

// Codelab :
// add code "build.gradle module " (1) "LoopJ, RecyclerView dan Architecture Component"
// update code "activity_main.xml"
// create new layout "weather_items"
// create new kotlin class "WeatherItems" + add code
// create new class "WeatherAdapter" (create adapter)
// add code "WeatherAdapter" (1)
// add code "MainActivity" (1) (2)
// add permission INTERNET & UsesClearTraffic (AndroidManifest)

// Codelab (add ViewModel) :
// create new kotlin class "MainViewModel"
// add code "MainViewModel" (1)
// move method setWeather() in "MainActivity" to "MainViewModel"
// add code "MainActivtiy" (3) and replace setWeather function call
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // (1)
    private lateinit var adapter: WeatherAdapter

    // (3)
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // (1)
        adapter = WeatherAdapter()
        adapter.notifyDataSetChanged()

        // (1)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // (3)
        // ViewModelProviders =  untuk menyambungkan kelas MainViewModel dengan MainActivity
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        // (2)
        binding.btnCity.setOnClickListener {
            val city = binding.editCity.text.toString()
            if (city.isEmpty()) return@setOnClickListener
            showLoading(true)

            // (3)
            mainViewModel.setWeather(city)

            // before use viewmodel
            //setWeather(city)
        }

        // (3)
        // untuk mendapatkan value dari livedata yang ada pada kelas MainViewModel
        mainViewModel.getWeathers().observe(this, {weatherItems ->
            if (weatherItems != null) {
                adapter.setData(weatherItems)
                showLoading(false)
            }
        })
    }

    // (2)
    // Move to MainViewModel
//    fun setWeather(cities: String) {
//        val listItems = ArrayList<WeatherItems>()
//
//        val apiKey = "23373b345890d776e2d25e7e052061c1"
//        val url = "https://api.openweathermap.org/data/2.5/group?id=${cities}&units=metric&appid=${apiKey}"
//
//        val client = AsyncHttpClient()
//        client.get(url, object : AsyncHttpResponseHandler() {
//            override fun onSuccess(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray
//            ) {
//                // Parsing JSON
//                try {
//                    val result = String(responseBody)
//                    val responseObject = JSONObject(result)
//                    val list = responseObject.getJSONArray("list")
//
//                    for (i in 0 until list.length()) {
//                        val weather = list.getJSONObject(i)
//                        val weatherItems = WeatherItems()
//                        weatherItems.id = weather.getInt("id")
//                        weatherItems.name = weather.getString("name")
//                        weatherItems.currentWeather = weather.getJSONArray("weather").getJSONObject(0).getString("main")
//                        weatherItems.description = weather.getJSONArray("weather").getJSONObject(0).getString("description")
//                        val tempInKelvin = weather.getJSONObject("main").getDouble("temp")
//                        val tempInCelsius = tempInKelvin - 273
//                        weatherItems.temperature = DecimalFormat("##.##").format(tempInCelsius)
//                        listItems.add(weatherItems)
//                    }
//
//                    //set data ke adapter
//                    adapter.setData(listItems)
//                    showLoading(false)
//                } catch (e: Exception){
//                    Log.d("Exception", e.message.toString())
//                }
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray?,
//                error: Throwable?
//            ) {
//                Log.d("onFailure", error?.message.toString())
//            }
//
//        })
//    }

    // (2)
    // untuk progress bar
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}