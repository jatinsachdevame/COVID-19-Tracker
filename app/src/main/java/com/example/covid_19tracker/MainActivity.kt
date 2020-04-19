package com.example.covid_19tracker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header, listView,false))
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            fetchResults()
        } else {
            Toast.makeText(this,"Check your Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchResults() {
        GlobalScope.launch {
            val response : Response = withContext(Dispatchers.IO) { Client.api.execute() }
            if (response.isSuccessful) {
                val data = Gson().fromJson(response.body?.string(),com.example.covid_19tracker.Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(1, data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateAdapter = StateAdapter(subList)
        listView.adapter = stateAdapter
    }

    private fun bindCombinedData(data: StatewiseItem) {
        val lastUpdatedTime: String? = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"
        confirmedTv.text = data.confirmed
        recoveredTv.text = data.recovered
        activeTv.text = data.active
        deceasedTv.text = data.deaths
    }

    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time-past.time)

        return when {
            seconds<60 -> {
                "Few seconds ago"
            }
            minutes<60 -> {
                "$minutes minutes ago"
            }
            hours<24 -> {
                "$hours hour ${minutes%60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(past).toString()
            }
        }
    }
}