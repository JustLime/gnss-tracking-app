package com.example.gnsstrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import com.example.gnsstrackingapp.views.MapView
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.RED,
            )
        )

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContent {
            GNSSTrackingAppTheme {
                OsmMap()
            }
        }
    }
}

//@Composable
//fun TopBar() {
//    Box(
//        modifier = Modifier
//            .height(60.dp)
//            .fillMaxWidth()
//            .background(Color.Magenta)
//    ) {
//        Text(text = "Hello", color = Color.White)
//    }
//}

@Composable
fun OsmMap() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(6))
        ) {
            MapView()
        }
    }
}
