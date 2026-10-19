package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PlatformType

/**
 * Clean, production-grade Jetpack Compose Form Component
 * collecting video title, description, file path selection,
 * and a platform dropdown (YouTube/Facebook) to populate Room database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUploadForm(
    initialTitle: String = "",
    initialDescription: String = "",
    initialVideoPath: String = "storage/emulated/0/DCIM/Camera/my_video_1080p.mp4",
    initialPlatform: PlatformType = PlatformType.YOUTUBE,
    currentLanguage: AppLanguage = AppLanguage.URDU,
    onSaveToDatabase: (
        title: String,
        description: String,
        videoPath: String,
        platform: PlatformType,
        scheduledMillis: Long
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUrdu = currentLanguage == AppLanguage.URDU

    // Form states
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var videoPath by remember { mutableStateOf(initialVideoPath) }
    var selectedPlatform by remember { mutableStateOf(initialPlatform) }
    var scheduledMillis by remember { mutableLongStateOf(System.currentTimeMillis() + 15 * 60 * 1000L) }
    var selectedTimingPreset by remember { mutableStateOf("15_MIN") }

    // Dropdown state
    var isPlatformDropdownExpanded by remember { mutableStateOf(false) }

    // Validation & Success state
    var titleError by remember { mutableStateOf<String?>(null) }
    var pathError by remember { mutableStateOf<String?>(null) }
    var showSuccessBanner by remember { mutableStateOf(false) }

    // Android Zero-Permission Photo/Video Picker launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            videoPath = uri.toString()
            pathError = null
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("video_upload_form_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Icon + Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "Room DB",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "ویڈیو ٹاسک فارم (Room Database)" else "Video Upload Task Form",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isUrdu) "لوکل روم ڈیٹا بیس میں شیڈولنگ محفوظ کریں" else "Populate Room DB with video & platform details",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // AI Autofill Button
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF7C3AED).copy(alpha = 0.12f),
                    modifier = Modifier
                        .clickable {
                            if (isUrdu) {
                                title = "🚀 2026 میں یوٹیوب اور فیس بک سے ارننگ کا مکمل طریقہ"
                                description = "اس ویڈیو میں ہم جانیں گے کہ کیسے ویڈیوز کو آٹومیٹ کیا جائے اور زیادہ آڈینس تک پہنچا جائے۔"
                                videoPath = "storage/emulated/0/DCIM/Camera/urdu_earning_guide.mp4"
                            } else {
                                title = "🚀 How to Automate YouTube & Facebook Uploads in 2026"
                                description = "Step-by-step masterclass on scheduling, cross-publishing, and automatic reporting."
                                videoPath = "storage/emulated/0/DCIM/Camera/tech_masterclass.mp4"
                            }
                            titleError = null
                            pathError = null
                        }
                        .testTag("ai_fill_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI",
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUrdu) "آٹو لکھیں" else "AI Auto",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C3AED)
                        )
                    }
                }
            }

            // Success feedback message
            AnimatedVisibility(visible = showSuccessBanner) {
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) {
                                "ٹاسک کامیابی کے ساتھ Room ڈیٹا بیس میں محفوظ ہو گیا ہے!"
                            } else {
                                "Task successfully persisted to Room Database!"
                            },
                            color = Color(0xFF047857),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // 1. Video Title Field
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = null
                    },
                    label = {
                        Text(if (isUrdu) "ویڈیو کا عنوان (Video Title) *" else "Video Title *")
                    },
                    placeholder = {
                        Text(
                            if (isUrdu) "مثال: یوٹیوب پر ویوز کیسے بڑھائیں" else "e.g. 5 Secrets to Skyrocket Video Views",
                            fontSize = 13.sp
                        )
                    },
                    isError = titleError != null,
                    supportingText = {
                        if (titleError != null) {
                            Text(text = titleError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text(text = "${title.length} / 100", fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // 2. Video Description Field
            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(if (isUrdu) "ویڈیو کی تفصیل (Description)" else "Video Description")
                    },
                    placeholder = {
                        Text(
                            if (isUrdu) "ویڈیو کے بارے میں مختصر وضاحت، ہیش ٹیگز یا نوٹس درج کریں..." else "Enter video summary, key takeaways, and hashtags...",
                            fontSize = 13.sp
                        )
                    },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_description_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // 3. File Path Selection
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isUrdu) "ویڈیو فائل پاتھ (File Path Selection) *" else "Video File Path Selection *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = videoPath,
                    onValueChange = {
                        videoPath = it
                        if (it.isNotBlank()) pathError = null
                    },
                    placeholder = { Text("e.g. /storage/emulated/0/DCIM/video.mp4") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        // Browse / Android Photo & Video Picker button
                        IconButton(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            modifier = Modifier.testTag("browse_video_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = "Browse",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    isError = pathError != null,
                    supportingText = {
                        if (pathError != null) {
                            Text(text = pathError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text(
                                text = if (isUrdu) "گیلری سے منتخب کریں یا لوکل پاتھ درج کریں" else "Browse device media or enter custom storage path",
                                fontSize = 11.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_path_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                // Quick Preset Chips for convenience
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickPathChip(
                        label = "Camera / DCIM",
                        onClick = {
                            videoPath = "storage/emulated/0/DCIM/Camera/my_camera_recording.mp4"
                            pathError = null
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPathChip(
                        label = "Movies / Reels",
                        onClick = {
                            videoPath = "storage/emulated/0/Movies/facebook_reel_export.mp4"
                            pathError = null
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPathChip(
                        label = "Downloads",
                        onClick = {
                            videoPath = "storage/emulated/0/Download/edited_tech_clip.mp4"
                            pathError = null
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 4. Platform Dropdown (YouTube / Facebook / Both)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isUrdu) "ٹارگٹ پلیٹ فارم (Platform Dropdown) *" else "Target Platform Dropdown *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                ExposedDropdownMenuBox(
                    expanded = isPlatformDropdownExpanded,
                    onExpandedChange = { isPlatformDropdownExpanded = !isPlatformDropdownExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("platform_dropdown")
                ) {
                    OutlinedTextField(
                        value = when (selectedPlatform) {
                            PlatformType.YOUTUBE -> if (isUrdu) "یوٹیوب (YouTube Channel)" else "YouTube"
                            PlatformType.FACEBOOK -> if (isUrdu) "فیس بک پیج (Facebook Page)" else "Facebook Page"
                            PlatformType.BOTH -> if (isUrdu) "دونوں پلیٹ فارمز (YouTube + Facebook)" else "Both (YouTube & Facebook)"
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlatformDropdownExpanded) },
                        leadingIcon = {
                            when (selectedPlatform) {
                                PlatformType.YOUTUBE -> Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "YouTube",
                                    tint = Color(0xFFFF0033),
                                    modifier = Modifier.size(20.dp)
                                )
                                PlatformType.FACEBOOK -> Text(
                                    text = "f",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1877F2),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                                PlatformType.BOTH -> Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF6366F1))
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = isPlatformDropdownExpanded,
                        onDismissRequest = { isPlatformDropdownExpanded = false }
                    ) {
                        // Option 1: YouTube
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFFFF0033),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "YT",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (isUrdu) "یوٹیوب (YouTube Channel)" else "YouTube Channel",
                                        fontWeight = if (selectedPlatform == PlatformType.YOUTUBE) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                selectedPlatform = PlatformType.YOUTUBE
                                isPlatformDropdownExpanded = false
                            },
                            modifier = Modifier.testTag("dropdown_item_youtube")
                        )

                        // Option 2: Facebook
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFF1877F2),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "FB",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (isUrdu) "فیس بک پیج (Facebook Page)" else "Facebook Page",
                                        fontWeight = if (selectedPlatform == PlatformType.FACEBOOK) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                selectedPlatform = PlatformType.FACEBOOK
                                isPlatformDropdownExpanded = false
                            },
                            modifier = Modifier.testTag("dropdown_item_facebook")
                        )

                        // Option 3: Both
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFF6366F1),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ALL",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (isUrdu) "دونوں پلیٹ فارمز (YouTube + Facebook)" else "Both (YouTube & Facebook)",
                                        fontWeight = if (selectedPlatform == PlatformType.BOTH) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                selectedPlatform = PlatformType.BOTH
                                isPlatformDropdownExpanded = false
                            },
                            modifier = Modifier.testTag("dropdown_item_both")
                        )
                    }
                }
            }

            // 5. Scheduling Timing Presets
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (isUrdu) "شیڈول کا وقت (Schedule Timing)" else "Schedule Timing",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimingChip(
                        label = if (isUrdu) "15 منٹ بعد" else "In 15 Mins",
                        selected = selectedTimingPreset == "15_MIN",
                        onClick = {
                            selectedTimingPreset = "15_MIN"
                            scheduledMillis = System.currentTimeMillis() + 15 * 60 * 1000L
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TimingChip(
                        label = if (isUrdu) "1 گھنٹہ بعد" else "In 1 Hour",
                        selected = selectedTimingPreset == "1_HR",
                        onClick = {
                            selectedTimingPreset = "1_HR"
                            scheduledMillis = System.currentTimeMillis() + 60 * 60 * 1000L
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TimingChip(
                        label = if (isUrdu) "آج شام 8 بجے" else "Tonight 8 PM",
                        selected = selectedTimingPreset == "TONIGHT",
                        onClick = {
                            selectedTimingPreset = "TONIGHT"
                            scheduledMillis = System.currentTimeMillis() + 4 * 3600 * 1000L
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Submit Button: Save to Room Database
            Button(
                onClick = {
                    var hasError = false
                    if (title.isBlank()) {
                        titleError = if (isUrdu) "برائے مہربانی ویڈیو کا عنوان درج کریں" else "Title cannot be empty"
                        hasError = true
                    }
                    if (videoPath.isBlank()) {
                        pathError = if (isUrdu) "برائے مہربانی ویڈیو پاتھ منتخب کریں" else "File path is required"
                        hasError = true
                    }

                    if (!hasError) {
                        onSaveToDatabase(
                            title.trim(),
                            description.trim(),
                            videoPath.trim(),
                            selectedPlatform,
                            scheduledMillis
                        )
                        showSuccessBanner = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_to_room_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isUrdu) "Room ڈیٹا بیس میں محفوظ کریں" else "Save to Room Database",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickPathChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TimingChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}
