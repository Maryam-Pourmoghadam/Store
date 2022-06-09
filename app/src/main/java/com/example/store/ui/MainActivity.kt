package com.example.store.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.store.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //splash
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val ivSplash = findViewById<ImageView>(R.id.iv_splash)
        val tvSplash = findViewById<TextView>(R.id.tv_splash)
        val frgmentContainer = findViewById<View>(R.id.fragmentContainerView)
        ivSplash.alpha = 0f
        tvSplash.alpha = 0f
        ivSplash.animate().setDuration(1500).alpha(1f)
        tvSplash.animate().setDuration(2000).alpha(1f).setStartDelay(500L).withEndAction {
            frgmentContainer.visibility = View.VISIBLE
            ivSplash.visibility = View.GONE
            supportActionBar?.show()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }
}