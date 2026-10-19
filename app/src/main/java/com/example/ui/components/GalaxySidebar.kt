package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppLanguage
import com.example.ui.AppTab
import com.example.ui.MainViewModel

/**
 * Galaxy Edge Floating Handle + Futuristic Galaxy Side Navigation Drawer
 */
@Composable
fun GalaxySidebarContainer(
    viewModel: MainViewModel,
    isOpen: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isSchedulerRunning by viewModel.isSchedulerRunning.collectAsState()
    val tasks by viewModel.allTasks.collectAsState()
    val context = LocalContext.current

    val neonGalaxyBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6366F1), // Indigo
            Color(0xFFA855F7), // Nebula Purple
            Color(0xFF06B6D4), // Cyan Starlight
            Color(0xFFEC4899)  // Deep Cosmos
        )
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Main Screen Content
        content()

        // Samsung Galaxy Edge Style Floating Handle (Right edge)
        if (!isOpen) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 0.dp)
                    .zIndex(20f)
            ) {
                Surface(
                    onClick = { onOpenChange(true) },
                    modifier = Modifier
                        .height(76.dp)
                        .width(28.dp)
                        .shadow(12.dp, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                        .border(
                            1.5.dp,
                            neonGalaxyBrush,
                            RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        )
                        .testTag("galaxy_edge_handle"),
                    color = Color(0xFF13172E).copy(alpha = 0.92f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Glowing dots and arrow icon
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF38BDF8))
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Open Galaxy Edge Sidebar",
                            tint = Color(0xFFA5B4FC),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEC4899))
                        )
                    }
                }
            }
        }

        // Galaxy Drawer Backdrop (Scrim)
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(animationSpec = tween(250)),
            exit = fadeOut(animationSpec = tween(250)),
            modifier = Modifier.zIndex(25f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onOpenChange(false) }
            )
        }

        // Sliding Galaxy Edge Panel
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(250, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .widthIn(max = 330.dp)
                .fillMaxWidth(0.85f)
                .zIndex(30f)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .shadow(24.dp, RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
                    .border(
                        1.5.dp,
                        neonGalaxyBrush,
                        RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp)),
                color = Color(0xFF0C1021)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF141935),
                                    Color(0xFF1D153B),
                                    Color(0xFF090D1A)
                                )
                            )
                        )
                ) {
                    // Galaxy Header
                    GalaxySidebarHeader(
                        isUrdu = isUrdu,
                        onClose = { onOpenChange(false) },
                        onToggleLanguage = { viewModel.toggleLanguage() }
                    )

                    Divider(color = Color(0xFF334155).copy(alpha = 0.5f))

                    // Scrollable Navigation Items & Tools
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        // Section: Galaxy Navigation
                        Text(
                            text = if (isUrdu) "🌌 گلیکسی نیویگیشن" else "🌌 GALAXY NAVIGATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA5B4FC),
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 4.dp)
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.Schedule,
                            label = StringsDictionary.get("tab_queue", currentLang),
                            badge = "${tasks.count { it.status == com.example.model.UploadStatus.SCHEDULED }}",
                            isSelected = selectedTab == AppTab.QUEUE,
                            onClick = {
                                viewModel.selectTab(AppTab.QUEUE)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.History,
                            label = StringsDictionary.get("tab_history", currentLang),
                            badge = "${tasks.count { it.status == com.example.model.UploadStatus.COMPLETED || it.status == com.example.model.UploadStatus.FAILED }}",
                            isSelected = selectedTab == AppTab.HISTORY,
                            onClick = {
                                viewModel.selectTab(AppTab.HISTORY)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.AddCircle,
                            label = StringsDictionary.get("tab_new_task", currentLang),
                            badge = "+",
                            isSelected = selectedTab == AppTab.NEW_TASK,
                            onClick = {
                                viewModel.selectTab(AppTab.NEW_TASK)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.Link,
                            label = StringsDictionary.get("tab_accounts", currentLang),
                            badge = "2",
                            isSelected = selectedTab == AppTab.ACCOUNTS,
                            onClick = {
                                viewModel.selectTab(AppTab.ACCOUNTS)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.Chat,
                            label = StringsDictionary.get("tab_whatsapp", currentLang),
                            badge = "LIVE",
                            accentColor = Color(0xFF25D366),
                            isSelected = selectedTab == AppTab.WHATSAPP,
                            onClick = {
                                viewModel.selectTab(AppTab.WHATSAPP)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.Lightbulb,
                            label = StringsDictionary.get("tab_analytics", currentLang),
                            badge = "AI",
                            accentColor = Color(0xFFF59E0B),
                            isSelected = selectedTab == AppTab.ANALYTICS,
                            onClick = {
                                viewModel.selectTab(AppTab.ANALYTICS)
                                onOpenChange(false)
                            }
                        )

                        GalaxyNavItem(
                            icon = Icons.Default.Info,
                            label = if (isUrdu) "ڈویلپر (M Zeeshan Sarwar)" else "About M Zeeshan Sarwar",
                            badge = "★",
                            accentColor = Color(0xFF38BDF8),
                            isSelected = selectedTab == AppTab.ABOUT,
                            onClick = {
                                viewModel.selectTab(AppTab.ABOUT)
                                onOpenChange(false)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color(0xFF334155).copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Galaxy Engine Toggle
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF4F46E5).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                            color = Color(0xFF161B33).copy(alpha = 0.8f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSchedulerRunning) Color(0xFF10B981).copy(alpha = 0.2f)
                                                else Color(0xFF64748B).copy(alpha = 0.2f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PowerSettingsNew,
                                            contentDescription = null,
                                            tint = if (isSchedulerRunning) Color(0xFF10B981) else Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (isUrdu) "آٹو اپلوڈر انجن" else "Auto Uploader Engine",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (isSchedulerRunning) {
                                                if (isUrdu) "🟢 فعال ہے" else "🟢 Active"
                                            } else {
                                                if (isUrdu) "⚪ موقوف ہے" else "⚪ Paused"
                                            },
                                            fontSize = 10.sp,
                                            color = if (isSchedulerRunning) Color(0xFF10B981) else Color(0xFF94A3B8)
                                        )
                                    }
                                }

                                Switch(
                                    checked = isSchedulerRunning,
                                    onCheckedChange = { viewModel.toggleScheduler() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF10B981),
                                        uncheckedThumbColor = Color(0xFF94A3B8),
                                        uncheckedTrackColor = Color(0xFF334155)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Developer Contact Card
                        DeveloperMiniCard(
                            isUrdu = isUrdu,
                            onOpenAbout = {
                                viewModel.selectTab(AppTab.ABOUT)
                                onOpenChange(false)
                            }
                        )
                    }

                    // Bottom Edge App info
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF090D1A)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "AutoPoster Pro v2.4",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "M Zeeshan Sarwar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA5B4FC)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GalaxySidebarHeader(
    isUrdu: Boolean,
    onClose: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFF0033), Color(0xFF8B5CF6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AutoPoster Hub",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = if (isUrdu) "گلیکسی ایڈیشن • سائیڈ بار" else "Galaxy Edition • Sidebar",
                            fontSize = 11.sp,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Sidebar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bilingual Toggle in Header
            Surface(
                onClick = onToggleLanguage,
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) "🌐 زبان تبدیل کریں:" else "🌐 Switch Language:",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = if (isUrdu) "English میں بدلیں 🔄" else "اردو میں بدلیں 🔄",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                }
            }
        }
    }
}

@Composable
private fun GalaxyNavItem(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    isSelected: Boolean = false,
    accentColor: Color = Color(0xFF6366F1),
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        accentColor.copy(alpha = 0.22f)
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        Color.White
    } else {
        Color(0xFFCBD5E1)
    }

    val iconTint = if (isSelected) {
        if (accentColor == Color(0xFF6366F1)) Color(0xFF38BDF8) else accentColor
    } else {
        Color(0xFF94A3B8)
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) accentColor.copy(alpha = 0.6f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontSize = 13.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!badge.isNullOrEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) accentColor else Color(0xFF1E293B)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DeveloperMiniCard(
    isUrdu: Boolean,
    onOpenAbout: () -> Unit
) {
    val context = LocalContext.current
    val phone = "+9223007487676"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4))),
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141935).copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ZS",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        // English name as required
                        Text(
                            text = "M Zeeshan Sarwar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Text(
                            text = if (isUrdu) "ایم زیشان سرور • ڈویلپر" else "Developer & Architect",
                            fontSize = 10.sp,
                            color = Color(0xFFA5B4FC)
                        )
                    }
                }

                Surface(
                    onClick = onOpenAbout,
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4F46E5).copy(alpha = 0.3f)
                ) {
                    Text(
                        text = if (isUrdu) "تفصیل" else "Info",
                        fontSize = 10.sp,
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = phone,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF38BDF8)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quick WhatsApp Button
                Surface(
                    onClick = {
                        try {
                            val url = "https://wa.me/9223007487676"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, phone, Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF25D366),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "WhatsApp",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Quick Call Button
                Surface(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, phone, Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUrdu) "کال" else "Call",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
