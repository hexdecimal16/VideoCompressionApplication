package com.dhairytripathi.videocompressionapplication.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.application.VideoCompressionApplication.Companion.appContext

object UiUtil {

    private var dialog: ProgressDialog? = null

    fun displayProgress(context: Context, msg: String?) {
        // This has been called from worker thread
        if (Looper.myLooper() != Looper.getMainLooper()) return

        hideProgress()

        if (context is Activity) {
            val activity = context as Activity
            if (activity.isDestroyed || activity.isFinishing) return
            dialog = ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT)
            dialog!!.isIndeterminate = true
            dialog!!.setCancelable(false)
            dialog!!.setMessage(msg)
            dialog!!.show()
        }
    }

    fun hideProgress() {

        if (dialog != null && dialog!!.isShowing ) {
            val window = dialog!!.window ?: return
            val decor: View = window.decorView
            dialog!!.dismiss()
            if (decor.getParent() != null) {
                dialog!!.dismiss()
                dialog = null
            }

        }
    }

    fun showUnexpectedErrorToast() {
        val toastMessage = appContext.getString(R.string.unexpected_error_occurred_at_our_end)

        Toast.makeText(appContext, toastMessage, Toast.LENGTH_SHORT).show()
    }
}