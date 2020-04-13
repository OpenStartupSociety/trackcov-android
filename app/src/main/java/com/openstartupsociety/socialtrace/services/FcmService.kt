package com.openstartupsociety.socialtrace.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotification
import com.openstartupsociety.socialtrace.data.local.repository.NotificationRepository
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.NotifyUsersRequest
import com.openstartupsociety.socialtrace.data.network.repository.NearbyUserRepository
import com.openstartupsociety.socialtrace.ui.splash.SplashActivity
import dagger.android.AndroidInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var nearbyUserRepository: NearbyUserRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        Log.d("FCM Service data", data.toString())

        when (data["code"]) {
            "1001" -> findContactedUsers()
            "999" -> notifyContact(data)
        }
    }

    private fun notifyContact(data: Map<String, String>) {
        val title = "You've come in contact with a Covid-19 patient"
        val channel = "General"

        GlobalScope.launch {
            val notification = LocalNotification(0, title, System.currentTimeMillis())
            notificationRepository.insertNotification(notification)
        }

        notify(title, null, channel)
    }

    private fun findContactedUsers() {
        val oldTimestamp = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -14)
        }.timeInMillis
        GlobalScope.launch {
            val users = nearbyUserRepository.getContactsFromLast14Days(oldTimestamp)
            val request = NotifyUsersRequest(users)
            when (val response = nearbyUserRepository.notifyUsers(request)) {
                is ApiSuccessResponse -> Log.d("FCM Service", response.body.data)
                is ApiErrorResponse -> Log.d("FCM Service", response.errorMessage)
            }
        }
    }

    private fun notify(title: String, content: String?, channel: String) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, channel)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_virus_notification)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setLights(Color.BLUE, 1, 1)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel, channel, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        NotificationManagerCompat.from(this).notify(channel.hashCode(), notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}