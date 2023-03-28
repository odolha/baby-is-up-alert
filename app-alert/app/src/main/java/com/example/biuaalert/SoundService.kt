package com.example.biuaalert;

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.net.URI

fun project(s1: Double, t1: Double, s2: Double, t2: Double, v: Double): Double {
    return t1 + ((v - s1) * (t2 - t1)) / (s2 - s1);
}

class SoundService : Service() {

    private val TAG = "SoundService"

    private lateinit var wsClient: WsClient

    override fun onBind(arg0: Intent?): IBinder? {
        Log.i(TAG, "onBind()")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate() , service started...")

        val uri = URI("${BuildConfig.BIUA_SERVER}")
        wsClient = WsClient(uri)
        wsClient.connect()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Play sound at interval
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                val volume = project(0.0, 0.0, 1.0, 1.0, wsClient.alertLevel).toFloat()
                val delay = project(0.0, 1000.0, 1.0, 100.0, wsClient.alertLevel).toLong()
                Log.i(TAG, "RUN PLAY $volume $delay")
                val player = MediaPlayer.create(this@SoundService, R.raw.sound)
                player!!.setVolume(volume, volume)
                player!!.start()
                mainHandler.postDelayed(this, delay)
            }
        })

        return START_STICKY
    }

    fun onUnBind(arg0: Intent?): IBinder? {
        Log.i(TAG, "onUnBind()")
        return null
    }

    fun onStop() {
        Log.i(TAG, "onStop()")
    }

    fun onPause() {
        Log.i(TAG, "onPause()")
    }

    override fun onDestroy() {
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "onCreate() , service stopped...")
    }

    override fun onLowMemory() {
        Log.i(TAG, "onLowMemory()")
    }
}
