package com.teamfour.kooksy.ui.create

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import com.teamfour.kooksy.R.id.main

class AddImageActivity : AppCompatActivity() {
    private lateinit var cameraPreview: ImageView
    private var isUsingFrontCamera = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.add_image)

        // Initialize the ImageView and buttons
        cameraPreview = findViewById(R.id.camera_preview)
        val captureButton = findViewById<ImageButton>(R.id.capture_image_button)
        val galleryButton = findViewById<Button>(R.id.upload_from_gallery_button)
        val switchButton = findViewById<ImageButton>(R.id.rotate_camera_button)
        val closeButton = findViewById<ImageButton>(R.id.close_button)

        // Set button listeners
        captureButton.setOnClickListener { openCamera() }
        galleryButton.setOnClickListener { openGallery() }
        switchButton.setOnClickListener { switchCamera() }
        closeButton.setOnClickListener { finish() }

        // Handle window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check for necessary permissions
        checkPermissions()
    }

    private fun checkPermissions() {
        val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (isUsingFrontCamera) {
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        } else {
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0)
        }
        cameraLauncher.launch(cameraIntent)
    }

    private fun switchCamera() {
        isUsingFrontCamera = !isUsingFrontCamera
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            cameraPreview.setImageBitmap(bitmap)
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            cameraPreview.setImageURI(imageUri)
        }
    }

    companion object {
        private const val REQUEST_PERMISSIONS = 1
    }
}
