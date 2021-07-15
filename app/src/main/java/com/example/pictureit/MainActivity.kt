package com.example.pictureit

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.pictureit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, CAMERA_KEY)
            } catch (exception: ActivityNotFoundException) {
                Toast.makeText(
                    this, "Your phone camera has an issue", Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            try {
                startActivityForResult(intent, GALLERY_KEY)
            } catch (exception: ActivityNotFoundException) {
                Toast.makeText(
                    this, "Your gallery apps have an issue", Toast.LENGTH_LONG
                ).show()
            }
        }
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_KEY && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.cameraImage.setImageBitmap(imageBitmap)
        }
        if (requestCode == GALLERY_KEY && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val imageStream = contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.galleryImage.setImageBitmap(selectedImage)
            }
        }
    }
}