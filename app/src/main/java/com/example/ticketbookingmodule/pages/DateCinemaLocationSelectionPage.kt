package com.example.ticketbookingmodule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateCinemaLocationSelectionPage(
    movieName: String,
    estimateTime: String,
    language: String,
    grade: String,
    selectedCinemaLocation: String, // Pre-selected cinema location
    onNavigateBack: () -> Unit,
    onNavigateNext: (String, String, String) -> Unit, // Pass selected cinema location, time, and experience
    availableShowtimes: List<Pair<String, String>> // Change to List of Pair to hold time and experience
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedExperience by remember { mutableStateOf("") } // Change from val to var
    var filteredShowtimes by remember { mutableStateOf(availableShowtimes) }
    var showDateError by remember { mutableStateOf(false) } // To show error if no date is selected

    // Handle filtering showtimes when the selected experience changes
    val onExperienceSelected: (String) -> Unit = { experience ->
        selectedExperience = experience
        filteredShowtimes = if (experience.isNotEmpty()) {
            availableShowtimes.filter { it.second == experience }
        } else {
            availableShowtimes
        }
    }


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
    val dateFormatter = DateTimeFormatter.ofPattern("EEE d MMM")
    val nextTwoWeeks = (1..14).map { today.plusDays(it.toLong()).format(dateFormatter) }

    // Handle Date Selection
    val onDateSelected: (String) -> Unit = { date ->
        selectedDate = date
        showDateError = false // Reset the error when a date is selected
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            // Top Rectangle with Movie Poster and Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(48.dp) // Size of the round-curved square
                        .padding(8.dp) // Padding to position the IconButton within the rectangle
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }

        item {
            // Spacer to separate the top rectangle from movie details
            Spacer(modifier = Modifier.height(16.dp))

            // Movie Details (Name, Estimate Time, Language, and Grade)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
            ) {
                // Movie name centered
                Text(
                    text = movieName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp) // Add padding below the movie name
                )

                // Center the estimate time, language, and grade in one line
                Text(
                    text = "$estimateTime.$language.$grade", // Concatenated format
                    style = MaterialTheme.typography.bodyMedium, // You can adjust this style as needed
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Align the text to center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Rounded rectangle container for date selection
            Box(modifier = roundedBoxModifier) {
                Column {
                    // Date Selection
                    Text(text = "Select Date", style = MaterialTheme.typography.titleMedium)
                    // Horizontal scrolling for dates
                    LazyRow (modifier = Modifier.padding(8.dp)){
                        items(nextTwoWeeks) { date ->
                           Button(
                               onClick = {onDateSelected(date)},
                               colors = ButtonDefaults.buttonColors(
                                   containerColor = if (selectedDate == date) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                               ),
                               modifier = Modifier.padding(8.dp)
                           ){
                               Text(
                                   text = date,
                                   color = if (selectedExperience == date)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                               )
                           }
                        }
                    }

                    if (showDateError) {
                        Text(
                            text = "Please select a date first",
                            color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Movie Experience Selection
                    Text(
                        text = "Select Movie Experience",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val experiences = listOf("2D", "4DX", "IMAX")
                        experiences.forEach { experience ->
                            Button(
                                onClick = { onExperienceSelected(experience) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedExperience == experience) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = experience,
                                    color = if (selectedExperience == experience)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Cinema location section
            Text(
                text = "Selected Cinema: $selectedCinemaLocation",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            Text(text = "Available Showtimes:", style = MaterialTheme.typography.titleMedium)
            LazyRow (modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                items(filteredShowtimes) { (time, experience) ->
                    Button(
                        onClick = {
                            if(selectedDate.isEmpty()){
                                // If no date is selected, show error
                                showDateError = true
                            }else{
                            onNavigateNext(selectedCinemaLocation, time, experience)} // Navigate when a button is clicked
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = time) // Show time on the top
                            HorizontalDivider() // Divider line
                            Text(text = experience) // Show movie experience at the bottom
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



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

@Preview(showBackground = true)
@Composable
fun DateCinemaLocationSelectionPreview() {
    DateCinemaLocationSelectionPage(
        movieName = "Your Movie Name",
        estimateTime = "2h 15m",
        language = "EN",
        grade = "PG-13",
        selectedCinemaLocation = "Cineplex 1",
        onNavigateBack = { /* Handle back navigation in preview */ },
        onNavigateNext = { cinemaLocation, selectedTime, selectedExperience ->
            // Handle navigation in preview (you can leave it empty for preview)
        },
        availableShowtimes = listOf(
            Pair("10:00 AM", "2D"),
            Pair("01:00 PM", "4DX"),
            Pair("04:00 PM", "IMAX"),
            Pair("07:00 PM", "2D")
        )
    )
}