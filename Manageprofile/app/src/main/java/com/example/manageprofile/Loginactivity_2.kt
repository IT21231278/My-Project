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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Loginactivity_2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity2)

        // Initialize Firebase Auth
        auth = Firebase.auth
        userEmail = findViewById(R.id.logEmail)
        userPassword = findViewById(R.id.logPassword)


        //mehema liyannath puluwan
        findViewById<TextView>(R.id.btnregister).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Signupactivity::class.java));
        });


        // mehema code eka liyannath puluwan
        findViewById<Button>(R.id.btnlogin).setOnClickListener {


            val uEmail = userEmail.text.toString()
            val uPassword = userPassword.text.toString()

            auth.signInWithEmailAndPassword(uEmail, uPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(this, "Home eka set karanna", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Productditalis2::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, Productditalis2::class.java))
           // Toast.makeText(this, "Home eka set karanna", Toast.LENGTH_LONG).show()
        }
    }
}