package com.example.test_photoframe_03

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

	abstract fun permissionGranted(requestCode: Int)
	abstract fun permissionDenied(requestCode: Int)

	fun requirePermissions(permissions: Array<String>, requestCode: Int) {
		val readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

		when {
			permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED } -> {
				permissionGranted(requestCode)
			}
			shouldShowRequestPermissionRationale(readStoragePermission) -> {
				alertDialog()
			}

			else -> {
				requestPermissions(permissions, requestCode)
			}
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (grantResults.isNotEmpty()) {
			when (requestCode) {
				requestCode -> {
					if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
						permissionGranted(requestCode)
					} else {
						permissionDenied(requestCode)
					}
				}
				else -> {
				}
			}
		} else {
			Toast.makeText(this, "권한 요청과정 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
		}
	}

	fun alertDialog() {
		AlertDialog.Builder(this)
			.setTitle("권한이 필요합니다.")
			.setMessage("전자액자 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
			.setPositiveButton("동의하기") { _, _ ->
				navigateSettings()
			}
			.setNegativeButton("거부하기") { _, _ ->
				finish()
			}
			.create()
			.show()
	}

	private fun navigateSettings() {
		val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
		val uri: Uri = Uri.fromParts("package", packageName, null)
		intent.data = uri
		startActivity(intent)
	}

}