package com.example.gnsstrackingapp.ui.screens.settings

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gnsstrackingapp.network.MapsDownloader
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    val isChecked = remember { mutableStateOf(false) }
    val typography = Typography()

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = typography.headlineLarge.fontSize
        )
//
//        SettingsListItem(
//            "RTCM Mode",
//            "Uses RTCM to get more accurate position",
//            icon = Icons.Outlined.Info,
//            contentDescription = "RTCM Mode",
//        ) {
//            Switch(
//                checked = isChecked.value,
//                onCheckedChange = { isChecked.value = !isChecked.value },
//                modifier = Modifier.scale(0.8f)
//            )
//        }

        SettingsListItem(
            "Theme",
            "Switch between light and dark theme",
            icon = Icons.Outlined.Edit,
            contentDescription = "RTCM Mode",
        )

        SettingsListItem(
            "Refreshing rate",
            "How often to refresh the map",
            icon = Icons.Outlined.Refresh,
            contentDescription = "RTCM Mode",
        )

        DownloadMapsButton(
            title = "Download maps",
            description = "Get the latest offline maps for Germany",
            icon = Icons.Outlined.ArrowDropDown,
            navController = navController,
            context = LocalContext.current
        )
    }
}

@Composable
fun SettingsListItem(
    title: String,
    description: String,
    icon: ImageVector,
    contentDescription: String? = null,
    content: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit = {},
) {
    val typography = Typography()

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(imageVector = icon, contentDescription = contentDescription)
                Column {
                    Text(
                        text = title,
                        fontSize = typography.bodyLarge.fontSize,
                    )
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        lineHeight = 16.sp,
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                }
            }
            Column(content = content)
        }
    }
}

@Composable
fun DownloadMapsButton(
    title: String,
    description: String,
    icon: ImageVector,
    contentDescription: String? = null,
    content: @Composable ColumnScope.() -> Unit = {},
    navController: NavController,
    context: Context
) {
    val typography = Typography()
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                // Show a toast that the download has started
                Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()

                try {
                    MapsDownloader(context).saveFileToPublicDirectory(
                        "https://download.geofabrik.de/europe/germany-latest.osm.bz2",
                        "germany-latest.osm.bz2"
                    )
//                    Log.i("DownloadMapsButton", "Download finished successfully")
//                    // Show a toast on successful download
//                    Toast.makeText(context, "Download finished successfully", Toast.LENGTH_SHORT)
//                        .show()
                } catch (e: Exception) {
                    Log.e("DownloadMapsButton", "Download failed: ${e.message}")
                    // Show a toast on failure
                    Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(imageVector = icon, contentDescription = contentDescription)
                Column {
                    Text(
                        text = title,
                        fontSize = typography.bodyLarge.fontSize,
                    )
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        lineHeight = 16.sp,
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                }
            }
            Column(content = content)
        }
    }
}