package com.example.covid19app.di

import com.example.india.covid19app.ui.helpline.HelplineViewModel
import com.example.india.covid19app.ui.home.HomeViewModel
import com.example.india.covid19app.ui.state.StateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { StateViewModel(get()) }
    viewModel { HelplineViewModel(get()) }
}