package com.example.ticketbookingmodule.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbookingmodule.ui.theme.TicketBookingModuleTheme

@Composable
fun SeatsSelectionPage(
    movieName: String,
    estimateTime: String,
    movieExperience: String,
    movieGrade: String,
    cinemaLocation: String,
    cinemaHall: String,
    dateTime: String,
    availableShowtime: String,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val rows = ('A'..'F').toList() // 6 rows
    val columns = (1..8).toList() // Reduced to 8 columns
    val reservedSeats = listOf("A1", "B3", "C5") // Example reserved seats
    val okuSeats = listOf("D2", "E4") // Example OKU seats
    var selectedSeats by remember { mutableStateOf<List<String>>(emptyList()) }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Header and Movie Details
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Select Seats",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            // Movie Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = movieName, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = estimateTime, style = MaterialTheme.typography.bodyMedium)
                    Text(text = " • ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = movieExperience, style = MaterialTheme.typography.bodyMedium)
                    Text(text = " • ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = movieGrade, style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            // Cinema Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = cinemaLocation, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = cinemaHall, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = dateTime, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 24.dp))
                Text(text = "Available Showtime: $availableShowtime", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Replace the nested LazyColumn with a Column for the seat grid
        item {
            Text(text = "Select Your Seats", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 16.dp))

            // Grid for seat selection
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp)
            ) {
                for (row in rows) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        for (column in columns) {
                            val seat = "${row}${column}"
                            SeatItem(
                                seat = seat,
                                isSelected = selectedSeats.contains(seat),
                                isReserved = reservedSeats.contains(seat),
                                isOKU = okuSeats.contains(seat)
                            ) {
                                selectedSeats = if (selectedSeats.contains(seat)) {
                                    selectedSeats - seat
                                } else {
                                    selectedSeats + seat
                                }
                            }
                        }
                    }
                }
            }
        }

        // Categories for seat types
        item {
            Spacer(modifier = Modifier.height(16.dp)) // Spacing before categories
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SeatCategoryItem(color = Color.Green, label = "Available")
                SeatCategoryItem(color = Color.Gray, label = "Reserved")
                SeatCategoryItem(color = Color.Blue, label = "OKU")
            }
        }

        // Continue Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateNext, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text(text = "Continue", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SeatItem(seat: String, isSelected: Boolean, isReserved: Boolean, isOKU: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isReserved -> Color.Gray
        isOKU -> Color.Blue
        isSelected -> Color.Yellow
        else -> MaterialTheme.colorScheme.surface
    }

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable(
                enabled = !isReserved && !isOKU, // Prevent selection for reserved and OKU seats
                onClick = onClick
            )
            .semantics { // Add accessibility information
                contentDescription = when {
                    isReserved -> "Seat $seat is reserved"
                    isOKU -> "Seat $seat is designated for OKU"
                    isSelected -> "Seat $seat selected"
                    else -> "Seat $seat available"
                }
            },
        color = backgroundColor,
        shape = MaterialTheme.shapes.small,
        contentColor = if (isReserved || isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
    ) {
        Text(
            text = seat,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isReserved || isSelected) Color.White else Color.Black // Adjust text color for contrast
        )
    }
}

@Composable
fun SeatCategoryItem(color: Color, label: String) {
    // Create a surface with round corners to hold both the color and label
    Surface(
        modifier = Modifier
            .padding(8.dp) // Padding around the surface
            .height(50.dp) // Fixed height for the category item
            .wrapContentWidth(), // Adjust width based on content
        color = MaterialTheme.colorScheme.surface, // Background color for the grouping
        shape = MaterialTheme.shapes.medium // Medium shape for round corners
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize() // Fill the available space
                .padding(8.dp), // Padding inside the row
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Arrange items to the start
        ) {
            Surface(
                modifier = Modifier
                    .size(24.dp) // Adjusted size for the color indicator
                    .padding(4.dp),
                color = color,
                shape = MaterialTheme.shapes.small // Smaller shape for rounded corners
            ) {}

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeatsSelectionPagePreview() {
    TicketBookingModuleTheme {
        // Sample data for preview
        SeatsSelectionPage(
            movieName = "Movie Name",
            estimateTime = "2h 15m",
            movieExperience = "IMAX",
            movieGrade = "PG-13",
            cinemaLocation = "Cineplex Mall",
            cinemaHall = "Hall 1",
            dateTime = "2024-09-30 10:00 AM",
            availableShowtime = "10:00 AM",
            onNavigateBack = { /* Handle back navigation */ },
            onNavigateNext = { /* Handle next navigation */ }
        )
    }
}
