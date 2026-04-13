package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// ── Dashboard colour tokens ───────────────────────────────────────────────────
private val BgDeep      = Color(0xFF0F0C29)
private val BgCard      = Color(0xFF24243E)
private val Purple      = Color(0xFF7F5AF0)
private val PurpleLight = Color(0xFFAB8DF8)
private val Green       = Color(0xFF2CB67D)
private val Cyan        = Color(0xFF00E5FF)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextMuted   = Color(0xFF94A3B8)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen() {
    var imageUri    by remember { mutableStateOf<Uri?>(null) }
    var plateNumber by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("") }
    var driverName  by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

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

            // ── Avatar / image picker ─────────────────────────────────────
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

            // ── Select Image button ───────────────────────────────────────
            Button(
                onClick = { launcher.launch("image/*") },
                shape   = RoundedCornerShape(12.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Select Image", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(28.dp))

            // ── Text fields ───────────────────────────────────────────────
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

            Spacer(Modifier.height(28.dp))

            // ── Save Car button ───────────────────────────────────────────
            Button(
                onClick  = { /* TODO: save car logic */ },
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
    AddCarScreen()
}