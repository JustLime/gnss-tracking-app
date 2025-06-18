package de.hhn.externalgnsstrackingapp.di

import de.hhn.externalgnsstrackingapp.ui.screens.map.LocationViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.map.MapViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.settings.SettingsViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.statistics.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { StatisticsViewModel() }

    viewModel { MapViewModel() }
    viewModel { LocationViewModel(get()) }
    viewModel { SettingsViewModel() }
}
