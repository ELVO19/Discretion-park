package com.example.carparkingsystem.ui.theme.screens.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.data.AuthViewModel   // ← import VM

private val BgDeep      = Color(0xFF0F0C29)
private val BgMid       = Color(0xFF1A1740)
private val BgCard      = Color(0xFF24243E)
private val Purple      = Color(0xFF7F5AF0)
private val PurpleLight = Color(0xFFAB8DF8)
private val Green       = Color(0xFF2CB67D)
private val GreenLight  = Color(0xFF5DDBA4)
private val Cyan        = Color(0xFF00E5FF)
private val Amber       = Color(0xFFFFB347)
private val Rose        = Color(0xFFFF6B8A)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextMuted   = Color(0xFF94A3B8)

private const val DEFAULT_OCCUPIED = 32
private const val DEFAULT_TOTAL    = 50

@Composable
private fun DonutChart(occupied: Int, total: Int, modifier: Modifier = Modifier) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(1f, animationSpec = tween(1200, easing = EaseOutCubic))
    }
    val progress         = animProgress.value
    val occupiedFraction = (occupied.toFloat() / total) * progress

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke  = 22.dp.toPx()
            val inset   = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(inset, inset)
            drawArc(
                color = BgMid, startAngle = -90f, sweepAngle = 360f,
                useCenter = false, topLeft = topLeft, size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(Purple, Rose, Amber)),
                startAngle = -90f, sweepAngle = 360f * occupiedFraction,
                useCenter = false, topLeft = topLeft, size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(Green, Cyan, GreenLight)),
                startAngle = -90f + 360f * occupiedFraction,
                sweepAngle = 360f * (1f - occupiedFraction),
                useCenter = false, topLeft = topLeft, size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(occupiedFraction * 100).toInt()}%",
                fontSize = 22.sp, fontWeight = FontWeight.Black, color = TextPrimary
            )
            Text(text = "Occupied", fontSize = 11.sp, color = TextMuted)
        }
    }
}


@Composable
private fun WeeklyBarChart(modifier: Modifier = Modifier) {
    val days   = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val data   = listOf(0.55f, 0.72f, 0.88f, 0.65f, 0.90f, 0.45f, 0.30f)
    val colors = listOf(Purple, PurpleLight, Rose, Amber, Green, Cyan, PurpleLight)
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(1f, animationSpec = tween(1000, easing = EaseOutCubic))
    }
    Canvas(modifier = modifier) {
        val barWidth  = size.width / (days.size * 2f)
        val maxBarH   = size.height * 0.78f
        val baselineY = size.height * 0.88f
        days.indices.forEach { i ->
            val barH  = maxBarH * data[i] * animProgress.value
            val left  = barWidth / 2f + i * (barWidth + barWidth)
            val top   = baselineY - barH
            val right = left + barWidth
            drawRoundRect(
                color = colors[i].copy(alpha = 0.18f),
                topLeft = Offset(left + 3.dp.toPx(), top + 4.dp.toPx()),
                size = Size(right - left, barH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
            )
            drawRoundRect(
                brush = Brush.verticalGradient(
                    listOf(colors[i], colors[i].copy(alpha = 0.5f)),
                    startY = top, endY = baselineY
                ),
                topLeft = Offset(left, top),
                size = Size(right - left, barH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
            )
            drawCircle(
                color = colors[i], radius = 3.dp.toPx(),
                center = Offset(left + (right - left) / 2f, baselineY + 6.dp.toPx())
            )
        }
        drawLine(
            color = BgMid,
            start = Offset(0f, baselineY), end = Offset(size.width, baselineY),
            strokeWidth = 1.dp.toPx()
        )
    }
}


@Composable
private fun MiniStatCard(
    label: String, value: String, sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Text(text = sub, fontSize = 10.sp, color = Color.White.copy(0.65f))
                }
                Column {
                    Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text(text = label, fontSize = 11.sp, color = Color.White.copy(0.7f))
                }
            }
        }
    }
}


@Composable
private fun ActivityRow(plate: String, slot: String, time: String, isEntry: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgMid.copy(alpha = 0.6f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (isEntry) Green.copy(0.2f) else Rose.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isEntry) Icons.AutoMirrored.Filled.ArrowForward else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = if (isEntry) GreenLight else Rose,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(text = plate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "Slot $slot", fontSize = 11.sp, color = TextMuted)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (isEntry) "Entry" else "Exit",
                fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                color = if (isEntry) GreenLight else Rose
            )
            Text(text = time, fontSize = 11.sp, color = TextMuted)
        }
    }
}


@Composable
private fun SectionHeader(title: String, action: String = "See all") {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = action, fontSize = 12.sp, color = Purple)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    navController: NavController,
    occupied: Int = DEFAULT_OCCUPIED,
    total: Int = DEFAULT_TOTAL
) {
    val selectedItem = remember { mutableStateOf(0) }

    // ── ViewModel & context for logout ───────────────────────────────────
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    // ─────────────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parking Dashboard",
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp,
                        style = TextStyle(brush = Brush.horizontalGradient(listOf(Purple, Green)))
                    )
                },
                actions = {
                    // ── LOGOUT BUTTON — now calls authViewModel.logout() ──
                    IconButton(onClick = {
                        authViewModel.logout(
                            navController = navController,
                            context       = context
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Rose
                        )
                    }
                    // ─────────────────────────────────────────────────────
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor   = BgDeep,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = BgCard) {
                listOf(
                    Triple(Icons.Default.Home, "Home", 0),
                    Triple(Icons.Default.AccountCircle, "Profile", 1),
                    Triple(Icons.Default.Settings, "Settings", 2)
                ).forEach { (icon, label, idx) ->
                    NavigationBarItem(
                        selected = selectedItem.value == idx,
                        onClick  = { selectedItem.value = idx },
                        icon  = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = Purple,
                            unselectedIconColor = TextMuted,
                            selectedTextColor   = Purple,
                            unselectedTextColor = TextMuted,
                            indicatorColor      = Purple.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgDeep)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // ── Hero banner ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Purple.copy(0.35f), Green.copy(0.25f)),
                            start = Offset(0f, 0f), end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
                    .border(1.dp, Purple.copy(0.3f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Good Morning 👋", fontSize = 13.sp, color = TextMuted)
                        Text(
                            "Smart Parking", fontSize = 20.sp, fontWeight = FontWeight.Black,
                            style = TextStyle(brush = Brush.horizontalGradient(listOf(PurpleLight, Cyan)))
                        )
                        Text("Today • ${occupied}/${total} slots used", fontSize = 12.sp, color = TextMuted)
                    }
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Purple.copy(0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Place, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Status + Donut ────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Parking Status", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                            style = TextStyle(brush = Brush.horizontalGradient(listOf(Purple, Green)))
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(10.dp).background(GreenLight, CircleShape))
                            Column {
                                Text("Available", fontSize = 11.sp, color = TextMuted)
                                Text("${total - occupied} Slots", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GreenLight)
                            }
                        }
                        HorizontalDivider(color = BgMid, thickness = 1.dp)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(10.dp).background(Rose, CircleShape))
                            Column {
                                Text("Occupied", fontSize = 11.sp, color = TextMuted)
                                Text("$occupied Slots", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Rose)
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.size(148.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    DonutChart(occupied = occupied, total = total, modifier = Modifier.fillMaxSize().padding(16.dp))
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Add Car / View Cars ───────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f).height(130.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color(0xFF7F5AF0), Color(0xFF5A3ED9)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Box(modifier = Modifier.size(52.dp).background(Color.White.copy(0.15f), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Add, contentDescription = "Add Car", tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Add Car", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 0.5.sp)
                            Text("Register new vehicle", fontSize = 11.sp, color = Color.White.copy(0.65f))
                        }
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).height(130.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color(0xFF2CB67D), Color(0xFF1A8A5A)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Box(modifier = Modifier.size(52.dp).background(Color.White.copy(0.15f), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "View Cars", tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("View Cars", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 0.5.sp)
                            Text("Browse all vehicles", fontSize = 11.sp, color = Color.White.copy(0.65f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Today's Stats ─────────────────────────────────────────────
            SectionHeader(title = "Today's Stats", action = "")
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStatCard("Revenue", "KES 4,200", "+12%", Icons.Filled.CheckCircle,
                    listOf(Color(0xFF7F5AF0), Color(0xFF5A3ED9)), Modifier.weight(1f))
                MiniStatCard("Check-ins", "47", "Today", Icons.AutoMirrored.Filled.ArrowForward,
                    listOf(Color(0xFF2CB67D), Color(0xFF1A7A55)), Modifier.weight(1f))
                MiniStatCard("Violations", "3", "Flagged", Icons.Default.Warning,
                    listOf(Color(0xFFE0555A), Color(0xFFB03040)), Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // ── Weekly Occupancy ──────────────────────────────────────────
            SectionHeader(title = "Weekly Occupancy", action = "This week")
            Spacer(Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(180.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BgCard),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(Modifier.size(8.dp).background(Purple, CircleShape))
                            Text("Occupancy %", fontSize = 11.sp, color = TextMuted)
                        }
                        Text("Mon – Sun", fontSize = 11.sp, color = TextMuted)
                    }
                    Spacer(Modifier.height(8.dp))
                    WeeklyBarChart(modifier = Modifier.fillMaxWidth().weight(1f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                            Text(it, fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Recent Activity ───────────────────────────────────────────
            SectionHeader(title = "Recent Activity")
            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActivityRow(plate = "KCA 123A", slot = "A-04", time = "08:42 AM", isEntry = true)
                ActivityRow(plate = "KBZ 456B", slot = "B-11", time = "09:15 AM", isEntry = false)
                ActivityRow(plate = "KDD 789C", slot = "C-02", time = "09:50 AM", isEntry = true)
                ActivityRow(plate = "KAA 321D", slot = "A-07", time = "10:03 AM", isEntry = false)
                ActivityRow(plate = "KCB 654E", slot = "B-05", time = "10:30 AM", isEntry = true)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    Dashboard(rememberNavController())
}