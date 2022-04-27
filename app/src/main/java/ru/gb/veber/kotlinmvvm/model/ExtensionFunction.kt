package ru.gb.veber.kotlinmvvm.model

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val FORMAT_DATE = "E, dd MMMM H:m"
const val FORMAT_DATE_DTO = "yyyy-MM-dd"
const val FORMAT_HOUR = "H:m"
const val FORMAT_WEEK = "EEEE"

fun Date.formatDate(): String = SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(this)
fun Date.formatHour(): String = SimpleDateFormat(FORMAT_HOUR, Locale.getDefault()).format(this)
fun Date.formatWeek(): String = SimpleDateFormat(FORMAT_WEEK, Locale.getDefault()).format(this)

fun dayWeekFromString(dateString: String) = SimpleDateFormat(FORMAT_DATE_DTO,Locale.getDefault()).parse(dateString)

fun String.addDegree(): String = this + "\u00B0"

fun getSlash(min: Int, max: Int) = min.toString().addDegree() + "/" + max.toString().addDegree()

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
    length: Int = Snackbar.LENGTH_INDEFINITE
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


