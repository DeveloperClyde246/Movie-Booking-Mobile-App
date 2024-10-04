package com.example.moviemenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_booking.RefundPage
import com.example.moviemenu.admin.AdminMainPage
import com.example.moviemenu.admin.ApproveRefundPage
import com.example.moviemenu.admin.RefundRequest
import com.example.moviemenu.database.AppDatabase
import com.example.moviemenu.database.BookingRepositoryImpl
import com.example.moviemenu.database.PaymentRepositorylmpl
import com.example.moviemenu.menu.CinemaMenuSearchScreen
import com.example.moviemenu.menu.MovieManageScreen

import com.example.moviemenu.order.ConfirmTicketBooking
import com.example.moviemenu.order.DateCinemaLocationSelectionPage
import com.example.moviemenu.order.ReviewSummaryPage
import com.example.moviemenu.order.SeatsSelectionPage
import com.example.moviemenu.payment.PaymentModeScreen
import com.example.moviemenu.payment.PaymentPageUI
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            AppDatabase.getDatabase(applicationContext).bookingDao().getAllBookings()
        }
        FirebaseApp.initializeApp(this)
        // Initialize Firebase Firestore
        db = Firebase.firestore
        setContent {
            MyApp(db)
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            // Logo placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Yellow, shape = RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))

            // Page title
            Text(
                text = "MBO",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.primary_color))
    ) {
        // Page subtitle
        Text(
            text = "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun Footer(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp),  // Add vertical padding
        horizontalArrangement = Arrangement.SpaceEvenly,  // Spread buttons evenly
        verticalAlignment = Alignment.CenterVertically  // Align buttons vertically in the center
    ) {
        // Refund Button
        Button(
            onClick = { navController.navigate("requestRefund") },
            modifier = Modifier.weight(1f),  // Ensure equal space for each button
            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray))
        ) {
            Text(text = "Refunds", color = Color.White, fontSize = 9.sp)  // Smaller text
        }

        // Cinemas Button
        Button(
            onClick = { navController.navigate("cinemaMenuSearch") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray))
        ) {
            Text(text = "Cinema", color = Color.White, fontSize = 9.sp)  // Smaller text
        }

        // Admin Button
        Button(
            onClick = {
                navController.navigate("adminMainPage") // Add navigation to AdminMainScreen
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray))
        ) {
            Text(text = "Admin", color = Color.White, fontSize = 9.sp)  // Smaller text
        }

        // F&B Button
        Button(
            onClick = { /* Navigate to F&B screen */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray))
        ) {
            Text(text = "F&B", color = Color.White, fontSize = 9.sp)  // Smaller text
        }

        // Profile Button
        Button(
            onClick = { /* Navigate to Profile screen */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray))
        ) {
            Text(text = "Me", color = Color.White, fontSize = 9.sp)  // Smaller text
        }
    }
}

@Composable
fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("E d MMM hh:mm a", Locale.getDefault())
    return dateFormat.format(date)
}

//@Preview
@Composable
fun MyApp(db: FirebaseFirestore) {
    val navController = rememberNavController()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header()

        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController, startDestination = "cinemaMenuSearch") {
                // ------- Footer section
                // Refund navigation
                composable("requestRefund") {
                    RefundPage() // Pass db to the screen
                }

                // Admin navigation
                composable("adminMainPage") {
                    AdminMainPage(
                        onMovieManagementClick = { navController.navigate("MovieManageScreen") },
                        onFAndBManagementClick = { navController.navigate("fAndBManagement") },
                        onRefundManagementClick = { navController.navigate("refundManagement") }
                    )
                }
                // Movie Manage Screen
                composable("movieManageScreen") {
                    MovieManageScreen(db = db) // Pass db to the screen
                }

                // Approve Refund Screen
                composable("refundManagement") {
                    ApproveRefundPage() // Pass db to the screen
                }

                // ------- Content section
                // Main menu page navigation
                composable("cinemaMenuSearch") { CinemaMenuSearchScreen(navController, db) }
                composable("movieMenuScreen/{cinema}") { backStackEntry ->
                    val cinema = backStackEntry.arguments?.getString("cinema") ?: ""
                    MovieMenuScreen(cinema, db, navController)
                }
                // Date Cinema Location Selection Page navigation
                composable("dateCinemaLocationSelection/{movieName}/{cinemaLocation}/{estimateTime}/{language}") { backStackEntry ->
                    val movieName = backStackEntry.arguments?.getString("movieName") ?: "Unknown Movie"
                    val cinemaLocation = backStackEntry.arguments?.getString("cinemaLocation") ?: "Unknown Cinema"
                    val estimateTime = backStackEntry.arguments?.getString("estimateTime") ?: "Unknown Time"
                    val language = backStackEntry.arguments?.getString("language") ?: "Unknown Language"

                    // Navigate to the DateCinemaLocationSelectionPage with the selected movie details
                    DateCinemaLocationSelectionPage(
                        movieName = movieName,
                        estimateTime = estimateTime, // Example static value, you can update this dynamically
                        language = language, // Example static value, you can update this dynamically
                        cinema = cinemaLocation,
                        navController = navController,
                        bookingRepository = BookingRepositoryImpl(AppDatabase.getDatabase(navController.context).bookingDao())
                    )
                    GlobalVariables.cinemaLocations=cinemaLocation
                }
                // Seat Selection page navigation
                composable(
                    route = "seatsSelection/{movieName}/{cinemaLocation}/{selectedTime}/{selectedExperience}/{selectedDate}/{availableShowtime}/{cinemaHall}/{estimateTime}",
                    arguments = listOf(
                        navArgument("movieName") { type = NavType.StringType },
                        navArgument("cinemaLocation") { type = NavType.StringType },
                        navArgument("selectedTime") { type = NavType.StringType },
                        navArgument("selectedExperience") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("availableShowtime") { type = NavType.StringType },
                        navArgument("cinemaHall") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val movieName = backStackEntry.arguments?.getString("movieName") ?: ""
                    val estimateTime = backStackEntry.arguments?.getString("estimateTime") ?: ""
                    val cinema = backStackEntry.arguments?.getString("cinema") ?: ""
                    val time = backStackEntry.arguments?.getString("availableShowtime") ?: ""
                    val experience = backStackEntry.arguments?.getString("selectedExperience") ?: ""
                    val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val cinemaHall = backStackEntry.arguments?.getString("cinemaHall") ?: ""
                    SeatsSelectionPage(
                        movieName = movieName,
                        estimateTime = estimateTime,
                        movieExperience = experience,
                        cinemaLocation = cinema,
                        selectedDate = selectedDate,
                        availableShowtime = time,
                        cinemaHall=cinemaHall,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateNext = { movieDetails2 ->
                            // Navigate to ConfirmTicketBooking screen using the combined MovieDetails2
                            navController.navigate(
                                "confirmTicketBooking/${movieDetails2.movieName}/${movieDetails2.cinemaLocation}/${movieDetails2.selectedTime}/${movieDetails2.selectedExperience}/${movieDetails2.selectedDate}/${movieDetails2.selectedSeats}/${movieDetails2.cinemaHall}"

                            )
                        }
                    )
                    GlobalVariables.experience=experience
                }
                // Confirm Ticket Booking navigation
                // Confirm Ticket Booking navigation
                composable(
                    "confirmTicketBooking/{movieName}/{cinema}/{time}/{experience}/{selectedDate}/{selectedSeats}/{cinemaHall}",
                    arguments = listOf(
                        navArgument("movieName") { type = NavType.StringType },
                        navArgument("cinema") { type = NavType.StringType },
                        navArgument("time") { type = NavType.StringType },
                        navArgument("experience") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedSeats") { type = NavType.StringType },
                        navArgument("cinemaHall") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val movieName = backStackEntry.arguments?.getString("movieName") ?: ""
                    val cinema = backStackEntry.arguments?.getString("cinema") ?: ""
                    val time = backStackEntry.arguments?.getString("time") ?: ""
                    val experience = backStackEntry.arguments?.getString("experience") ?: ""
                    val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val selectedSeats = backStackEntry.arguments?.getString("selectedSeats")?.split(",") ?: emptyList()
                    val cinemaHall = backStackEntry.arguments?.getString("cinemaHall") ?: ""

                    ConfirmTicketBooking(
                        movieName = movieName,
                        cinemaLocation = cinema,
                        selectedTime = time,
                        selectedExperience = experience,
                        selectedDate = selectedDate,
                        selectedSeats = selectedSeats,
                        cinemaHall = cinemaHall,
                        onNavigateBack = { navController.popBackStack() },
                        onConfirm = {
                                movieName, cinemaLocation, selectedTime, selectedExperience, selectedDate, selectedSeats, cinemaHall, ticketDetails ->
                            navController.navigate(
                                "reviewSummary/$movieName/$cinemaLocation/$selectedTime/$selectedExperience/$selectedDate/${selectedSeats.joinToString(",")}/$cinemaHall/${serializeTicketDetails(ticketDetails)}")
                        },
                        bookingRepository = BookingRepositoryImpl(AppDatabase.getDatabase(navController.context).bookingDao())
                    )
                }
                // Review Summary Page navigation
                composable(
                    "reviewSummary/{movieName}/{cinema}/{time}/{experience}/{selectedDate}/{selectedSeats}/{cinemaHall}/{ticketDetails}",
                    arguments = listOf(
                        navArgument("movieName") { type = NavType.StringType },
                        navArgument("cinema") { type = NavType.StringType },
                        navArgument("time") { type = NavType.StringType },
                        navArgument("experience") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedSeats") { type = NavType.StringType },
                        navArgument("cinemaHall") { type = NavType.StringType },
                        navArgument("ticketDetails") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val movieName = backStackEntry.arguments?.getString("movieName") ?: ""
                    val cinema = backStackEntry.arguments?.getString("cinema") ?: ""
                    val time = backStackEntry.arguments?.getString("time") ?: ""
                    val experience = backStackEntry.arguments?.getString("experience") ?: ""
                    val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val selectedSeats = backStackEntry.arguments?.getString("selectedSeats")?.split(",") ?: emptyList()
                    val cinemaHall = backStackEntry.arguments?.getString("cinemaHall") ?: ""
                    val ticketDetailsString = backStackEntry.arguments?.getString("ticketDetails") ?: ""

                    // Deserialize the ticket details string back to a map
                    val ticketDetails = deserializeTicketDetails(ticketDetailsString)

                    GlobalVariables.cinemaLocations?.let {
                        ReviewSummaryPage(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToPayment = { movieName, cinemaLocation, totalPrice, ticketDetails, cinemaHall, selectedSeats, selectedDate, selectedTime ->
                                val ticketDetailsString = serializeTicketDetails(ticketDetails)
                                navController.navigate(
                                    "paymentPage/$movieName/$cinemaLocation/$totalPrice/$ticketDetailsString/$cinemaHall/${selectedSeats.joinToString(",")}/$selectedDate/$selectedTime"
                                )
                            },
                            movieName = movieName, // You can pass the actual movie name
                            cinemaLocation = it,
                            selectedDate = selectedDate,
                            selectedTime = time,
                            selectedSeats = selectedSeats,
                            cinemaHall = cinemaHall,
                            ticketDetails = ticketDetails,
                            bookingRepository = BookingRepositoryImpl(AppDatabase.getDatabase(navController.context).bookingDao())
                        )
                    }
                }

                // Payment page navigation
                composable(
                    "paymentPage/{movieName}/{cinemaLocation}/{totalPrice}/{ticketDetails}/{cinemaHall}/{selectedSeats}/{selectedDate}/{selectedTime}",
                    arguments = listOf(
                        navArgument("movieName") { type = NavType.StringType },
                        navArgument("cinemaLocation") { type = NavType.StringType },
                        navArgument("totalPrice") { type = NavType.FloatType },
                        navArgument("ticketDetails") { type = NavType.StringType },
                        navArgument("cinemaHall") { type = NavType.StringType },
                        navArgument("selectedSeats") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTime") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val movieName = backStackEntry.arguments?.getString("movieName") ?: ""
                    val cinemaLocation = backStackEntry.arguments?.getString("cinemaLocation") ?: ""
                    val totalPrice = backStackEntry.arguments?.getFloat("totalPrice")?.toDouble() ?: 0.0
                    val ticketDetailsString = backStackEntry.arguments?.getString("ticketDetails") ?: ""
                    val cinemaHall = backStackEntry.arguments?.getString("cinemaHall") ?: ""
                    val selectedSeats = backStackEntry.arguments?.getString("selectedSeats")?.split(",") ?: emptyList()
                    val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val selectedTime = backStackEntry.arguments?.getString("selectedTime") ?: ""

                    // Deserialize ticketDetails from the string
                    val ticketDetails = deserializeTicketDetails(ticketDetailsString)

                    PaymentPageUI(
                        navController = navController,  // Pass the navController here
                        movieName = movieName,
                        cinemaLocation = cinemaLocation,
                        totalPrice = totalPrice,
                        selectedDate = selectedDate,
                        selectedTime = selectedTime,  // Make sure selectedTime is passed
                        ticketDetails = ticketDetails,
                        cinemaHall = cinemaHall,
                        selectedSeats = selectedSeats,
                        onNavigate = { paymentMethod ->
                            // Navigate to PaymentModeScreen with the selected payment method and transaction details
                            navController.navigate(
                                "paymentMode/$paymentMethod/$cinemaLocation/$selectedDate/$selectedTime/${"%.2f".format(totalPrice)}"
                            )
                        }
                    )
                }
                // Payment Mode navigation
                composable(
                    "paymentMode/{paymentMode}/{cinemaLocation}/{selectedDate}/{selectedTime}/{totalPrice}",
                    arguments = listOf(
                        navArgument("paymentMode") { type = NavType.StringType },
                        navArgument("cinemaLocation") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTime") { type = NavType.StringType },
                        navArgument("totalPrice") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val paymentMode = backStackEntry.arguments?.getString("paymentMode") ?: ""
                    val cinemaLocation = backStackEntry.arguments?.getString("cinemaLocation") ?: ""
                    val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val selectedTime = backStackEntry.arguments?.getString("selectedTime") ?: ""
                    val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toDouble() ?: 0.0

                    PaymentModeScreen(
                        paymentMode = paymentMode,
                        navController = navController,
                        cinemaName = "MBO Cinemas", // Pass cinema name here
                        selectedDate = selectedDate,
                        selectedTime = selectedTime,
                        cinemaLocation = cinemaLocation,
                        totalPrice = totalPrice,
                        paymentRepository = PaymentRepositorylmpl(
                            AppDatabase.getDatabase(navController.context).paymentDao()
                        )
                    )
                }


                // ------------
            }
        }
        Footer(navController = navController)
    }
}

// Utility functions for serializing and deserializing ticketDetails
fun serializeTicketDetails(ticketDetails: Map<String, Int>): String {
    return ticketDetails.entries.joinToString(",") { "${it.key}:${it.value}" }
}

fun deserializeTicketDetails(ticketDetailsString: String): Map<String, Int> {
    return ticketDetailsString.split(",").map {
        val (key, value) = it.split(":")
        key to value.toInt()
    }.toMap()
}

object GlobalVariables{
    var cinemaLocations: String? = ""
    var experience: String? = ""

}
