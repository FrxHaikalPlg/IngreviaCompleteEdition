package com.frxhaikal_plg.ingrevia.ui.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.ui.AboutDevActivity
import com.frxhaikal_plg.ingrevia.ui.login.LoginActivity
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileImage: ShapeableImageView
    private var selectedImageUri: Uri? = null
    private lateinit var userPreferences: UserPreferences

    // Permission launcher for camera and storage
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                openImagePickerDialog()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                profileImage.setImageURI(it)
            }
        }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                selectedImageUri?.let {
                    profileImage.setImageURI(it)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileImage = view.findViewById(R.id.profile_image)
        userPreferences = UserPreferences(requireContext())

        // Binding tombol logout
        val logOutButton: View = view.findViewById(R.id.log_out)
        logOutButton.setOnClickListener {
            logOut()
        }

        val aboutDevButton: View = view.findViewById(R.id.about_developer)
        aboutDevButton.setOnClickListener {
            aboutDev()
        }

        profileImage.setOnClickListener {
            checkPermissions()
        }
    }

    private fun aboutDev() {
        val intent = Intent(requireContext(), AboutDevActivity::class.java)
        startActivity(intent)
    }

    private fun logOut() {
        lifecycleScope.launch {
            userPreferences.clearUserSession() // Menghapus sesi pengguna
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java)) // Kembali ke LoginActivity
            requireActivity().finish() // Menutup ProfileActivity
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermissionLauncher.launch(permissions)
    }

    private fun openImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Take a Photo")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Change Profile Picture")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> captureImageWithCamera()
                }
            }
            .show()
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun captureImageWithCamera() {
        val imageUri = createImageUri()
        selectedImageUri = imageUri
        captureImageLauncher.launch(imageUri)
    }

    private fun createImageUri(): Uri? {
        val contentResolver = requireContext().contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}