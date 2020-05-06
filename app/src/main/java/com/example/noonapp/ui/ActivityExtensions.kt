package com.example.noonapp.ui

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlin.math.max


fun Activity.showToastAboveKeyboard(message: String?, length: Int): Toast? {
    val rootView = this.findViewById<View>(android.R.id.content)
    var toast : Toast? = null
    if (rootView != null) {
        toast = Toast.makeText(this, message, length)
        val yOffset = max(0, rootView.height - toast.yOffset)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, yOffset)
        toast.show()
    }
    return toast
}


