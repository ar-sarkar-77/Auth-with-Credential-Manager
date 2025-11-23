package com.anondo.loginflowwithauth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.anondo.loginflowwithauth.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth =  FirebaseAuth.getInstance()



    }

    override fun onStart() {
        super.onStart()
        var curretUser = auth.currentUser

        if (curretUser!=null){

            binding.userName.text = curretUser.displayName
            binding.userEmail.text = curretUser.email

            binding.profileImage.load(curretUser.photoUrl)

        }else{
            startActivity(Intent(this , LogIn::class.java))
            finish()
        }
    }

}