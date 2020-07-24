package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
//page2
class feed : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    var check:ListView?=null
    var emails:ArrayList<String> = ArrayList()
    var snaps:ArrayList<DataSnapshot> = ArrayList()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.snap)
        {
            val intent= Intent(this,snapupdate::class.java)
            startActivity(intent)
        }
        else if(item.itemId==R.id.out){
            mAuth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate=menuInflater
        inflate.inflate(R.menu.pagetwo,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        check=findViewById(R.id.fool)
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        check?.adapter=adapter

        FirebaseDatabase.getInstance().getReference().child("users")
            .child(mAuth.currentUser!!.uid).child("snaps")
            .addChildEventListener(object:ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    emails.add(snapshot.child("from").value as String)
                    snaps.add(snapshot)
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var index=0
                    for(snap:DataSnapshot in snaps){
                        if(snap.key==snapshot.key){
                            snaps.removeAt(index)
                            emails.removeAt(index)
                        }
                        index++
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        check?.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
            val got=snaps.get(position)
            val intent=Intent(this,Image::class.java)
            intent.putExtra("imageName",got.child("Image").value as String)
            intent.putExtra("imageUrl",got.child("ImageUrl").value as String)
            intent.putExtra("message",got.child("message").value as String)
            intent.putExtra("ID",got.key)
            startActivity(intent)
        }
    }

}