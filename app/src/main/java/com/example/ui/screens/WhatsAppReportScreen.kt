package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WhatsAppReportGenerator
import com.example.model.AppLanguage
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WhatsAppReportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    val whatsappSettings by viewModel.whatsappSettings.collectAsState()
    val logs by viewModel.reportLogs.collectAsState()
    val allTasks by viewModel.repository.tasks.collectAsState()

    var phoneInput by remember(whatsappSettings.phoneNumber) { mutableStateOf(whatsappSettings.phoneNumber) }
    var autoSend by remember(whatsappSettings.autoSendAfterUpload) { mutableStateOf(whatsappSettings.autoSendAfterUpload) }
    var langUrdu by remember(whatsappSettings.languageUrdu) { mutableStateOf(whatsappSettings.languageUrdu) }

    val latestCompletedTask = allTasks.firstOrNull { it.status == UploadStatus.COMPLETED } ?: allTasks.firstOrNull()
    val samplePreviewMessage = if (latestCompletedTask != null) {
        WhatsAppReportGenerator.generateReportMessage(
            latestCompletedTask,
            if (langUrdu) AppLanguage.URDU else AppLanguage.ENGLISH
        )
    } else {
        if (isUrdu) "📢 آٹو پوسٹر: فی الوقت کوئی اپلوڈ شدہ ویڈیو موجود نہیں۔" else "📢 AutoPoster: No uploaded tasks yet."
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column {
                Text(
                    text = if (isUrdu) "وٹس ایپ رپورٹنگ سنٹر" else "WhatsApp Reporting Hub",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isUrdu) {
                        "ہر شیڈول ویڈیو اپلوڈ ہونے کے بعد یہ ایپ آپ کو براہ راست وٹس ایپ پر تفصیلی رپورٹ بھیجتی ہے۔"
                    } else {
                        "Receive instant, automated reports on your WhatsApp as soon as any scheduled video is published."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // WhatsApp Configuration Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("whatsapp_config_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "WhatsApp",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isUrdu) "رپورٹ وصول کرنے کا نمبر" else "Report Recipient Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        // Status
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isUrdu) "فعال ہے" else "Active",
                                color = Color(0xFF047857),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = {
                            phoneInput = it
                            viewModel.updateWhatsAppConfig(it, autoSend, langUrdu, true)
                        },
                        label = { Text(if (isUrdu) "وٹس ایپ موبائل نمبر (Country Code کے ساتھ)" else "WhatsApp Mobile Number (e.g. +923001234567)") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFF25D366))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("whatsapp_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Auto-send Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isUrdu) "خودکار رپورٹ بھیجیں" else "Auto-dispatch after upload",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isUrdu) "ویڈیو اپلوڈ مکمل ہوتے ہی نوٹیفکیشن ارسال ہوگا" else "Sends automated notification as soon as video is live",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = autoSend,
                            onCheckedChange = {
                                autoSend = it
                                viewModel.updateWhatsAppConfig(phoneInput, it, langUrdu, true)
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF25D366))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Urdu message format toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isUrdu) "اردو زبان میں رپورٹ کا فارمیٹ" else "Format Report in Urdu",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isUrdu) "رپورٹ اردو متن اور ایموجیز کے ساتھ بنے گی" else "Formatted in fluent Urdu with metadata & emojis",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = langUrdu,
                            onCheckedChange = {
                                langUrdu = it
                                viewModel.updateWhatsAppConfig(phoneInput, autoSend, it, true)
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF25D366))
                        )
                    }
                }
            }
        }

        // WhatsApp Live Mockup Preview
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isUrdu) "وٹس ایپ میسج کا لائیو پیش منظر (Live Preview)" else "Live WhatsApp Message Preview",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Realistic WhatsApp Chat Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFECE5DD) // classic WhatsApp chat background
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // WhatsApp Chat Top Bar Mockup
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF075E54))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "AP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "AutoPoster Bot 🤖",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "online",
                                    color = Color(0xFFD4E7C5),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // WhatsApp Message Bubble
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 2.dp,
                                    bottomEnd = 12.dp
                                ),
                                color = Color(0xFFE7FFDB), // WhatsApp sent message green bubble
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = samplePreviewMessage,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp,
                                        color = Color(0xFF111827),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.align(Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Just now",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.DoneAll,
                                            contentDescription = "Read",
                                            tint = Color(0xFF34B7F1),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Instant Send Button
                Button(
                    onClick = { viewModel.sendDirectTestReport(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("send_whatsapp_report_now_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isUrdu) "ابھی وٹس ایپ پر رپورٹ بھیجیں" else "Send Report via WhatsApp Now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // WhatsApp Report Log Audit Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isUrdu) "بھیجی گئی رپورٹس کی ہسٹری (${logs.size})" else "Dispatched Reports History (${logs.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(logs, key = { it.id }) { log ->
            WhatsAppLogItem(log = log, isUrdu = isUrdu)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WhatsAppLogItem(
    log: com.example.model.WhatsAppReportLog,
    isUrdu: Boolean
) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedTime = sdf.format(Date(log.timestampMillis))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF25D366)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = log.platformsSummary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = formattedTime,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "🎬 ${log.videoTitle}",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "To: ${log.recipientPhone}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = log.status,
                    fontSize = 11.sp,
                    color = Color(0xFF10B981),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
