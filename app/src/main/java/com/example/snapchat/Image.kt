package com.example.snapchat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
//Snap opening page
class Image : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    var pic:ImageView?=null
    var text:TextView?=null
    inner class imageDownlaoad: AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            try{
                val url= URL(params[0])
                val connection=url.openConnection() as HttpURLConnection
                connection.connect()
                val c=connection.inputStream
                return BitmapFactory.decodeStream(c)
            }
            catch(e:Exception){
                e.printStackTrace()
                return null
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        pic=findViewById(R.id.imageView3)
        text=findViewById(R.id.textView)
        text?.text=intent.getStringExtra("message")
        val update=imageDownlaoad()
        val pi:Bitmap
        try{
            pi=update.execute(intent.getStringExtra("imageUrl")).get()
            pic?.setImageBitmap(pi)
        } catch(e:Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser!!.uid)
            .child("snaps").child(intent.getStringExtra("ID")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()
    }
}