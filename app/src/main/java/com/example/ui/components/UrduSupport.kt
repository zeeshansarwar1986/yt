package com.example.ui.components

import com.example.model.AppLanguage

object StringsDictionary {
    fun get(key: String, language: AppLanguage): String {
        val isUrdu = language == AppLanguage.URDU
        return when (key) {
            // App Header
            "app_title" -> if (isUrdu) "آٹو پوسٹر مینیجر" else "AutoPoster Hub"
            "app_subtitle" -> if (isUrdu) "یوٹیوب اور فیس بک آٹو اپلوڈر و وٹس ایپ رپورٹر" else "YouTube & Facebook Auto-Publisher with WhatsApp Reports"
            "engine_active" -> if (isUrdu) "🟢 آٹو اپلوڈر فعال ہے" else "🟢 Auto-Upload Active"
            "engine_paused" -> if (isUrdu) "⚪ سروس موقوف ہے" else "⚪ Auto-Upload Paused"
            "switch_lang" -> if (isUrdu) "English میں دیکھیں" else "اردو میں دیکھیں"
            
            // Tabs
            "tab_queue" -> if (isUrdu) "شیڈولر" else "Queue"
            "tab_history" -> if (isUrdu) "ہسٹری" else "History"
            "tab_new_task" -> if (isUrdu) "نیا ٹاسک" else "New Task"
            "tab_accounts" -> if (isUrdu) "کنکشنز" else "Accounts"
            "tab_whatsapp" -> if (isUrdu) "وٹس ایپ رپورٹ" else "WhatsApp"
            "tab_analytics" -> if (isUrdu) "تجزیات و آئیڈیاز" else "Insights"

            // Status counts
            "scheduled_count" -> if (isUrdu) "شیڈول شدہ" else "Scheduled"
            "in_progress" -> if (isUrdu) "اپلوڈنگ جاری" else "Uploading"
            "completed" -> if (isUrdu) "مکمل شدہ" else "Completed"
            "failed" -> if (isUrdu) "ناکام ٹاسکس" else "Failed"

            // Queue Screen
            "queue_title" -> if (isUrdu) "اپلوڈ کی فہرست اور شیڈول" else "Video Queue & Schedule"
            "videos_count" -> if (isUrdu) "ویڈیوز" else "videos"
            "filter_all" -> if (isUrdu) "تمام ٹاسکس" else "All"
            "filter_scheduled" -> if (isUrdu) "شیڈولڈ" else "Scheduled"
            "filter_completed" -> if (isUrdu) "مکمل شدہ" else "Completed"
            "search_placeholder" -> if (isUrdu) "ویڈیو یا عنوان تلاش کریں..." else "Search scheduled videos or tags..."
            "empty_title" -> if (isUrdu) "کوئی شیڈول ویڈیو موجود نہیں" else "No Scheduled Videos"
            "empty_desc" -> if (isUrdu) "آپ 'نیا ٹاسک' پر کلک کر کے یوٹیوب یا فیس بک کیلئے ویڈیو شیڈول کر سکتے ہیں۔" else "Schedule videos to auto-publish to YouTube & Facebook with automated WhatsApp updates."

            // New Task Form
            "new_task_heading" -> if (isUrdu) "نیا ویڈیو ٹاسک شیڈول کریں" else "Schedule New Video Upload"
            "new_task_subheading" -> if (isUrdu) "ویڈیو سیٹ کریں، پلیٹ فارم چنیں اور مقررہ وقت پر آٹومیٹک پبلش ہو جائے گی۔" else "Configure video metadata, target platforms, and automated WhatsApp delivery."
            "video_title" -> if (isUrdu) "ویڈیو کا عنوان (Video Title) *" else "Video Title *"
            "video_title_placeholder" -> if (isUrdu) "مثال: یوٹیوب پر ویوز کیسے بڑھائیں" else "e.g. 5 Secrets to Skyrocket Video Views"
            "video_description" -> if (isUrdu) "تفصیل (Description)" else "Video Description"
            "video_description_placeholder" -> if (isUrdu) "ویڈیو کے بارے میں مختصر وضاحت، ہیش ٹیگز یا نوٹس درج کریں..." else "Enter video summary, key takeaways, and hashtags..."
            "video_path_label" -> if (isUrdu) "ویڈیو فائل پاتھ (File Path Selection) *" else "Video File Path Selection *"
            "video_path_hint" -> if (isUrdu) "گیلری سے منتخب کریں یا لوکل پاتھ درج کریں" else "Browse device media or enter custom storage path"
            "select_platform" -> if (isUrdu) "ٹارگٹ پلیٹ فارم (Platform Dropdown) *" else "Target Platform Dropdown *"
            "schedule_time" -> if (isUrdu) "شیڈول کا وقت (Schedule Timing)" else "Schedule Timing"
            "privacy_setting" -> if (isUrdu) "پرائیویسی:" else "Privacy Setting:"
            "btn_schedule_now" -> if (isUrdu) "Room ڈیٹا بیس میں محفوظ کریں" else "Save to Room Database"
            "btn_upload_immediate" -> if (isUrdu) "ابھی فوری اپلوڈ کریں" else "Upload Immediately"
            "btn_ai_generate" -> if (isUrdu) "✨ AI سے عنوان اور ٹیگز تیار کریں" else "✨ Auto-Generate Viral Title & Tags"
            "ai_banner_title" -> if (isUrdu) "سمارٹ AI مواد جنریٹر" else "AI Metadata Generator"
            "ai_banner_desc" -> if (isUrdu) "وائرل عنوان، تفصیل اور ٹیگز ایک کلک پر حاصل کریں" else "Generate high-CTR titles and hashtags instantly"

            // WhatsApp section
            "whatsapp_title" -> if (isUrdu) "وٹس ایپ رپورٹنگ سنٹر" else "WhatsApp Reporting Hub"
            "whatsapp_desc" -> if (isUrdu) "ہر شیڈول ویڈیو اپلوڈ ہونے کے بعد یہ ایپ آپ کو براہ راست وٹس ایپ پر تفصیلی رپورٹ بھیجتی ہے۔" else "Receive instant, automated reports on your WhatsApp as soon as any scheduled video is published."
            "phone_number_label" -> if (isUrdu) "وٹس ایپ موبائل نمبر (Country Code کے ساتھ)" else "WhatsApp Phone Number (with Country Code)"
            "auto_send_toggle" -> if (isUrdu) "خودکار رپورٹ بھیجیں" else "Auto-dispatch report after upload finishes"
            "auto_send_sub" -> if (isUrdu) "ویڈیو اپلوڈ مکمل ہوتے ہی نوٹیفکیشن ارسال ہوگا" else "Sends automated notification as soon as video is live"
            "lang_format_toggle" -> if (isUrdu) "اردو زبان میں رپورٹ کا فارمیٹ" else "Format Report in Urdu"
            "lang_format_sub" -> if (isUrdu) "رپورٹ اردو متن اور ایموجیز کے ساتھ بنے گی" else "Formatted with Urdu metadata, links & emojis"
            "live_preview_title" -> if (isUrdu) "وٹس ایپ میسج کا لائیو پیش منظر (Live Preview)" else "Live WhatsApp Message Preview"
            "send_test_report" -> if (isUrdu) "ابھی وٹس ایپ پر رپورٹ بھیجیں" else "Send Report via WhatsApp Now"
            "report_history" -> if (isUrdu) "بھیجی گئی رپورٹس کی ہسٹری" else "Dispatched Reports History"

            // Accounts
            "accounts_heading" -> if (isUrdu) "چینل اور پیج کنکشنز" else "Connected Channels & Pages"
            "accounts_subheading" -> if (isUrdu) "یہاں اپنے یوٹیوب چینل اور فیس بک پیج کے کریڈینشلز محفوظ رکھیں تاکہ آٹو اپلوڈنگ بلا تعطل جاری رہے۔" else "Manage authenticated connections to your YouTube Channel and Facebook Page for autonomous background uploading."
            "youtube_channel" -> if (isUrdu) "یوٹیوب چینل کنکشن" else "YouTube Channel Connection"
            "facebook_page" -> if (isUrdu) "فیس بک پیج کنکشن" else "Facebook Page Connection"
            "connected_status" -> if (isUrdu) "کنیکٹڈ ہے (Active)" else "Connected (Active)"
            "disconnected_status" -> if (isUrdu) "غیر فعال (Disconnected)" else "Disconnected"
            "btn_reconnect" -> if (isUrdu) "سیٹنگز تبدیل کریں" else "Edit Credentials"
            "test_ping" -> if (isUrdu) "پنگ ٹیسٹ کریں" else "Test Ping"
            "subscribers_label" -> if (isUrdu) "سبسکرائبرز" else "Subscribers"
            "videos_uploaded_label" -> if (isUrdu) "کل ویڈیوز" else "Uploaded Videos"
            "followers_label" -> if (isUrdu) "فالورز" else "Followers"
            "page_category_label" -> if (isUrdu) "کیٹگری" else "Page Category"
            "security_note" -> if (isUrdu) "آپ کے تمام ٹوکنز اور پاس ورڈز محفوظ ہیں اور صرف آپ کی ڈیوائس سے بیک گراؤنڈ اپلوڈنگ کے لیے استعمال ہوتے ہیں۔" else "All API tokens are kept private on your device and strictly used for authorized automated scheduling."

            // Smart Ideas / Insights
            "analytics_heading" -> if (isUrdu) "تجزیات اور سمارٹ آئیڈیاز" else "Analytics & Smart Ideas"
            "analytics_subheading" -> if (isUrdu) "یہاں آپ کے چینل کی پیشرفت اور مواد کو وائرل کرنے کے جدید آئیڈیاز جمع کیے گئے ہیں۔" else "Performance analytics, peak posting schedule recommendations, and growth ideas."
            "total_uploads" -> if (isUrdu) "کل اپلوڈز" else "Total Uploads"
            "audience_reach" -> if (isUrdu) "کل فالورز ریچ" else "Audience Reach"
            "adaptive_ready_title" -> if (isUrdu) "موبائل اور سسٹم (ویب/ڈیسک ٹاپ) کیلئے موزوں" else "Adaptive Mobile & Desktop Ready"
            "adaptive_ready_desc" -> if (isUrdu) "یہ ایپ خودکار ایڈاپٹیو لے آؤٹ پر بنائی گئی ہے جو ٹیبلٹ یا سسٹم پر سائیڈ نیویگیشن ریل اور موبائل پر باٹم بار کے ساتھ کام کرتی ہے۔" else "Engineered with adaptive window sizing, supporting phones via bottom navigation and tablets/desktop systems via responsive navigation rails."
            "peak_posting_title" -> if (isUrdu) "ویڈیو اپلوڈ کا بہترین وقت (Peak Posting Times)" else "Recommended Peak Publishing Hours"
            "advanced_ideas_title" -> if (isUrdu) "💡 مزید بہترین آئیڈیاز اور تجاویز" else "💡 Advanced Ideas & Recommendations"

            // Galaxy Sidebar & About Screen
            "tab_about" -> if (isUrdu) "ڈویلپر و ایپ (About)" else "About Developer"
            "galaxy_sidebar_title" -> if (isUrdu) "گلیکسی ایج سائیڈ بار" else "Galaxy Edge Sidebar"
            "galaxy_sidebar_sub" -> if (isUrdu) "جدید نیویگیشن اور کوئیک ایکشنز" else "Modern Navigation & Quick Tools"
            "developer_label" -> if (isUrdu) "ایپ ڈویلپر" else "App Developer"
            "developer_name" -> if (isUrdu) "ایم زیشان سرور" else "M Zeeshan Sarwar"
            "developer_title" -> if (isUrdu) "چیف سافٹ ویئر آرکیٹیکٹ و ڈویلپر" else "Lead Software Architect & Developer"
            "contact_phone_label" -> if (isUrdu) "رابطہ نمبر" else "Contact Number"
            "contact_whatsapp" -> if (isUrdu) "وٹس ایپ پر بات کریں" else "Chat on WhatsApp"
            "contact_call" -> if (isUrdu) "براہ راست کال کریں" else "Direct Call"
            "copy_number" -> if (isUrdu) "نمبر کاپی کریں" else "Copy Phone Number"
            "copied_toast" -> if (isUrdu) "نمبر کاپی ہو گیا: +9223007487676" else "Copied: +9223007487676"
            "about_app_desc" -> if (isUrdu) "AutoPoster Pro یوٹیوب اور فیس بک پر ویڈیوز خودکار اپلوڈ کرنے اور تفصیلی وٹس ایپ رپورٹس بھیجنے کے لیے ایک جدید اور محفوظ سسٹم ہے۔" else "AutoPoster Pro is an automated multi-platform publisher engineered for YouTube and Facebook Pages with instant WhatsApp reports."
            "app_version_label" -> if (isUrdu) "ورژن 2.4 (Galaxy Edition)" else "Version 2.4 (Galaxy Edition)"
            "galaxy_quick_tools" -> if (isUrdu) "کوئیک گلیکسی ٹولز" else "Quick Galaxy Tools"
            "engine_status_label" -> if (isUrdu) "آٹو انجن سٹیٹس" else "Auto Engine Status"

            // Search Bar & Filter Strings
            "search_tasks_placeholder" -> if (isUrdu) "شیڈولڈ اور اپلوڈ شدہ ویڈیوز عنوان سے تلاش کریں..." else "Search scheduled & uploaded tasks by title..."
            "filter_all" -> if (isUrdu) "تمام" else "All"
            "filter_scheduled" -> if (isUrdu) "شیڈولڈ" else "Scheduled"
            "filter_uploaded" -> if (isUrdu) "اپلوڈ شدہ" else "Uploaded"
            "filter_uploading" -> if (isUrdu) "جاری" else "Uploading"
            "filter_failed" -> if (isUrdu) "ناکام" else "Failed"
            "tasks_found_count" -> if (isUrdu) "ٹاسکس ملے" else "tasks found"
            "reset_search" -> if (isUrdu) "تلاش ختم کریں" else "Reset"
            "no_matching_tasks" -> if (isUrdu) "کوئی مماثل ویڈیو نہیں ملی" else "No Matching Videos Found"
            "no_matching_tasks_desc" -> if (isUrdu) "آپ کی تلاش کے مطابق کوئی شیڈولڈ یا اپلوڈ شدہ ٹاسک نہیں ملا۔ عنوان تبدیل کر کے دوبارہ کوشش کریں۔" else "No scheduled or uploaded tasks match your search criteria. Try a different title keyword."

            else -> key
        }
    }
}
