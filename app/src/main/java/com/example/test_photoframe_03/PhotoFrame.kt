package com.example.test_photoframe_03

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.test_photoframe_03.databinding.ActivityPhotoframeBinding
import java.util.*
import kotlin.concurrent.timer

class PhotoFrame : AppCompatActivity() {

	private val imageUriList = mutableListOf<Uri>()

	private var currentPosition = 0

	private var timer: Timer? = null
	private val photoImageView: ImageView by lazy {
		binding.photoImageView
	}

	private val backgroundPhotoImageView: ImageView by lazy {
		binding.backgroundPhotoImageView
	}


	private lateinit var binding: ActivityPhotoframeBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityPhotoframeBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		getImageDataInIntent()

	}

	private fun getImageDataInIntent() {
		val size = intent.getIntExtra("UriListSize", 0)
		for (i in 0..size) {
			intent.getStringExtra("photo$i")?.let {
				imageUriList.add(Uri.parse(it))
			}
		}

	}

	private fun startTimer() {
		timer = timer(period = 5 * 1000) {
			runOnUiThread {

				Log.d("PhotoFrame", "5초가 지나감 !!")

				val current = currentPosition
				val next = if (imageUriList.size <= currentPosition + 1) 0 else currentPosition + 1

				backgroundPhotoImageView.setImageURI(imageUriList[current])

				photoImageView.alpha = 0f
				photoImageView.setImageURI(imageUriList[next])
				photoImageView.animate()
					.alpha(1.0f)
					.setDuration(1000)
					.start()

				currentPosition = next
			}

		}
	}

	override fun onStop() {
		super.onStop()

		Log.d("PhotoFrame", "onStop!!! timer cancel")
		timer?.cancel()
	}


	override fun onStart() {
		super.onStart()

		Log.d("PhotoFrame", "onStart!!! timer start")
		startTimer()
	}

	override fun onDestroy() {
		super.onDestroy()

		Log.d("PhotoFrame", "onDestroy!!! timer cancel")
		timer?.cancel()
	}



}