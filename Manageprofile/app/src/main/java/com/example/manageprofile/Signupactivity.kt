package com.example.manageprofile

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Signupactivity : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var userComPass: EditText

    private lateinit var auth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupactivity)

        auth = Firebase.auth

        fullName = findViewById(R.id.userFullName)
        userEmail = findViewById(R.id.userEmail)
        userPassword = findViewById(R.id.userPassword)
        userComPass = findViewById(R.id.userComPass)

        if(userPassword.text.toString().equals(userComPass.text.toString())){

        }else{
            Toast.makeText(
                baseContext,
                "Password is not match .",
                Toast.LENGTH_SHORT,
            ).show()
        }

        findViewById<Button>(R.id.btnsignup).setOnClickListener(View.OnClickListener {

            if(userPassword.text.toString() != userComPass.text.toString() ){
                Toast.makeText(this,"error password and confirm Password",Toast.LENGTH_LONG).show()
            }else{
                val uEmail = userEmail.text.toString()
                val uPassword = userPassword.text.toString()



                auth.createUserWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUi(user)

                        startActivity(Intent(this, Loginactivity_2::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }

                }.addOnFailureListener {

                }
            }

        })

    findViewById<TextView>(R.id.btnloginregister).setOnClickListener(View.OnClickListener {
        startActivity(Intent(this,Loginactivity_2::class.java))
    })

    }

    private fun updateUi(user: FirebaseUser?) {

        val user = hashMapOf(
            "userId" to auth.currentUser?.uid.toString(),
            "FUll NAme" to fullName.text.toString(),
            "Email" to userEmail.text.toString(),
            "Password" to userPassword.text.toString(),
        )
        db.collection("user")
            .add(user).addOnSuccessListener { docunentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${docunentReference.id}")
                Toast.makeText(
                    baseContext,
                    "added with Data.",
                    Toast.LENGTH_SHORT,
                ).show()
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(
                    baseContext,
                    "Error adding.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

}