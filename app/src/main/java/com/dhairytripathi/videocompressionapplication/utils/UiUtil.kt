package com.dhairytripathi.videocompressionapplication.utils

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.application.VideoCompressionApplication.Companion.appContext

object UiUtil {

    private var dialog: AlertDialog? = null

    fun displayProgress(context: Context) {
        // This has been called from worker thread
        if (Looper.myLooper() != Looper.getMainLooper()) return

        hideProgress()

        if (context is Activity) {
            if (context.isDestroyed || context.isFinishing) return
            dialog = setProgressDialog(context)
            dialog!!.setCancelable(false) // wait until the process is finished.
            dialog!!.show()
        }
    }

    private fun setProgressDialog(context:Context): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = context.getText(R.string.loading)
        tvText.textSize = 18.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    fun hideProgress() {

        if (dialog != null && dialog!!.isShowing ) {
            val window = dialog!!.window ?: return
            val decor: View = window.decorView
            if (decor.parent != null) {
                dialog!!.dismiss()
                dialog = null
            }

        }
    }

    fun showErrorToast() {
        val toastMessage = appContext.getString(R.string.unexpected_error_occurred_at_our_end)
        ToastUtils.showToast(toastMessage, appContext)
    }
}