package com.firebaseth.shaky

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.hardware.SensorManager
import com.squareup.seismic.ShakeDetector

class ShakeManager(context: Context) : LifecycleObserver {
    private var sensorManager: SensorManager? = null
    private var shakeDetector: ShakeDetector? = null
    private var onDeviceShaked: (() -> Unit)? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(ShakeDetector.Listener { onDeviceShaked?.invoke() })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startDetector() {
        shakeDetector?.start(sensorManager)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopDetector() {
        shakeDetector?.stop()
    }

    fun doOnDeviceShaked(onDeviceShaked: () -> Unit) {
        this.onDeviceShaked = onDeviceShaked
    }
}