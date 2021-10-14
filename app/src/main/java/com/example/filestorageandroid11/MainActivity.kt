package com.example.filestorageandroid11

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    val CREATE_FILE = 1

    private lateinit var iv: ImageView
    private lateinit var btn: Button
    private lateinit var btn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv = findViewById(R.id.iv)
        btn = findViewById(R.id.btn)
        btn2 = findViewById(R.id.btn2)

        btn.setOnClickListener {
            var bitmapDrawable: BitmapDrawable = iv.drawable as BitmapDrawable
            var bitmap = bitmapDrawable.bitmap
            saveImageToStorage(bitmap)
        }


        btn2.setOnClickListener {
            createFile(null)
        }

    }


    private fun saveImageToStorage(
        bitmap: Bitmap,
        filename: String = "screenshot.jpg",
        mimeType: String = "image/jpeg",
        directory: String = Environment.DIRECTORY_PICTURES,
        mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    ) {
        val imageOutStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            }

            contentResolver.run {
                val uri =
                    contentResolver.insert(mediaContentUri, contentValues)
                        ?: return
                imageOutStream = openOutputStream(uri) ?: return
            }
        } else {
            val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
            val image = File(imagePath, filename)
            imageOutStream = FileOutputStream(image)
        }

        imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }


    private fun createFile(pickerInitialUri: Uri?) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            //putExtra(Intent.EXTRA_TITLE, "invoice.pdf")
            putExtra(Intent.EXTRA_TITLE, ".nomedia")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, CREATE_FILE)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == CREATE_FILE && resultCode == RESULT_OK) {
            // The result data contains a URI for directory that
            // the user selected.
            resultData?.data?.also { uri ->
                Log.d("tag", "Success")
            }
        }
    }


}