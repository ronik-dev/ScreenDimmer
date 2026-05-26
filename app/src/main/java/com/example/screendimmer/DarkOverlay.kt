package com.example.screendimmer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager

class DarkOverlay : Service() {

		private var windowManager: WindowManager? = null
		private var overlayView: View? = null

		override fun onCreate() {
				super.onCreate()
		}

		override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
				windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

				val params = WindowManager.LayoutParams(
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
						WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
						PixelFormat.TRANSLUCENT
				)

				overlayView = View(this).apply {
						setBackgroundColor(Color.BLACK)
						alpha = 0.5f
				}

				windowManager?.addView(overlayView, params)

				return START_STICKY
		}

		override fun onDestroy() {
				super.onDestroy()
				// Crucial: Clean up and remove the view when the service is stopped
				if (overlayView != null && windowManager != null) {
						windowManager?.removeView(overlayView)
				}
		}

		override fun onBind(intent: Intent?): IBinder? {
				return null
		}
}
