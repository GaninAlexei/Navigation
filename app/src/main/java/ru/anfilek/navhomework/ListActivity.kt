package ru.anfilek.navhomework

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.Manifest.permission.CAMERA as CAMERA_PERMISSION

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        findViewById<FloatingActionButton>(R.id.fabStartCamera).setOnClickListener {
            val value = radioGroup.checkedRadioButtonId
            if (value == R.id.radioButton1) {
                startCameraFeature()
            }
            else if(value == R.id.radioButton2){

            }
        }

        findViewById<Button>(R.id.buttonItem).setOnClickListener {
            startItemActivity()
        }

    }


    private fun startCameraFeature() {
        // check camera permission
        // handle the check result
        // show dialog if it is needed
        // feel free to customise the button if it is needed

        val permission = CAMERA_PERMISSION
        handleCheckResult(
                permission,
                checkPermission(permission)
            )
    }

    private fun startItemActivity() {
        startActivity(Intent(this, ItemActivity::class.java))
    }

    private fun checkPermission(permission: String): CheckPermissionResult {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> CheckPermissionResult.GRANTED

            ContextCompat.checkSelfPermission(
                    this,
                    permission
            ) == PackageManager.PERMISSION_GRANTED -> CheckPermissionResult.GRANTED

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                    permission
            ) -> CheckPermissionResult.NEED_TO_EXPLAIN

            else -> CheckPermissionResult.NEED_TO_REQUEST
        }
    }

    private fun handleCheckResult(permission: String, result: CheckPermissionResult){
        when (result){
            CheckPermissionResult.GRANTED -> startCameraActivity()
            CheckPermissionResult.DENIED -> failedGracefully()
            CheckPermissionResult.NEED_TO_REQUEST -> askForPermission(CAMERA_PERMISSION)
            CheckPermissionResult.NEED_TO_EXPLAIN -> showRationale()
        }
    }

    private fun showRationale() {
        AlertDialog.Builder(this)
                .setTitle("Camera permission")
                .setMessage("Camera permission is needed to allow this feature work.")
                .setPositiveButton("I understand") { _, _ -> askForPermission(CAMERA_PERMISSION) }
                .show()
    }

    private fun failedGracefully() {
        AlertDialog.Builder(this)
                .setTitle("Camera permission")
                .setMessage("Camera Permission was not granted. We respect your decision.")
                .setNegativeButton("I changed my mind") { _, _ -> askForPermission(CAMERA_PERMISSION)}
                .setPositiveButton("Ok", null)
                .show()
    }

    private fun startCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
    }

    private val requestPermissionLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
            ){ isGranted: Boolean ->
                if(isGranted){
                    startCameraActivity()
                } else{
                    failedGracefully()
                }
            }

    private fun askForPermission(permission: String) {
        requestPermissionLauncher.launch(permission)
    }

    enum class CheckPermissionResult{
        GRANTED,
        DENIED,
        NEED_TO_REQUEST,
        NEED_TO_EXPLAIN
    }
}