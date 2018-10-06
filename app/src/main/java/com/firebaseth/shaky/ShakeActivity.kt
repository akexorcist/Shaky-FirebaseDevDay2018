package com.firebaseth.shaky

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_shake.*


class ShakeActivity : AppCompatActivity() {
    private var reference: DatabaseReference? = null
    private var shakeCount = 0

    companion object {
        const val EXTRA_SHAKE_COUNT = "com.firebaseth.shaky.shake_activity.shake_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)

        hideSystemUi()
        setupActionBar()
        setupFirebaseDatabase()
        setupShakeDetector()
        initShakeInfo()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(EXTRA_SHAKE_COUNT, shakeCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        shakeCount = savedInstanceState?.getInt(EXTRA_SHAKE_COUNT) ?: 0
        updateShakingInfo()
    }

    private fun hideSystemUi() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_app)
        }
    }

    private fun setupShakeDetector() {
        val shakeManager = ShakeManager(this)
        shakeManager.doOnDeviceShaked { onDeviceShaked() }
        lifecycle.addObserver(shakeManager)
    }

    private fun setupFirebaseDatabase() {
        reference = FirebaseDatabase.getInstance().getReference("shakes/android/entries")
    }

    private fun initShakeInfo() {
        shakeCount = 0
        updateShakingInfo()
    }

    private fun updateShakingInfo() {
        textViewShakeCounter?.text = shakeCount.toString()
    }

    private fun onDeviceShaked() {
        shakeCount++
        updateShakingInfo()
        pushDeviceShakedToFirebase()
    }

    private fun pushDeviceShakedToFirebase() {
        reference?.push()?.setValue(true)
    }
}