package com.example.geoagenda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("titulo", intent.getStringExtra("titulo"))
        service.putExtra("nota", intent.getStringExtra("nota"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        context.startService(service)
    }

}