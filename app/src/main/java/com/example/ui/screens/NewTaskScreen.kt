package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.components.VideoUploadForm
import java.util.UUID

@Composable
fun NewTaskScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    val whatsappSettings by viewModel.whatsappSettings.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = if (isUrdu) "نیا ویڈیو ٹاسک شیڈول کریں" else "Schedule New Video Upload",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isUrdu) {
                        "ویڈیو کا عنوان، تفصیل، فائل پاتھ منتخب کریں اور یوٹیوب یا فیس بک چن کر لوکل Room ڈیٹا بیس میں محفوظ کریں۔"
                    } else {
                        "Collect video title, description, file path, and platform dropdown to populate Room database."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // The Clean VideoUploadForm Compose UI Component
        item {
            VideoUploadForm(
                initialTitle = "",
                initialDescription = "",
                initialVideoPath = "storage/emulated/0/DCIM/Camera/my_video_1080p.mp4",
                initialPlatform = PlatformType.YOUTUBE,
                currentLanguage = currentLang,
                onSaveToDatabase = { title, desc, path, platform, scheduledMillis ->
                    val task = ScheduledTask(
                        id = "task_${UUID.randomUUID().toString().take(8)}",
                        title = title,
                        description = desc.ifEmpty { "Auto-uploaded video via AutoPoster Hub." },
                        tags = listOf("Video", "SocialMedia", platform.name),
                        category = "Entertainment",
                        platform = platform,
                        videoFileName = path,
                        videoFileSizeMb = 48.0,
                        videoDuration = "05:00",
                        privacy = VideoPrivacy.PUBLIC,
                        scheduledTimeMillis = scheduledMillis,
                        status = UploadStatus.SCHEDULED,
                        progressPercent = 0
                    )
                    viewModel.repository.scheduleNewTask(task)
                }
            )
        }

        // WhatsApp Automatic Dispatch Reminder Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF25D366).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isUrdu) {
                            "شیڈول وقت پر ویڈیو اپلوڈ ہوتے ہی رپورٹ آپ کے وٹس ایپ نمبر (${whatsappSettings.phoneNumber}) پر خودکار ارسال ہوگی۔"
                        } else {
                            "When the scheduled time arrives, automated video publishing and WhatsApp notification dispatch to ${whatsappSettings.phoneNumber} will trigger."
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF166534),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
