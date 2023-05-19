package com.example.timetable_application

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.common.util.CollectionUtils.listOf

sealed class BottomMenuScreen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    //导航元素清单
    object MainCompose : BottomMenuScreen("LoginBIT",
        Icons.Default.Home, "LoginBIT")
    object OtherCompose : BottomMenuScreen("Other",
        Icons.Default.Menu, "Other")
    object TimetableScreen : BottomMenuScreen("Timetable",
        Icons.Default.Home, "Timetable")
}

@Composable
fun BottomMenu(navController: NavController) {
    val menuItems = listOf<BottomMenuScreen>(
        BottomMenuScreen.MainCompose,
        BottomMenuScreen.OtherCompose,
        BottomMenuScreen.TimetableScreen
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.height(7.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            //创建底部导航元素
            NavigationBarItem(
                label = { Text(text = it.route) },
                alwaysShowLabel = true,
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route)
                },
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) }
            )
        }
    }
}