package com.example.biuaalert

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val textView = findViewById<TextView>(R.id.textView)
        btnPlay.setOnClickListener {
            if (btnPlay.text == "Start") {
                val myService = Intent(this@MainActivity, SoundService::class.java)
                startService(myService)
                btnPlay.text = "Stop"
                textView.setTextColor(Color.parseColor("#FF0000"))
            } else {
                val myService = Intent(this@MainActivity, SoundService::class.java)
                stopService(myService)
                btnPlay.text = "Start"
                textView.setTextColor(Color.parseColor("#0000FF"))
            }
        }
    }
}