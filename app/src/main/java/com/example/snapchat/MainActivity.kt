package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
//page1
class MainActivity : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    var user: EditText?=null
    var pass: EditText?=null
    fun back(view: View){
        val press=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        press.hideSoftInputFromWindow(currentFocus?.windowToken,0)
    }
    fun signUp(view: View) {
        val press=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        press.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        mAuth.createUserWithEmailAndPassword(user?.text.toString(), pass?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.i("singup", "sucess")
                    FirebaseDatabase.getInstance().getReference().child("users")
                        .child(mAuth.currentUser!!.uid).child("email").setValue(user?.text.toString())
                    Toast.makeText(this, "sign up success, Login back", Toast.LENGTH_SHORT).show()
                    user?.text?.clear()
                    pass?.text?.clear()
                } else {
                    Toast.makeText(this, "sign up failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun signIn(view: View){
        val press=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        press.hideSoftInputFromWindow(currentFocus?.windowToken,0)

        mAuth.signInWithEmailAndPassword(user?.text.toString(), pass?.text.toString())
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    login()
                }
                else{
                    Toast.makeText(this, "check your username and password", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun login(){
        val grt= Intent(this,feed::class.java)
        startActivity(grt)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user=findViewById(R.id.editTextTextPersonName3)
        pass=findViewById(R.id.editTextTextPersonName2)

        if(mAuth.currentUser!=null){
            login()
        }
    }
}