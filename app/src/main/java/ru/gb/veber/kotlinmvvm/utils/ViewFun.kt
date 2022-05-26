package ru.gb.veber.kotlinmvvm.model

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import ru.gb.veber.kotlinmvvm.room.HistorySelect
import java.text.SimpleDateFormat
import java.util.*


fun View.showSnackBarResources(
    text: Int,
    actionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, resources.getString(text), length)
        .setAction(resources.getString(actionText), action).show()
}

fun View.showSnackBarError(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action).show()
}

fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun AppCompatImageView.loadSvg(keyIcon: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data("$WEATHER_URL_ICON$keyIcon.svg")
        .target(this)
        .build()
    imageLoader.enqueue(request)
}



