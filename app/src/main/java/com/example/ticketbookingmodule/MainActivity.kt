package com.example.ticketbookingmodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketBookingApp()
        }
    }
}

@Composable
fun TicketBookingApp() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "dateCinemaSelection") {
            composable("dateCinemaSelection") {
                DateCinemaLocationSelectionPage(
                    movieName = "Your Movie Name", // Replace with your movie name
                    estimateTime = "2h 15m",
                    language = "EN",
                    grade = "PG-13",
                    selectedCinemaLocation = "Cineplex 1", // Pre-selected cinema location
                    onNavigateBack = { /* Handle back navigation */ },
                    onNavigateNext = { cinemaLocation, selectedTime, selectedExperience ->
                        // Handle navigation to the next page
                        navController.navigate("seatSelection/$cinemaLocation/$selectedTime/$selectedExperience")
                    },
                    availableShowtimes = listOf(
                        Pair("10:00 AM", "2D"),
                        Pair("01:00 PM", "4DX"),
                        Pair("04:00 PM", "IMAX"),
                        Pair("07:00 PM", "2D")
                    ) // Ensure this is a list of pairs
                )
            }
            composable("seatSelection/{cinemaLocation}/{selectedTime}/{selectedExperience}") { backStackEntry ->
                val cinemaLocation = backStackEntry.arguments?.getString("cinemaLocation")
                val selectedTime = backStackEntry.arguments?.getString("selectedTime")
                val selectedExperience = backStackEntry.arguments?.getString("selectedExperience")

                // Implement the Seat Selection page here or navigate to another composable
                SeatSelectionPage(cinemaLocation, selectedTime, selectedExperience) // Implement this page accordingly
            }
        }
    }
}

@Composable
fun SeatSelectionPage(cinemaLocation: String?, selectedTime: String?, selectedExperience: String?) {
    // Implementation of the Seat Selection page
    // You can pass the parameters to this composable and build your UI
    // This is just a placeholder
    Text(text = "Seat Selection for $cinemaLocation at $selectedTime with experience $selectedExperience")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TicketBookingApp()
}
