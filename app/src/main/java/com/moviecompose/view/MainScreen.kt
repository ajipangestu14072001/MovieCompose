package com.moviecompose.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moviecompose.R
import com.moviecompose.ui.theme.Pink
import com.moviecompose.utils.NoRippleTheme
import com.moviecompose.utils.bottomBarHeight
import com.moviecompose.utils.icon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.coil.CoilImage

@Destination(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
) {
    val sectionState = remember { mutableStateOf(HomeSection.Home) }

    val navItems = HomeSection.values()
        .toList()
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    items = navItems,
                    currentSection = sectionState.value,
                    onSectionSelected = {
                        sectionState.value = it
                    },
                )
            }) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            Crossfade(
                modifier = modifier,
                targetState = sectionState.value
            )
            { section ->
                when (section) {
                    HomeSection.Home -> HomeScreen(navigator = navigator)
                    HomeSection.Reels -> SearchScreen(navigator = navigator)
                    HomeSection.Add -> HorrorScreen(navigator = navigator)
                    HomeSection.Favorite -> CalendarScreen()
                    HomeSection.Profile -> ProfileScreen(navigator = navigator)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    items: List<HomeSection>,
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
) {
    BottomNavigation(
        modifier = Modifier.height(bottomBarHeight),
        backgroundColor = Color(0XFF393E46),
        contentColor = Color.White
    ) {
        items.forEach { section ->

            val selected = section == currentSection

            val iconRes = if (selected) section.selectedIcon else section.icon
            val inactiveColor = Color.White

            BottomNavigationItem(
                icon = {

                    if (section == HomeSection.Profile) {
                        BottomBarProfile(selected)
                    } else {
                        Icon(
                            painter = painterResource(id = iconRes),
                            modifier = Modifier.icon(),
                            tint = if(selected) inactiveColor else Color.LightGray,
                            contentDescription = ""
                        )
                    }

                },
                selected = selected,
                onClick = { onSectionSelected(section) },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
private fun BottomBarProfile(isSelected: Boolean) {
    val shape = CircleShape

    val borderModifier = if (isSelected) {
        Modifier
            .border(
                color = Color.White,
                width = 1.dp,
                shape = shape
            )
    } else Modifier

    val padding = if (isSelected) 3.dp else 0.dp

    Box(
        modifier = borderModifier
    ) {
        Box(
            modifier = Modifier
                .icon()
                .padding(padding)
                .background(color = Color.LightGray, shape = shape)
                .clip(shape)
        ) {
            CoilImage(
                imageModel = "https://www.babatpost.com/wp-content/uploads/JKT-48-haruka.jpg.webp",
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

private enum class HomeSection(
    val icon: Int,
    val selectedIcon: Int,
) {
    Home(icon = R.drawable.home, selectedIcon = R.drawable.home_selected),
    Reels(icon = R.drawable.search, selectedIcon = R.drawable.search_selected),
    Add(icon = R.drawable.ic_round_add_circle_outline_24, selectedIcon = R.drawable.ic_round_add_circle_24),
    Favorite(icon = R.drawable.ic_round_calendar_month_24, selectedIcon = R.drawable.ic_round_event_note_24),
    Profile(icon = R.drawable.ic_baseline_circle_24, selectedIcon = R.drawable.ic_baseline_circle_24)
}