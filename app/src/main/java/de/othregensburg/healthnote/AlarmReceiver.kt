package de.othregensburg.healthnote

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// BroadcastReceiver class to handle Alarms
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
        // Build notification
        val builder = context?.let {
            NotificationCompat.Builder(it, "healthnote")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HealthNote Alarm Manager")
                .setContentText("Test alert")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        }

        val notificationManagerCompat = context?.let { NotificationManagerCompat.from(it) }
        if (builder != null) {
            notificationManagerCompat?.notify(123, builder.build())
        }

    }

}