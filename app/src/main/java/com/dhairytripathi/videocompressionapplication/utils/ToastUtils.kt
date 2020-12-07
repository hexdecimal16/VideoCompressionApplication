package com.dhairytripathi.videocompressionapplication.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {

    fun showToast(msg: String?, ctx: Context?) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}