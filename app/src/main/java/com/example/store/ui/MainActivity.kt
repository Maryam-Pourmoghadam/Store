package com.example.store.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.store.R
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}