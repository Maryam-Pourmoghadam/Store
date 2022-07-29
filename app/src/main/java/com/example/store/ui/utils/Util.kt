package com.example.store.ui.utils

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

fun Fragment.enableLoadingView(detailsView: View, loadingView: View) {
    detailsView.isGone = true
    loadingView.isVisible = true
}

fun Fragment.disableLoadingView(detailsView: View, loadingView: View) {
    detailsView.isVisible= true
    loadingView.isGone = true
}

