package com.rosh.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rosh.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var notificationManager:NotificationManagerCompat
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        notificationManager = NotificationManagerCompat.from(this)

        binding.apply {
            btnShow.setOnClickListener {
                notificationManager.notify(1, notifyBuilder())
            }
            btnHide.setOnClickListener {
                notificationManager.cancel(1)
            }

        }
    }
    private fun notifyBuilder(): Notification {
        val notificationSmallItem = RemoteViews(packageName, R.layout.notification_small_item)
        val notificationBigItem = RemoteViews(packageName, R.layout.notification_big_item)

        val clickIntent = Intent(this, NotificationReceiver::class.java)
        val clickPendingIntent =
            PendingIntent.getService(this, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE)

        notificationSmallItem.setTextViewText(R.id.tv_title_1, "Kichik sarlavha")
        notificationSmallItem.setTextViewText(R.id.tv_about_1, "Kichik matn")

        notificationBigItem.setImageViewResource(R.id.image_view_expanded,
            R.drawable.ic_launcher_foreground
        )
        notificationBigItem.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("exampleChannel", name, importance).apply {
                description = descriptionText
            }


            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, "exampleChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(notificationSmallItem)
            .setCustomBigContentView(notificationBigItem)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()).build()
    }

}