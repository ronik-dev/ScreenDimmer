package com.example.screendimmer

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
		override fun onCreate(savedInstanceState: Bundle?) {
				super.onCreate(savedInstanceState)
				enableEdgeToEdge()
				setContentView(R.layout.activity_main)
				ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
						val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
						v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
						insets
				}
				if (!Settings.canDrawOverlays(this)) {
						requireOverlayPermission()
				} else {
						drawOverlay()
				}
		}
		fun requireOverlayPermission(){
				val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))
				startActivityForResult(intent, 0)
		}
		fun drawOverlay(){
				val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
				val params = WindowManager.LayoutParams(
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
						WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
						PixelFormat.TRANSLUCENT
				)
				val overlayView = View(this).apply {
						setBackgroundColor(Color.BLACK)
				}
				wm.addView(overlayView, params)
		}
}

