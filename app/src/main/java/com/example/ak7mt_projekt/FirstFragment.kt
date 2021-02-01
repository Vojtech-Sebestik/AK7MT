package com.example.ak7mt_projekt

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class FirstFragment : Fragment() {
    var CITY : String? = "praha,cz"
    val API : String? = "67272c7562c2a0a2e834c80b79707704"
    val SHARED_PREFS = "sharedPrefs"
    val TEXT = "text"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle? ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_first, container, false)


        //entryData.setText(CITY)

        view.myButton.setOnClickListener { view: View? ->

            CITY = entryData.text.toString()
            weatherTask().execute()
        }
        return view
    }



    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            //errortext.visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                //loadData()
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&lang=cz&units=metric&appid=$API").readText()
            } catch (e: Exception) {

                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                // získání dat
                var jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val coord = jsonObj.getJSONObject("coord")
                val latitude = coord.getString("lat") + " °N"
                val longitude = coord.getString("lon") + " °E"
                val updateAtText = "Aktualizováno: " + SimpleDateFormat("dd/MM/yyy HH:mm", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min: " + main.getString("temp_min") + " °C"
                val tempMax = "Max: " + main.getString("temp_max") + " °C"
                val pressure = main.getString("pressure") + " hPa"
                val humidity = main.getString("humidity") + " %"
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed") + " km/h"
                val city = jsonObj.getString("name")
                val country = sys.getString("country")
                val icon = weather.getString("icon")

                // prirazeni dat
                latitudeData.text = latitude
                longitudeData.text = longitude
                countryData.text = country
                minTempData.text = tempMin
                maxTempData.text = tempMax
                pressureData.text = pressure
                windspeedData.text = windSpeed
                cityData.text = city
                humidityData.text = humidity
                temperatureData.text = temp
                sunsetData.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunset))
                sunriseData.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunrise))
                // prirazeni obrazku počasé
                Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView)
                saveData()

            } catch (e: Exception) {
                myButton.text = "nefunguje"
            }
        }
    }

    fun saveData() {
        val sharedPreferences = this.activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(TEXT, entryData.text.toString())
        editor?.apply()
    }


    fun loadData(){
        val sharedPreferences =
            this.activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        CITY = (sharedPreferences?.getString(TEXT, "Kromeriz,cz"))

    }
}