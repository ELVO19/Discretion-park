package com.example.carparkingsystem.ui.theme.screens.car

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carparkingsystem.data.CarViewModel
import com.example.carparkingsystem.models.CarModel

private val BgDeep      = Color(0xFF0F0C29)
private val BgMid       = Color(0xFF1A1740)
private val BgCard      = Color(0xFF24243E)
private val Purple      = Color(0xFF7F5AF0)
private val PurpleLight = Color(0xFFAB8DF8)
private val Green       = Color(0xFF2CB67D)
private val Cyan        = Color(0xFF00E5FF)
private val Rose        = Color(0xFFFF6B8A)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextMuted   = Color(0xFF94A3B8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(navController: NavController) {
    val carViewModel: CarViewModel = viewModel()
    val cars = carViewModel.cars
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        carViewModel.fetchCar(context)
    }

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registered Vehicles",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(brush = Brush.horizontalGradient(listOf(Purple, Green)))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PurpleLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgDeep,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (cars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgDeep)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No vehicles registered", color = TextMuted, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Add a car from the dashboard", color = TextMuted.copy(0.6f), fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgDeep)
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BgMid)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${cars.size} vehicles total", color = TextMuted, fontSize = 13.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Purple.copy(0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Active", color = PurpleLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(cars, key = { it.id ?: it.hashCode().toString() }) { car ->
                    CarCard(
                        car = car,
                        onDelete = { carId -> carViewModel.deleteCar(carId, context) },
                        navController = navController
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun CarCard(
    car: CarModel,
    onDelete: (String) -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = BgCard,
            title = { Text("Delete Vehicle?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently remove ${car.plateNumber ?: "this vehicle"} from the system.",
                    color = TextMuted
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    car.id?.let { onDelete(it) }
                }) {
                    Text("Delete", color = Rose, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(Brush.horizontalGradient(listOf(Purple, Cyan, Green)))
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BgMid)
                        .border(2.dp, Purple.copy(0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!car.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = car.imageUrl,
                            contentDescription = "Car image",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = car.plateNumber?.take(2)?.uppercase() ?: "??",
                            color = PurpleLight,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.plateNumber ?: "Unknown Plate",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    InfoChip(
                        label = car.vehicleType ?: "Unknown",
                        chipColor = Purple.copy(0.2f),
                        textColor = PurpleLight
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(text = car.driverName ?: "No driver name", color = TextMuted, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = BgMid, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("update_car/${car.id}") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(0.5f)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Update", fontSize = 13.sp)
                }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Rose.copy(0.4f)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Delete", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, chipColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(chipColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}