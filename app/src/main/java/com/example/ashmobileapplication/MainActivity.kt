package com.example.ashmobileapplication;

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_animation)

        activityScope.launch {
            delay(5000)
            startActivity(Intent(this@MainActivity, ConnectionActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }
}
