package com.example.yamanboztepe_final

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load

class MainActivity: AppCompatActivity() {
    private lateinit var imgCapture: ImageView
    private lateinit var txtTag: EditText
    private lateinit var photoAdapter: PhotoAdapter

    private var taggedPhotos = ArrayList<TaggedPhotos>()
    private var tags = listOf<String>()
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setActions()
    }

    private fun bindViews() {
        setRecyclerView()
        imgCapture = findViewById(R.id.imgCapture)
        txtTag = findViewById(R.id.txtTag)
    }

    private fun setActions() {
        findViewById<Button>(R.id.btnCapture).setOnClickListener {
            dispatchTakePictureIntent()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveData()
        }

        findViewById<Button>(R.id.btnLoad).setOnClickListener {
            loadData()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        if (txtTag.text.isNotEmpty() && imgCapture.visibility == View.VISIBLE) {
            val photosDatabase = TaggedPhotosDatabase.getPhotosDatabase(this)
            val capturedImageBitmap = (imgCapture.drawable as BitmapDrawable).bitmap
            tags = txtTag.text.toString().split(";").map { it.trim() }
            tags.forEach {
                photosDatabase?.photosDao()?.insert(TaggedPhotos(it,capturedImageBitmap))
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Please take a photo and type something in tag", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (txtTag.text.isNotEmpty()) {
            val photosDatabase = TaggedPhotosDatabase.getPhotosDatabase(this)
            tags = txtTag.text.toString().split(";").map { it.trim() }
            var existsTags = mutableListOf<String>()
            tags.forEach {
                val tPhotos = photosDatabase?.photosDao()?.getPhotos(it) as ArrayList<TaggedPhotos>
                if (tPhotos.isNotEmpty()) {
                    existsTags.add(it)
                }
            }

            if (existsTags.isNotEmpty()) {
                taggedPhotos.clear()
                taggedPhotos.addAll(photosDatabase?.photosDao()?.getPhotos(existsTags.first()) as ArrayList<TaggedPhotos>)
                photoAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "No photo found with the requested tag.", Toast.LENGTH_SHORT).show()
            }

            loadImage()

        } else {
            Toast.makeText(this, "Please type something in tag", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadImage() {
        if (taggedPhotos.isNotEmpty()) {
            imgCapture.visibility = View.VISIBLE
            imgCapture.load(taggedPhotos.last().photos)

        } else {
            imgCapture.visibility = View.INVISIBLE
        }
    }

    private fun setRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.photosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        photoAdapter = PhotoAdapter(taggedPhotos)
        recyclerView.adapter = photoAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgCapture.setImageBitmap(imageBitmap)
            imgCapture.visibility = View.VISIBLE

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
