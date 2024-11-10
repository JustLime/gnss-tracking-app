package com.example.gnsstrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.dark(
//                android.graphics.Color.RED,
//            )
//        )

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContent {
            GNSSTrackingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}


