package com.example.myapplication.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R

class SplachScreen : AppCompatActivity() {
    var preferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)
        preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        Thread(Runnable {
            Thread.sleep(3000)
            var currentUser=preferences!!.getString("CURRENTUSER", null)
            if(currentUser != null ){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("CURRENTUSER",currentUser)
                startActivity(intent)}
            else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }).start()
    }
}
