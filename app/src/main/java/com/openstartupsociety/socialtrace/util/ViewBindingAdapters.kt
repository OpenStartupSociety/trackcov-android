package com.openstartupsociety.socialtrace.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

@BindingAdapter("error")
fun setError(view: TextInputLayout, error: String?) {
    view.error = error
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, drawable: Drawable) {
    imageView.load(drawable)
}

@BindingAdapter("loadImage", "circleCrop", requireAll = false)
fun loadImage(imageView: ImageView, url: String, circleCrop: Boolean = false) {
    imageView.load(url) {
        if (circleCrop) transformations(CircleCropTransformation())
    }
}

@BindingAdapter("loadBarcode", "circleCrop", requireAll = false)
fun loadBarcode(
    imageView: ImageView,
    text: String,
    circleCrop: Boolean = false
) {
    val bitmap = BarcodeEncoder().encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400)
    imageView.load(bitmap) {
        if (circleCrop) transformations(CircleCropTransformation())
    }
}
