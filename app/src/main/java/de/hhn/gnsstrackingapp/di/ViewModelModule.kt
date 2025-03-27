package de.hhn.gnsstrackingapp.di

import de.hhn.gnsstrackingapp.ui.screens.map.LocationViewModel
import de.hhn.gnsstrackingapp.ui.screens.map.MapViewModel
import de.hhn.gnsstrackingapp.ui.screens.settings.SettingsViewModel
import de.hhn.gnsstrackingapp.ui.screens.statistics.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { StatisticsViewModel() }

    viewModel { MapViewModel() }
    viewModel { LocationViewModel(get()) }
    viewModel { SettingsViewModel() }
}
