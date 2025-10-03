package com.constant.everydayjapanese.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.model.Kanji
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.scene.main.MainActivity
import com.constant.everydayjapanese.scene.main.SettingActivity
import com.constant.everydayjapanese.singleton.JSONManager
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.util.FrequencyEnum
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.SectionEnum
import java.time.Instant
import java.time.ZoneOffset
/**
 * Implementation of App Widget functionality.
 */
class EverydayJapaneseAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.everyday_japanese_app_widget)
        val indexEnum = IndexEnum.ofRaw(PrefManager.getInstance().getIntValue(Pref.partToMemorize.name))
        val frequencyEnum = FrequencyEnum.ofRaw(PrefManager.getInstance().getIntValue(Pref.frequenceOfWordChange.name))

        // 오늘의 count 계산
        val count = when (frequencyEnum) {
            FrequencyEnum.day -> getDayCount()
            FrequencyEnum.hour -> getHourCount()
        }
        var kanji: Kanji? = null
        var vocab: Vocabulary? = null

        views.setTextViewText(R.id.textview_sound, "")
        views.setTextViewText(R.id.textview_meaning, "")
        views.setTextViewText(R.id.textview_kanji, "")
        views.setTextViewText(R.id.textview_eumhun, "")

        when (indexEnum) {
            IndexEnum.kanjiBookmark -> {
                PrefManager.getInstance().getStringValue(Pref.kanjiBookmark.name)?.let { json ->
                    val set = JSONManager.getInstance().decodeJSONtoKanjiSet(json).toList()
                    if (set.isNotEmpty()) {
                        kanji = set[count % set.size]
                    }
                }

            }
            IndexEnum.vocabularyBookmark -> {
                PrefManager.getInstance().getStringValue(Pref.vocabularyBookmark.name)?.let { json ->
                    val set = JSONManager.getInstance().decodeJSONtoVocabularySet(json).toList()
                    if (set.isNotEmpty()) {
                        vocab = set[count % set.size]
                    }
                }
            }
            else -> {
                val jsonData = JSONManager.getInstance().loadJsonFromAsset(context, indexEnum.getFileName())
                if (indexEnum.getSection() == SectionEnum.kanji) {
                    val list = JSONManager.getInstance().decodeJSONtoKanjiArray(jsonData)
                    if (list.isNotEmpty()) kanji = list[count % list.size]
                } else if (indexEnum.getSection() == SectionEnum.vocabulary) {
                    val list = JSONManager.getInstance().decodeJSONtoVocabularyArray(jsonData)
                    if (list.isNotEmpty()) vocab = list[count % list.size]
                }
            }
        }

        // Kanji or Vocabulary 출력

        if ((indexEnum == IndexEnum.kanjiBookmark && kanji == null) ||
            (indexEnum == IndexEnum.vocabularyBookmark && vocab == null)
            ){
            views.setViewVisibility(R.id.textview_sound, View.GONE)
            views.setViewVisibility(R.id.textview_meaning, View.GONE)
            views.setViewVisibility(R.id.textview_kanji, View.GONE)
            views.setTextViewText(R.id.textview_eumhun, context.getString(R.string.no_words_registered_favorites))
        } else {
            views.setViewVisibility(R.id.textview_sound, View.VISIBLE)
            views.setViewVisibility(R.id.textview_meaning, View.VISIBLE)
            views.setViewVisibility(R.id.textview_kanji, View.VISIBLE)
        }

        kanji?.let {
            views.setTextViewText(R.id.textview_sound, it.jpSound)
            views.setTextViewText(R.id.textview_meaning, it.jpMeaning)
            views.setTextViewText(R.id.textview_kanji, it.kanji)
            views.setTextViewText(R.id.textview_eumhun, it.eumhun)
        }

        vocab?.let {
            views.setTextViewText(R.id.textview_sound, it.sound)
            views.setTextViewText(R.id.textview_meaning, it.meaning)
            views.setTextViewText(R.id.textview_kanji, it.word)
            views.setTextViewText(R.id.textview_eumhun, "")
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        val settingsIntent = Intent(context, SettingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val settingsPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId + 1000,
            settingsIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.button_settings, settingsPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun getDayCount(): Int {
        val currentTimeSeconds = Instant.now().epochSecond.toInt()
        val dayCount = currentTimeSeconds / (24 * 60 * 60)
        return dayCount
    }

    fun getHourCount(): Int {
        val currentTimeSeconds = Instant.now().epochSecond
        val hourCount = (currentTimeSeconds / 3600).toInt()
        return hourCount
    }
}
