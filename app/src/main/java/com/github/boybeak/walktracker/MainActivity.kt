package com.github.boybeak.walktracker

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.github.boybeak.permission.Callback
import com.github.boybeak.permission.PH
import com.github.boybeak.walk.Walk
import com.github.boybeak.walk.WalkReader
import com.github.boybeak.walk.WalkWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val ww = WalkWriter.newInstance();
    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    //声明定位回调监听器
    var mLocationListener: AMapLocationListener = object : AMapLocationListener {

        override fun onLocationChanged(aMapLocation: AMapLocation) {
            ww.add(aMapLocation.latitude, aMapLocation.longitude, aMapLocation.altitude)
            Toast.makeText(this@MainActivity, "" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val file = File(this@MainActivity.filesDir, "Ab.walk")
        val walk = WalkReader.newInstance().read(file)
        if (walk != null) {
            val sb = StringBuilder(walk.toString()).append("\n")
            walk.nodes.forEach {
                sb.append("[").append(it.latitude).append(',').append(it.longitude).append(',').append(it.altitude).append("]\n")
            }
            text.text = sb
        }

        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener)
        val option = AMapLocationClientOption()
        option.setGpsFirst(true)
        mLocationClient!!.setLocationOption(option)

        PH.ask(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).go(this, object : Callback {
            override fun onGranted(permissions: List<String>) {
                ww.start(file)
                mLocationClient!!.startLocation()
            }

            override fun onDenied(permission: String) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ww.flush().end()
        mLocationClient!!.stopLocation()
    }

}