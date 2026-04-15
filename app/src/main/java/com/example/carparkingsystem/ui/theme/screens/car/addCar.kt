package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.*

import androidx.compose.material3.rememberTimePickerState
import java.util.Calendar

import androidx.compose.material.icons.filled.DateRange
import com.example.carparkingsystem.data.CarViewModel
import java.util.Locale
import androidx.compose.foundation.layout.Row



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
fun AddCarScreen(navController: NavController) {
    var imageUri    by remember { mutableStateOf<Uri?>(null) }
    var plateNumber by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("") }
    var driverName  by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var carColor by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var entryTime by remember { mutableStateOf("") }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }
    val carViewModel: CarViewModel = viewModel()

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Car Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(listOf(Purple, Green))
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = BgDeep,
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


            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Purple.copy(alpha = 0.35f), Green.copy(alpha = 0.25f)),
                            start = Offset(0f, 0f),
                            end   = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter            = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.Person,
                        contentDescription = null,
                        modifier           = Modifier.size(64.dp),
                        tint               = PurpleLight
                    )
                }
            }

            Spacer(Modifier.height(12.dp))


            Button(
                onClick = { launcher.launch("image/*") },
                shape   = RoundedCornerShape(12.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Select Image", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(28.dp))


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

            OutlinedTextField(
                value         = plateNumber,
                onValueChange = { plateNumber = it },
                label         = { Text("Plate Number") },
                modifier      = fieldModifier,
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = vehicleType,
                onValueChange = { vehicleType = it },
                label         = { Text("Vehicle Type") },
                modifier      = fieldModifier,
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            data class CarColor(val name: String, val color: Color)

            val colorsList = listOf(
                CarColor("Red", Color(0xFFD32F2F)),
                CarColor("Blue", Color(0xFF1976D2)),
                CarColor("Black", Color.Black),
                CarColor("White", Color.White),
                CarColor("Silver", Color(0xFFB0BEC5)),
                CarColor("Green", Color(0xFF388E3C)),
                CarColor("Yellow", Color(0xFFFBC02D)),
                CarColor("Orange", Color(0xFFF57C00)),
                CarColor("Purple", Color(0xFF7B1FA2)),
                CarColor("Pink", Color(0xFFE91E63)),
                CarColor("Brown", Color(0xFF5D4037)),
                CarColor("Gold", Color(0xFFFFD700))
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = carColor,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Car Color") },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(BgCard, RoundedCornerShape(12.dp))
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
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = CircleShape
                                            )
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = item.name,
                                        color = if (isSelected) PurpleLight else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                carColor = item.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = driverName,
                onValueChange = { driverName = it },
                label         = { Text("Driver Name") },
                modifier      = fieldModifier,
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = phoneNumber,
                onValueChange = { phoneNumber = it },
                label         = { Text("Phone Number") },
                modifier      = fieldModifier,
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = fieldColors
            )

            Spacer(Modifier.height(14.dp))

            var showTimePicker by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = entryTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("Entry Time") },
                modifier = fieldModifier,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Time", tint = PurpleLight)
                    }
                }
            )

            if (showTimePicker) {
                val timePickerState = rememberTimePickerState(
                    initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
                    is24Hour = true
                )
                AlertDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            entryTime = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            showTimePicker = false
                        }) { Text("OK", color = Purple) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel", color = TextMuted)
                        }
                    },
                    containerColor = BgCard,
                    text = {
                        TimePicker(state = timePickerState)
                    }
                )
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick  = { carViewModel.uploadCar(
                    imageUri,
                    plateNumber,
                    vehicleType,
                    driverName,
                    phoneNumber,
                    carColor,
                    entryTime,
                    context,
                    navController
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
                    text       = "Save Car",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White,
                    style      = TextStyle(
                        brush = Brush.horizontalGradient(listOf(PurpleLight, Cyan))
                    )
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddCarScreenPreview() {
    AddCarScreen(rememberNavController())
}