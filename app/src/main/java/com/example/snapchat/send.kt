package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
//person Selction
class send : AppCompatActivity() {
    var vews:ListView?=null
    var emails:ArrayList<String> = ArrayList()
    var keys:ArrayList<String> = ArrayList()
    var date:ArrayList<String> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        vews=findViewById(R.id.lsit)
        var adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        vews?.adapter=adapter
        FirebaseDatabase.getInstance().getReference().child("users")
            .addChildEventListener(object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val data=snapshot.child("email").value as String
                    emails.add(data)
                    val dt=snapshot.key as String
                    keys.add(dt)
                    date.add(snapshot.toString())
                    adapter.notifyDataSetChanged()
                }override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}

            })
        vews?.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
            val snapMap: Map<String, String?> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email,"Image" to intent.getStringExtra("imageName"),
                "ImageUrl" to intent.getStringExtra("imageUrl"),"message" to intent.getStringExtra("message"))
            FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(position))
                .child("snaps").push().setValue(snapMap)
            val intent=Intent(this,feed::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

}