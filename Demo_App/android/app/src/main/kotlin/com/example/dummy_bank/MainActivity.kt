package com.example.dummy_bank

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "phishsafe_sdk/screen_recording"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "isScreenRecording" -> {
                    val isRecording = isScreenRecording()
                    result.success(isRecording)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun isScreenRecording(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = activityManager.runningAppProcesses
        val screenRecordKeywords = listOf("screenrec", "recorder", "mobizen", "az", "du", "record")

        for (process in processes) {
            val processName = process.processName.lowercase()
            if (screenRecordKeywords.any { keyword -> processName.contains(keyword) }) {
                return true
            }
        }
        return false
    }
}
