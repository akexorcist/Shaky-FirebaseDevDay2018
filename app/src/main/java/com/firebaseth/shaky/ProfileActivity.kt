package com.firebaseth.shaky

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        buttonStartShaking?.setOnClickListener { goToShakeScreen() }
        setupActionBar()
        checkUserLogIn()

    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_app)
        }
    }

    private fun checkUserLogIn() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            clearDeviceShakeInfoInFirebase()
            updateUserProfileImage(user)
        } ?: run {
            goToLogInScreen()
        }
    }

    private fun updateUserProfileImage(user: FirebaseUser) {
        Glide.with(this)
                .load(user.photoUrl)
                .apply(RequestOptions().placeholder(R.drawable.ic_placeholder))
                .into(imageViewProfile)
    }

    private fun clearDeviceShakeInfoInFirebase() {
        FirebaseDatabase.getInstance().getReference("shakes/android/entries").removeValue()
    }

    private fun goToLogInScreen() {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    private fun goToShakeScreen() {
        startActivity(Intent(this, ShakeActivity::class.java))
    }
}