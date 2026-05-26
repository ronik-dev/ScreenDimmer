package com.example.screendimmer

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
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
						findViewById<LinearLayout>(R.id.ll_permission_container).removeAllViews()
						// Use your helper function here so the intent is properly constructed
						startDimmerService(50) 
				}
		}

		override fun oncreate(savedinstancestate: bundle?) {
				super.oncreate(savedinstancestate)
				enableedgetoedge()
				setcontentview(r.layout.activity_main)

				viewcompat.setonapplywindowinsetslistener(findviewbyid(r.id.main)) { v, insets ->
						val systembars = insets.getinsets(windowinsetscompat.type.systembars())
						v.setpadding(systembars.left, systembars.top, systembars.right, systembars.bottom)
						insets
				}

				val dimmerseekbar = findviewbyid<seekbar>(r.id.sb_dimmer)
				val brightnesslabel = findviewbyid<textview>(r.id.tv_brightness_label)

				if (!settings.candrawoverlays(this)) {
						requireoverlaypermission()
				} else {
						startdimmerservice(dimmerseekbar.progress)
				}

				dimmerseekbar.setonseekbarchangelistener(object : seekbar.onseekbarchangelistener {
						override fun onprogresschanged(seekbar: seekbar?, progress: int, fromuser: boolean) {
								brightnesslabel.text = "dim intensity: $progress%"

								if (settings.candrawoverlays(this@mainactivity)) {
										startdimmerservice(progress)
								}
						}

						override fun onstarttrackingtouch(seekbar: seekbar?) {}
						override fun onstoptrackingtouch(seekbar: seekbar?) {}
				})
		}

		private fun startdimmerservice(level: int) {
				val serviceintent = intent(this, darkoverlay::class.java).apply {
						putextra("dim_level", level)
				}
				startservice(serviceintent)
		}

		private fun requireoverlaypermission() {
				val permissioncontainer = findviewbyid<linearlayout>(r.id.ll_permission_container)

				val explanationtext = textview(this).apply {
						text = "this application requires permission to draw over other apps to dim the screen."
						textsize = 16f
						setpadding(16, 16, 16, 32) 
						textalignment = view.text_alignment_center
				}
				permissioncontainer.addview(explanationtext)

				val pbutton = button(this).apply {
						text = "grant permissions"
						setbackgroundcolor(color.white)
						setonclicklistener {
								val intent = intent(
										settings.action_manage_overlay_permission, 
										uri.parse("package:$packagename")
								)
								overlaypermissionlauncher.launch(intent)
						}
				}
				permissioncontainer.addview(pbutton)
		}
}
