package com.example.test_photoframe_03

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.test_photoframe_03.databinding.ActivityMainBinding

/*
* xo 핵심기능
*  저장소 접근 권한요청후 승인 받아 imageViewList에 저장.
* 	저장된 Image들을 전자액자 Activity로 가져와 일정한 간격과 애니메이션으로 전환하여 보여줌
*
* xo 공부내용
*  LinearLayout
* 	Permission과 예외처리
* 	startActivity로 Activity간 데이터 주고받기
* 	viewAnimation
* 	LifeCycle응용
* 	ContentProvider의 SAF
*  */
@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {



	private val imageViewList: List<ImageView> by lazy {
		mutableListOf<ImageView>().apply {
			add(binding.imageViewFirst)
			add(binding.imageViewSecond)
			add(binding.imageViewThird)
			add(binding.imageViewFours)
			add(binding.imageViewFifth)
			add(binding.imageViewSixth)
			add(binding.imageViewSevens)
			add(binding.imageViewEits)
			add(binding.imageViewNines)
		}
	}

	private val imageUriList = mutableListOf<Uri>()

	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityMainBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setListener()

	}

	override fun permissionGranted(requestCode: Int) {
		when (requestCode) {
			READ_STORAGE_REQUEST_CODE -> openGallery()
		}
	}

	override fun permissionDenied(requestCode: Int) {
		when (requestCode) {
			READ_STORAGE_REQUEST_CODE -> alertDialog()
		}
	}

	private fun setListener() {
		binding.run {
			addPhotoButton.setOnClickListener {
				val readStoragePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
				requirePermissions(readStoragePermission, READ_STORAGE_REQUEST_CODE)

			}
			startPhotoFrameButton.setOnClickListener {
				sendImageData()
			}
		}
	}

	private fun openGallery() {
		val intent = Intent(Intent.ACTION_GET_CONTENT)
		intent.type = "image/*"
		startActivityForResult(intent, SEND_INTENT_REQUEST_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (resultCode == RESULT_OK) {
			when (requestCode) {
				SEND_INTENT_REQUEST_CODE -> {
					val imageUri: Uri? = data?.data
					if (imageUri != null) {
						if (imageUriList.size == 9) {
							Toast.makeText(this, "사진은 9장까지만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
							return
						}
						imageUriList.add(imageUri)
						imageViewList.get(imageUriList.size - 1).setImageURI(imageUri)
					} else {
						Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
					}

				}
				else -> {

				}
			}
		}
	}

	private fun sendImageData(){
		val intent = Intent(this, PhotoFrame::class.java)
		intent.putExtra("UriListSize",imageUriList.size)
		// imageUriList에 담겨있는 Uri 정보들을 하나하나 꺼내 putExtra로 담는다. index는 name으로, uri는 value로
		imageUriList.forEachIndexed { index, uri ->
			intent.putExtra("photo$index",uri.toString())
		}
		startActivity(intent)
	}


	companion object {
		private const val READ_STORAGE_REQUEST_CODE = 100
		private const val SEND_INTENT_REQUEST_CODE = 101
	}


}
