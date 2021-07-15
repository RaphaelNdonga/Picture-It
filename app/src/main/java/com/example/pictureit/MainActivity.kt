package com.example.pictureit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pictureit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val getGalleryPicture =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                val imageStream = contentResolver.openInputStream(uri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.galleryImage.setImageBitmap(selectedImage)
            }

        val getCameraPicture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap
                    binding.cameraImage.setImageBitmap(bitmap)
                }
            }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getCameraPicture.launch(intent)
        }
        binding.galleryButton.setOnClickListener {
            getGalleryPicture.launch("image/*")
        }
        setContentView(binding.root)
    }
}