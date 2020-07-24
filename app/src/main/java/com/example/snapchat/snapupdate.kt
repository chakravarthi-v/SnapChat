package com.example.snapchat

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*

//Image Selection
class snapupdate : AppCompatActivity() {
    var pic: ImageView?=null
    var tex: EditText?=null
    var placeOn= UUID.randomUUID().toString()+".jpg"
    fun getPhoto(){
        val pico= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pico,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val img=data?.data
        if(requestCode==1&&resultCode== Activity.RESULT_OK){
            try{
                val imgo= MediaStore.Images.Media.getBitmap(this.contentResolver,img)
                pic?.setImageBitmap(imgo)
            }
            catch(e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }

    fun onClick(view: View){
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            getPhoto()
        }
        else{
            getPhoto()
        }
    }
    fun upload(view: View){
        pic?.isDrawingCacheEnabled=true
        pic?.buildDrawingCache()
        val bitmap=(pic?.drawable as BitmapDrawable).bitmap
        val ap= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ap)
        val choke=ap.toByteArray()
        val task= FirebaseStorage.getInstance().getReference().child("images").child(placeOn).putBytes(choke)
        task.addOnFailureListener(OnFailureListener {
            Log.i("success","update")
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>{taskSnapshot ->
            val url: Uri
            val uri: Task<Uri> = taskSnapshot.storage.downloadUrl
            while (!uri.isComplete());
            url = uri.getResult()!!
            Log.i("url",url.toString())
            val intent=Intent(this,send::class.java)
            intent.putExtra("imageUrl",url.toString())
            intent.putExtra("imageName",placeOn)
            intent.putExtra("message",tex?.text.toString())
            startActivity(intent)

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snapupdate)
        pic=findViewById(R.id.imageView2)
        tex=findViewById(R.id.editTextTextPersonName)
    }
}