package com.example.store.model

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SharedFunctions {
    fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view, message,
            Snackbar.LENGTH_SHORT
        ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
            .show()
    }
}