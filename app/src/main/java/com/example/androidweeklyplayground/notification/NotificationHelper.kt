package com.example.androidweeklyplayground.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androidweeklyplayground.MainActivity
import com.example.androidweeklyplayground.R


@SuppressLint("MissingPermission")
fun Context.createNotification(
    title: String,
    text: String,
    imageResource: Int,
    channelID: String = "12345"
) {
    val notificationBuilder = createDefaultBuilder(title, text, channelID)
        .apply {
            setCustomNotification(this@createNotification, title, text, imageResource)
        }
    NotificationManagerCompat.from(this)
        .apply {
            createNotificationChannelIfNecessary()
            notify(12345, notificationBuilder.build())
        }
}

private fun Context.createDefaultBuilder(
    title: String,
    text: String,
    channelID: String = "12345",
    priority: Int = NotificationCompat.PRIORITY_MAX
): NotificationCompat.Builder {
    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent =
        PendingIntent.getActivity(this, 1729, intent, PendingIntent.FLAG_IMMUTABLE)

    return NotificationCompat.Builder(this.applicationContext, channelID)
        .setSmallIcon(R.mipmap.ic_launcher_round) // Required.
        .setPriority(priority)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentTitle(title)
        .setContentText(text)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .addAction(NotificationCompat.Action(androidx.core.R.drawable.ic_call_answer, "Answer Call", pendingIntent))
        .addAction(NotificationCompat.Action(androidx.core.R.drawable.ic_call_decline, "Decline Call", null))
}

private fun NotificationCompat.Builder.setCustomNotification(
    context: Context,
    title: String,
    text: String,
    imageResource: Int
): NotificationCompat.Builder {
    // inflate the layout and set the values to our UI IDs
    val remoteViews = RemoteViews(context.packageName, R.layout.view_custom_image_notification)
    remoteViews.setImageViewResource(R.id.image, imageResource)
    remoteViews.setTextViewText(R.id.title, title)
    remoteViews.setTextViewText(R.id.text, text)

    setCustomBigContentView(remoteViews)

    return this
}

private fun NotificationManagerCompat.createNotificationChannelIfNecessary() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel.
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("12345", "Generic", importance)
        mChannel.description = "Genric notifications"
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        createNotificationChannel(mChannel)
    }
}