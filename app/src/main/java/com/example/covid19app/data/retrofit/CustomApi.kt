package com.example.india.covid19app.data.retrofit

import com.example.india.covid19app.data.model.StateDistrictWise
import com.example.india.covid19app.data.model.Stats
import retrofit2.http.*

interface CustomApi {

    @GET("/data.json")
    suspend fun getStats(): Stats

    @GET("/v2/state_district_wise.json")
    suspend fun getDistrictStats(): List<StateDistrictWise>

}