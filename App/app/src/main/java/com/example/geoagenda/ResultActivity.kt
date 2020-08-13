package com.example.geoagenda

import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if (intent.getBooleanExtra("notification", false)) { //Just for confirmation
            supportActionBar?.title = Html.fromHtml("<font color='#FFFFFF'>${"Recordatorio"} </font>");
            txtTitleView.movementMethod = ScrollingMovementMethod.getInstance()
            txtTitleView.text = intent.getStringExtra("title")
            txtMsgView.movementMethod = ScrollingMovementMethod.getInstance()
            txtMsgView.text = intent.getStringExtra("message")
            cerrarButton.setOnClickListener {
                finishAffinity()
            }

        }
    }
}