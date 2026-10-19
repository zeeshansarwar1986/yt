package com.example.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.model.AppLanguage
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WhatsAppReportGenerator {

    fun generateReportMessage(
        task: ScheduledTask,
        language: AppLanguage = AppLanguage.URDU,
        recipientName: String = "ایڈمن صاحب / Admin"
    ): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = sdf.format(Date(task.scheduledTimeMillis))
        val isUrdu = language == AppLanguage.URDU

        return if (isUrdu) {
            buildString {
                append("📢 *آٹو پوسٹر اپلوڈ رپورٹ*\n")
                append("────────────────────\n")
                append("محترم $recipientName!\n")
                append("آپ کا شیڈول کردہ ویڈیو ٹاسک کامیابی سے اپلوڈ ہو چکا ہے۔\n\n")
                append("🎬 *عنوان:* ${task.title}\n")
                append("📂 *کیٹگری:* ${task.category}\n")
                append("⏱️ *شیڈول شدہ وقت:* $formattedDate\n")
                append("📊 *سٹیٹس:* ✅ ${task.status.labelUrdu}\n")
                append("📡 *پلیٹ فارمز:* ${task.platform.displayNameUrdu}\n\n")
                
                if (task.platform == PlatformType.YOUTUBE || task.platform == PlatformType.BOTH) {
                    append("▶️ *یوٹیوب لنک:* ${task.youtubeVideoUrl ?: "https://youtu.be/yt_${task.id.take(8)}"}\n")
                }
                if (task.platform == PlatformType.FACEBOOK || task.platform == PlatformType.BOTH) {
                    append("🌐 *فیس بک پیج لنک:* ${task.facebookPostUrl ?: "https://facebook.com/watch/?v=fb_${task.id.take(8)}"}\n")
                }
                
                append("\n🏷️ *ٹیگز:* ${task.tags.joinToString(" ") { "#$it" }}\n")
                append("────────────────────\n")
                append("🤖 _یہ ایک خودکار رپورٹ ہے جو آپ کے مقررہ وقت کے مطابق تیار کی گئی ہے۔_")
            }
        } else {
            buildString {
                append("📢 *AutoPoster Execution Report*\n")
                append("────────────────────\n")
                append("Hello Admin!\n")
                append("Your scheduled video upload has completed successfully.\n\n")
                append("🎬 *Title:* ${task.title}\n")
                append("📂 *Category:* ${task.category}\n")
                append("⏱️ *Scheduled At:* $formattedDate\n")
                append("📊 *Status:* ✅ ${task.status.label}\n")
                append("📡 *Platforms:* ${task.platform.displayName}\n\n")

                if (task.platform == PlatformType.YOUTUBE || task.platform == PlatformType.BOTH) {
                    append("▶️ *YouTube URL:* ${task.youtubeVideoUrl ?: "https://youtu.be/yt_${task.id.take(8)}"}\n")
                }
                if (task.platform == PlatformType.FACEBOOK || task.platform == PlatformType.BOTH) {
                    append("🌐 *Facebook URL:* ${task.facebookPostUrl ?: "https://facebook.com/watch/?v=fb_${task.id.take(8)}"}\n")
                }

                append("\n🏷️ *Tags:* ${task.tags.joinToString(" ") { "#$it" }}\n")
                append("────────────────────\n")
                append("🤖 _Automated dispatch from AutoPoster Engine._")
            }
        }
    }

    fun openWhatsApp(context: Context, rawPhoneNumber: String, message: String): Boolean {
        return try {
            val cleanPhone = rawPhoneNumber.replace("+", "").replace(" ", "").replace("-", "")
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
