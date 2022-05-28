package de.othregensburg.healthnote

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import de.othregensburg.healthnote.model.Medicament
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_main_containerview)
        val navController = navHostFragment?.findNavController()
        if (navController != null) {
            setupActionBarWithNavController(navController)
        }
    }

    fun scheduleAlerts(medList: List<Medicament>) {
        val tag = "scheduleAlerts"
        // Debug: Check Amount of elements in given medList
        Log.d(tag, "medList has ${medList.size} elements")
        for (med in medList.listIterator()) {
            if (med.alert) {
                // Set Alert according to data of med
                val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                alarmIntent.putExtra("MED_ID", med.id)
                alarmIntent.putExtra("MED_NAME", med.name)
                val pendingIntent = PendingIntent.getBroadcast(this, med.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
                val cal = Calendar.getInstance()
                val hour = med.time.substring(0,2).toInt()
                val min = med.time.substring(3,5).toInt()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, min)
                cal.set(Calendar.SECOND, 0)
                val repeatArr = resources.getStringArray(R.array.repeat_array)
                if (med.repeatSetting == repeatArr[0]) {
                    // Daily
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 86400000, pendingIntent)
                    val toastText = "Successfully scheduled daily alert at $hour:$min"
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
                else {
                    // Weekly
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 604800000, pendingIntent)
                    val toastText = "Successfully scheduled weekly alert at $hour:$min"
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun cancelAlerts() {
        // Cancel all alerts
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_IMMUTABLE))
        Log.d("cancelAlerts", "Alerts cancelled")
    }

    private fun createNotificationChannel() {
            // test notification channel
            val name : CharSequence = "HealthNoteReminderChannel"
            val description = "Channel for HealthNote Alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("healthnote", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
    }

}