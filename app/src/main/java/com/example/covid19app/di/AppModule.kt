package com.example.india.covid19app.di

import com.google.gson.Gson
import org.koin.dsl.module

val appModule = module {

    single {
        Gson()
    }

}