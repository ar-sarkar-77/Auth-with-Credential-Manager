package com.anondo.loginflowwithauth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.anondo.loginflowwithauth.databinding.ActivityLogInBinding
import com.google.android.gms.common.SignInButton
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LogIn : AppCompatActivity() {

    lateinit var binding : ActivityLogInBinding
    lateinit var credentialManager: CredentialManager
    lateinit var credentialRequest: GetCredentialRequest
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager.create(this)
        auth = FirebaseAuth.getInstance()

        var googleOption  = GetSignInWithGoogleOption.Builder(
            getString(R.string.googleLogIn_ClientId))
            .setNonce(java.util.UUID.randomUUID().toString())
            .build()

        credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()


        binding.signInBtn.setOnClickListener {

            Log.d("Tag" , "Button Clicked")
            credentialGoogle(this)

        }


    }

    //----------------------------------------------------------------------------------------------
    fun credentialGoogle(context: Context) {

        credentialManager.getCredentialAsync(
            context,
            credentialRequest,
            null,
            Runnable::run,
            object : CredentialManagerCallback<GetCredentialResponse , GetCredentialException>{

                override fun onResult(result: GetCredentialResponse) {

                    var credential = result.credential
                    var credentialIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                    var idToken = credentialIdToken.idToken

                    var authCredential = GoogleAuthProvider.getCredential(idToken ,null)

                    auth.signInWithCredential(authCredential)
                        .addOnSuccessListener {
                            startActivity(Intent(applicationContext , MainActivity::class.java))
                            finish()
                        }

                }

                override fun onError(e: GetCredentialException) {
                    Log.e("CredentialError", "Error: ${e.javaClass.name} — ${e.message}")
                }
            }
        )
    }



}