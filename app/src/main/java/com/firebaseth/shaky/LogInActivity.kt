package com.firebaseth.shaky

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_SIGN_IN = 142
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        buttonLogIn?.setOnClickListener { requestLogIn() }
        setupActionBar()
        checkAutoLogIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN &&
                resultCode == Activity.RESULT_OK) {
            onLoggedIn()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_app)
        }
    }

    private fun checkAutoLogIn() {
        FirebaseAuth.getInstance().currentUser?.let { onLoggedIn() }
    }

    private fun requestLogIn() {
        val providers: List<AuthUI.IdpConfig> = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_CODE_SIGN_IN)
    }

    private fun onLoggedIn() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }
}
