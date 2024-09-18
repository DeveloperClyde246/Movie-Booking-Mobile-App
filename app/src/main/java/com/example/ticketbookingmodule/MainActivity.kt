package com.example.ticketbookingmodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ticketbookingmodule.ui.theme.TicketBookingModuleTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateChip(date: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clickable(onClick = onClick),
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DateCinemaLocationSelectionPage(
    movieName: String,
    estimateTime: String,
    language: String,
    grade: String,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedExperience by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("") }

    // Define a smaller text style
    val smallTextStyle = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp // Adjust font size as needed
    )

    // Define styles for the rounded rectangle container
    val roundedBoxModifier = Modifier
        .padding(16.dp)
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        )
        .padding(16.dp)

    // Generate dates for the next two weeks
    val today = LocalDate.now()
    val dates = (0 until 14).map { dayOffset ->
        today.plusDays(dayOffset.toLong()).format(DateTimeFormatter.ofPattern("EEE d MMM"))
    }

    // Example cinema locations
    val locations = listOf("Cinema 1", "Cinema 2", "Cinema 3")

    // Times for each cinema location
    val allTimes = listOf("10:00 AM", "01:00 PM", "04:00 PM", "07:00 PM")
    val filteredTimes = when (selectedExperience) {
        "2D" -> listOf("10:00 AM", "01:00 PM") // Example times for 2D
        "4DX" -> listOf("04:00 PM", "07:00 PM") // Example times for 4DX
        "IMAX" -> listOf("01:00 PM", "07:00 PM") // Example times for IMAX
        else -> allTimes
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        // Top Rectangle with Movie Poster and Back Button
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Back Button
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Movie Poster (replace with your drawable resource)
                // Image(painter = painterResource(id = R.drawable.movie_poster),
                //       contentDescription = null,
                //       modifier = Modifier
                //           .fillMaxHeight()
                //           .weight(1f))
            }

            // Movie Details
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(text = movieName, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))

                // Details in a Row with smaller text
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Estimate Time: $estimateTime", style = smallTextStyle)
                    Text(text = "Language: $language", style = smallTextStyle) // Shortened language format
                    Text(text = "Grade: $grade", style = smallTextStyle)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Rounded rectangle container for date and experience selection
        Box(modifier = roundedBoxModifier) {
            Column {
                // Date Selection
                Text(text = "Select Date", style = MaterialTheme.typography.titleMedium)
                // Add your DatePicker composable here
                Spacer(modifier = Modifier.height(8.dp))
                // Horizontal scrolling for dates
                LazyRow {
                    items(dates) { date ->
                        DateChip(date = date, isSelected = date == selectedDate) {
                            selectedDate = date
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Cinema Location Selection
                Text(text = "Select Cinema Location", style = MaterialTheme.typography.titleMedium)
                Column {
                    locations.forEach { location ->
                        Text(text = location, style = MaterialTheme.typography.headlineSmall)
                        filteredTimes.forEach { time ->
                            Text(
                                text = "$time - ${selectedExperience.ifEmpty { "Any" }}",
                                modifier = Modifier.clickable {
                                    selectedLocation = location
                                    selectedTime = time
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
        }

        Spacer(modifier = Modifier.height(16.dp))

            // Cinema Location Selection
            Text(text = "Select Cinema Location", style = MaterialTheme.typography.titleMedium)
            Column {
                locations.forEach { location ->
                    Text(text = location, style = MaterialTheme.typography.headlineSmall)
                    filteredTimes.forEach { time ->
                        Text(
                            text = "$time - ${selectedExperience ?: "Any"}",
                            modifier = Modifier.clickable {
                                selectedLocation = location
                                selectedTime = time
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Navigation Buttons
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onNavigateBack) {
                    Text("Back to Menu")
                }
                Button(onClick = {
                    // Check if selections are valid before proceeding
                    if (selectedDate.isNotEmpty() && selectedExperience.isNotEmpty() && selectedTime.isNotEmpty()) {
                        onNavigateNext()
                    }
                }) {
                    Text("Next")
                }
            }
    }
}



    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                TicketBookingModuleTheme {
                    DateCinemaLocationSelectionPage(
                        movieName = "Movie Name",
                        estimateTime = "Estimate Time",
                        language = "Language",
                        grade = "Grade",
                        onNavigateBack = { /* Handle navigation back to the menu */ },
                        onNavigateNext = { /* Handle navigation to the next page */ }
                    )
                }
            }
        }
    }

    //@Preview(showBackground = true)
    @Composable
    fun DateCinemaLocationSelectionPageReview() {
        TicketBookingModuleTheme {
            DateCinemaLocationSelectionPage(
                movieName = "Movie Name",
                estimateTime = "Estimate Time",
                language = "Language",
                grade = "Grade",
                onNavigateBack = { /* Preview navigation back action */ },
                onNavigateNext = { /* Preview navigation next action */ }
            )
        }
    }
}