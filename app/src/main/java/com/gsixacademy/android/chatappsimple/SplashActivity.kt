package com.gsixacademy.android.chatappsimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    private lateinit var text: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // TODO Step 4 - for Rotate text animation !!!
        text = findViewById(R.id.simple)
//        text = findViewById(R.id.chat_app)

        // TODO Step 2 - for Rotate text animation !!!
//      animateChatApp()
        animateSimple()

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        },3300)

    }
    // TODO Step 3 - for Rotate text animation !!!
//    private fun animateChatApp() {
//        val rotate = AnimationUtils.loadAnimation(this, R.anim.text_animation_rotate)
//        text.animation = rotate
//    }

    private fun animateSimple() {
        val rotate = AnimationUtils.loadAnimation(this, R.anim.text_animation_rotate)
        text.animation = rotate
    }
}