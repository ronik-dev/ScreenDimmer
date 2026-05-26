package com.example.screendimmer

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

		private val overlayPermissionLauncher = registerForActivityResult(
				ActivityResultContracts.StartActivityForResult()
		) {
				if (Settings.canDrawOverlays(this)) {
						findViewById<LinearLayout>(R.id.main).removeAllViews()
					val serviceIntent = Intent(this, OVERLAY_SERVICE::class.java)
					startService(serviceIntent);
				}
		}

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
					val serviceIntent = Intent(this, OVERLAY_SERVICE::class.java)
						startService(serviceIntent)
				}
		}

		private fun requireOverlayPermission() {
				val mainLayout = findViewById<LinearLayout>(R.id.main)

				val explanationText = TextView(this).apply {
						text = "This application requires permission to draw over other apps to dim the screen."
						textSize = 16f
						setPadding(16, 16, 16, 32)
						textAlignment = View.TEXT_ALIGNMENT_CENTER
				}
				mainLayout.addView(explanationText)

				val pButton = Button(this).apply {
						text = "Grant Permissions"
						setBackgroundColor(Color.WHITE)
						setOnClickListener {
								val intent = Intent(
										Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
										Uri.parse("package:$packageName")
								)
								overlayPermissionLauncher.launch(intent)
						}
				}
				mainLayout.addView(pButton)
		}

}
