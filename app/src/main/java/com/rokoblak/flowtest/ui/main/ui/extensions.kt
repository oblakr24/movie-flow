package com.rokoblak.flowtest.ui.main.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.rokoblak.flowtest.ui.main.networkAvailableFlow


fun Context.showToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(@StringRes resId: Int) {
    requireContext().showToast(resId)
}

fun Fragment.showToast(message: String) {
    requireContext().showToast(message)
}

fun Fragment.getNetworkLiveData() = requireContext().networkAvailableFlow().asLiveData()

fun Context.resolveDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun ImageView.setDrawable(@DrawableRes resId: Int) {
    setImageDrawable(context.resolveDrawable(resId))
}

fun TextView.setTextColorRes(@ColorRes resId: Int) {
    setTextColor(context.resolveColor(resId))
}

fun Fragment.resolveDrawable(@DrawableRes resId: Int) = requireContext().resolveDrawable(resId)

fun Context.resolveColor(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun View.resolveColor(@ColorRes resId: Int) = context.resolveColor(resId)

fun Fragment.resolveColor(@ColorRes resId: Int) = requireContext().resolveColor(resId)

fun Context.resolveDimen(@DimenRes resId: Int) = resources.getDimension(resId)

fun Fragment.resolveDimen(@DimenRes resId: Int) = requireContext().resolveDimen(resId)

fun RecyclerView.ViewHolder.getString(@StringRes resId: Int) = itemView.context.getString(resId)

