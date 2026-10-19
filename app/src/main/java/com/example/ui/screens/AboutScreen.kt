package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.components.StringsDictionary

@Composable
fun AboutScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    val isSchedulerRunning by viewModel.isSchedulerRunning.collectAsState()
    val tasks by viewModel.allTasks.collectAsState()
    val context = LocalContext.current

    var isCopied by remember { mutableStateOf(false) }
    val phoneNumber = "+9223007487676"
    val developerNameEnglish = "M Zeeshan Sarwar"
    val developerNameUrdu = "ایم زیشان سرور"
    val developerEmail = "zeshansarwar1986@gmail.com"

    val galaxyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B4B),
            Color(0xFF311042),
            Color(0xFF0B0F19)
        )
    )

    val neonBorderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFFA855F7),
            Color(0xFF06B6D4),
            Color(0xFFEC4899)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(galaxyGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Galaxy Hero Developer Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, neonBorderBrush, RoundedCornerShape(24.dp))
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF13172E).copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Galaxy Glow Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(3.dp, neonBorderBrush, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6),
                                        Color(0xFF4F46E5),
                                        Color(0xFF0F172A)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ZS",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Developer Name in English (Mandatory requirement)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = developerNameEnglish,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Urdu Name Subtitle
                    Text(
                        text = developerNameUrdu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA5B4FC),
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Role / Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF4F46E5).copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF818CF8))
                    ) {
                        Text(
                            text = if (isUrdu) "چیف سافٹ ویئر آرکیٹیکٹ و ڈویلپر" else "Lead Software Architect & Developer",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE0E7FF),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Divider(color = Color(0xFF334155).copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(18.dp))

                    // Phone Number Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                        color = Color(0xFF1E293B).copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0284C7).copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Phone",
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isUrdu) "موبائل / وٹس ایپ نمبر" else "Mobile / WhatsApp",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                    Text(
                                        text = phoneNumber,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Developer Phone", phoneNumber)
                                    clipboard.setPrimaryClip(clip)
                                    isCopied = true
                                    Toast.makeText(
                                        context,
                                        if (isUrdu) "نمبر کاپی ہو گیا: $phoneNumber" else "Copied: $phoneNumber",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.testTag("btn_copy_phone")
                            ) {
                                Icon(
                                    imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = if (isCopied) Color(0xFF10B981) else Color(0xFF38BDF8)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons (WhatsApp & Call)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // WhatsApp Direct Button
                        Button(
                            onClick = {
                                try {
                                    val url = "https://wa.me/9223007487676?text=" + Uri.encode(
                                        if (isUrdu) "السلام علیکم زیشان صاحب، میں آٹو پوسٹر ایپ کے بارے میں رابطہ کر رہا ہوں۔"
                                        else "Hello M Zeeshan Sarwar, I am reaching out regarding the AutoPoster App."
                                    )
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp not available", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_about_whatsapp"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "وٹس ایپ چیٹ" else "WhatsApp",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }

                        // Direct Call Button
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Dialer unavailable", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_about_call"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "براہ راست کال" else "Call Now",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Email Row
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:$developerEmail")
                                        putExtra(Intent.EXTRA_SUBJECT, "AutoPoster App Feedback")
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, developerEmail, Toast.LENGTH_SHORT).show()
                                }
                            },
                        color = Color(0xFF1E293B).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color(0xFFA855F7),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = developerEmail,
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Architecture & Specs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.85f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6366F1).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF818CF8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AutoPoster Pro (Galaxy Edition)",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isUrdu) "ورژن 2.4 - جدید خودکار ویڈیو اپلوڈر" else "Version 2.4 - Modern Video Auto-Publisher",
                                fontSize = 11.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = StringsDictionary.get("about_app_desc", currentLang),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Feature highlights grid
                    FeatureItem(
                        icon = Icons.Default.Storage,
                        iconTint = Color(0xFF38BDF8),
                        title = if (isUrdu) "لوکل روم ڈیٹا بیس (Room DB)" else "Room SQLite Local Database",
                        desc = if (isUrdu) "تمام ٹاسکس اور ہسٹری مستقل محفوظ رہتی ہے۔" else "Zero-loss task persistence and local auditing."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FeatureItem(
                        icon = Icons.Default.Star,
                        iconTint = Color(0xFFF59E0B),
                        title = if (isUrdu) "یوٹیوب اور فیس بک خودکار سپورٹ" else "YouTube & Facebook Multi-Platform",
                        desc = if (isUrdu) "شیڈولڈ وقت پر خودکار پبلشنگ اور ٹیگز سیٹنگز۔" else "Simultaneous automated dispatch with tags & privacy."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FeatureItem(
                        icon = Icons.Default.Chat,
                        iconTint = Color(0xFF10B981),
                        title = if (isUrdu) "وٹس ایپ انسٹنٹ رپورٹنگ" else "Automated WhatsApp Reporting Hub",
                        desc = if (isUrdu) "ہر ویڈیو مکمل ہونے پر خودکار رپورٹ جنریٹ۔" else "Real-time dispatch log and instant status delivery."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FeatureItem(
                        icon = Icons.Default.Security,
                        iconTint = Color(0xFFA855F7),
                        title = if (isUrdu) "محفوظ اور خودمختار آرکیٹیکچر" else "Privacy-First Architecture",
                        desc = if (isUrdu) "کریڈینشلز اور ڈیٹا ڈیوائس پر محفوظ رہتے ہیں۔" else "Direct client architecture with local storage."
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // System Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141A29))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${tasks.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Text(
                            text = if (isUrdu) "کل ٹاسکس" else "Total Tasks",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color(0xFF334155))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSchedulerRunning) "ONLINE" else "PAUSED",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSchedulerRunning) Color(0xFF10B981) else Color(0xFFF59E0B)
                        )
                        Text(
                            text = if (isUrdu) "شیڈولر انجن" else "Scheduler Engine",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color(0xFF334155))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isUrdu) "اردو / ENG" else "ENG / اردو",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA855F7)
                        )
                        Text(
                            text = if (isUrdu) "دو لسانی" else "Bilingual",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "© 2026 M Zeeshan Sarwar • All Rights Reserved",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(17.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.White
            )
            Text(
                text = desc,
                fontSize = 11.5.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 16.sp
            )
        }
    }
}
