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
				windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
		}

		override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
				val dimProgress = intent?.getIntExtra("DIM_LEVEL", 50) ?: 50
				val alphaValue = dimProgress / 100f

				if (overlayView == null) {
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
								alpha = alphaValue
						}
						windowManager?.addView(overlayView, params)
				} else {
						overlayView?.alpha = alphaValue
				}

				return START_STICKY
		}

		override fun onDestroy() {
				super.onDestroy()
				if (overlayView != null && windowManager != null) {
						windowManager?.removeView(overlayView)
				}
		}

		override fun onBind(intent: Intent?): IBinder? {
				return null
		}
}
