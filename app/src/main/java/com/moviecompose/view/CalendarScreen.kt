package com.moviecompose.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.endlos.Kalendar
import com.himanshoe.kalendar.endlos.model.KalendarDay
import com.himanshoe.kalendar.endlos.model.KalendarEvent
import com.moviecompose.ui.theme.Pink
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    var date by remember { mutableStateOf("") }
    var listEvent: List<KalendarEvent>? = null
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Pink
    ) {
        Kalendar(
            modifier = Modifier.background(Color.White),
            kalendarEvents = listOf(
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 6, dayOfMonth = 25),
                    eventName = "Transformer"
                ),
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 6, dayOfMonth = 25),
                    eventName = "The Flash"
                ),
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 6, dayOfMonth = 27),
                    eventName = "BatMan"
                ),
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 7, dayOfMonth = 1),
                    eventName = "Moshi Moshi"
                ),
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 7, dayOfMonth = 2),
                    eventName = "Spark Hero"
                ),
                KalendarEvent(
                    LocalDate(year = 2023, monthNumber = 7, dayOfMonth = 2),
                    eventName = "The Last Of Us"
                ),
            ),
            onCurrentDayClick = { day: KalendarDay, events: List<KalendarEvent> ->
                date = day.localDate.toString()
                listEvent = events
                println(day.localDate.toString() + events)
                coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide()
                    else sheetState.show()
                }
            },
        )
    }
    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)

            ) {

                Spacer(modifier = Modifier.height(10.dp))

                CoilImage(
                    imageModel = "https://3.bp.blogspot.com/-VVp3WvJvl84/X0Vu6EjYqDI/AAAAAAAAPjU/ZOMKiUlgfg8ok8DY8Hc-ocOvGdB0z86AgCLcBGAsYHQ/s1600/jetpack%2Bcompose%2Bicon_RGB.png",
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Selected date: $date\n$listEvent",
                    modifier = Modifier
                        .padding(20.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
            }
        }
    ) {}
}
