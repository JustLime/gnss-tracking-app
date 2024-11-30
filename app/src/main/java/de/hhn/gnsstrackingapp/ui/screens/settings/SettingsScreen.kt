package de.hhn.gnsstrackingapp.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
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
