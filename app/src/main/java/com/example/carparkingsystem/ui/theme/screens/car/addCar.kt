package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.carparkingsystem.data.CarViewModel
import java.util.Calendar
import java.util.Locale

private val BgDeep      = Color(0xFF0F0C29)
private val BgCard      = Color(0xFF24243E)
private val Purple      = Color(0xFF7F5AF0)
private val PurpleLight = Color(0xFFAB8DF8)
private val Green       = Color(0xFF2CB67D)
private val Cyan        = Color(0xFF00E5FF)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextMuted   = Color(0xFF94A3B8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarScreen(navController: NavController, carId: String) {
    val carViewModel: CarViewModel = viewModel()
    val context = LocalContext.current

    // Find the car from the already-loaded list
    val car = carViewModel.cars.find { it.id == carId }

    // Pre-fill fields with existing data
    var imageUri     by remember { mutableStateOf<Uri?>(null) }
    var plateNumber  by remember { mutableStateOf(car?.plateNumber  ?: "") }
    var vehicleType  by remember { mutableStateOf(car?.vehicleType  ?: "") }
    var driverName   by remember { mutableStateOf(car?.driverName   ?: "") }
    var phoneNumber  by remember { mutableStateOf(car?.phoneNumber  ?: "") }
    var carColor     by remember { mutableStateOf(car?.carColor     ?: "") }
    var entryTime    by remember { mutableStateOf(car?.entryTime    ?: "") }
    var expanded     by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor   = Purple,
        unfocusedBorderColor = TextMuted.copy(alpha = 0.4f),
        focusedLabelColor    = PurpleLight,
        unfocusedLabelColor  = TextMuted,
        cursorColor          = Purple,
        focusedTextColor     = TextPrimary,
        unfocusedTextColor   = TextPrimary
    )
    val fieldModifier = Modifier.fillMaxWidth()

    data class CarColorItem(val name: String, val color: Color)
    val colorsList = listOf(
        CarColorItem("Red",    Color(0xFFD32F2F)),
        CarColorItem("Blue",   Color(0xFF1976D2)),
        CarColorItem("Black",  Color.Black),
        CarColorItem("White",  Color.White),
        CarColorItem("Silver", Color(0xFFB0BEC5)),
        CarColorItem("Green",  Color(0xFF388E3C)),
        CarColorItem("Yellow", Color(0xFFFBC02D)),
        CarColorItem("Orange", Color(0xFFF57C00)),
        CarColorItem("Purple", Color(0xFF7B1FA2)),
        CarColorItem("Pink",   Color(0xFFE91E63)),
        CarColorItem("Brown",  Color(0xFF5D4037)),
        CarColorItem("Gold",   Color(0xFFFFD700))
    )

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Update Vehicle",
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgDeep)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Avatar ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Purple.copy(0.35f), Green.copy(0.25f)),
                            start = Offset(0f, 0f),
                            end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // New image picked by user
                    imageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // Existing image from Firebase
                    !car?.imageUrl.isNullOrBlank() -> {
                        AsyncImage(
                            model = car?.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = PurpleLight
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                shape  = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Change Image", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(28.dp))

            // ── Fields ────────────────────────────────────────────────────
            OutlinedTextField(
                value = plateNumber, onValueChange = { plateNumber = it },
                label = { Text("Plate Number") },
                modifier = fieldModifier, singleLine = true,
                shape = RoundedCornerShape(12.dp), colors = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = vehicleType, onValueChange = { vehicleType = it },
                label = { Text("Vehicle Type") },
                modifier = fieldModifier, singleLine = true,
                shape = RoundedCornerShape(12.dp), colors = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            // ── Color dropdown ────────────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = carColor, onValueChange = {},
                    readOnly = true,
                    label = { Text("Car Color") },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    shape = RoundedCornerShape(12.dp), colors = fieldColors
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(BgCard, RoundedCornerShape(12.dp))
                ) {
                    colorsList.forEach { item ->
                        val isSelected = carColor == item.name
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(item.color, CircleShape)
                                            .border(1.dp, Color.Gray, CircleShape)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = item.name,
                                        color = if (isSelected) PurpleLight else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = { carColor = item.name; expanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = driverName, onValueChange = { driverName = it },
                label = { Text("Driver Name") },
                modifier = fieldModifier, singleLine = true,
                shape = RoundedCornerShape(12.dp), colors = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = phoneNumber, onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = fieldModifier, singleLine = true,
                shape = RoundedCornerShape(12.dp), colors = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            // ── Entry time ────────────────────────────────────────────────
            OutlinedTextField(
                value = entryTime, onValueChange = {},
                readOnly = true,
                label = { Text("Entry Time") },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp), colors = fieldColors,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Time", tint = PurpleLight)
                    }
                }
            )

            if (showTimePicker) {
                val timePickerState = rememberTimePickerState(
                    initialHour   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
                    is24Hour      = true
                )
                AlertDialog(
                    onDismissRequest = { showTimePicker = false },
                    containerColor = BgCard,
                    confirmButton = {
                        TextButton(onClick = {
                            entryTime = String.format(
                                Locale.getDefault(), "%02d:%02d",
                                timePickerState.hour, timePickerState.minute
                            )
                            showTimePicker = false
                        }) { Text("OK", color = Purple) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel", color = TextMuted)
                        }
                    },
                    text = { TimePicker(state = timePickerState) }
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Save button ───────────────────────────────────────────────
            Button(
                onClick = {
                    carViewModel.updateCar(
                        carId            = carId,
                        imageUri         = imageUri,
                        existingImageUrl = car?.imageUrl,
                        plateNumber      = plateNumber,
                        vehicleType      = vehicleType,
                        driverName       = driverName,
                        phoneNumber      = phoneNumber,
                        carColor         = carColor,
                        entryTime        = entryTime,
                        context          = context,
                        navController    = navController
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        Brush.linearGradient(listOf(Purple, Color(0xFF5A3ED9))),
                        shape = RoundedCornerShape(14.dp)
                    ),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text       = "Save Changes",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White,
                    style      = TextStyle(brush = Brush.horizontalGradient(listOf(PurpleLight, Cyan)))
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateCarScreenPreview() {
    UpdateCarScreen(rememberNavController(), carId = "preview_id")
}