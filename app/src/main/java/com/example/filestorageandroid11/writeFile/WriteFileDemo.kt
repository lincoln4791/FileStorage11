package com.example.filestorageandroid11.writeFile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.util.Log
import com.example.filestorageandroid11.R
import com.example.filestorageandroid11.databinding.ActivityWriteFileDemoBinding
import java.io.*
import java.lang.StringBuilder

class WriteFileDemo : AppCompatActivity() {
    val CREATE_FILE = 1
    val WRITE_FILE = 2
    val READ_FILE = 3

    lateinit var binding: ActivityWriteFileDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteFileDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

                // Optionally, specify a URI for the directory that should be opened in
                // the system file picker before your app creates the document.
            }
            startActivityForResult(intent, CREATE_FILE)
        }

        binding.btnRead.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

                // Optionally, specify a URI for the directory that should be opened in
                // the system file picker before your app creates the document.
            }
            startActivityForResult(intent, READ_FILE)
        }


        binding.btnWrite.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

                // Optionally, specify a URI for the directory that should be opened in
                // the system file picker before your app creates the document.
            }
            startActivityForResult(intent, WRITE_FILE)
        }

    }




    private fun writeFile(uri: Uri) {

        try {

            var parcelFileDescriptor: ParcelFileDescriptor? =
                this.contentResolver.openFileDescriptor(uri, "w")
            var fos = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)
            var dataToWrite = binding.et1.text.toString()
            fos.write(dataToWrite.toByteArray())
            fos.close()
            parcelFileDescriptor.close()
        } catch (e: IOException) {
        }
    }


    private fun readFile(uri: Uri) :String {
        var inputStream = contentResolver.openInputStream(uri)
        var bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var stringBuilder = StringBuilder()

        var lines:List<String> = bufferedReader.readLines()
        for(line in lines){
            stringBuilder.append(line+"\n")
        }

        inputStream?.close()
        return stringBuilder.toString()
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

        else if (requestCode == WRITE_FILE && resultCode == RESULT_OK){
            resultData?.data?.also { uri ->
                Log.d("tag", "Write Success")
                writeFile(uri)
            }
        }

        else if (requestCode == READ_FILE && resultCode == RESULT_OK){
            resultData?.data?.also { uri ->
                Log.d("tag", "Write Success")
                var data = readFile(uri)
                Log.d("tag","read Data $data")
            }
        }


    }


}