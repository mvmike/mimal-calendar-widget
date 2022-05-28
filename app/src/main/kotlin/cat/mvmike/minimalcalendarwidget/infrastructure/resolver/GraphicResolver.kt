// Copyright (c) 2016, Miquel Martí <miquelmarti111@gmail.com>
// See LICENSE for licensing information
package cat.mvmike.minimalcalendarwidget.infrastructure.resolver

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import cat.mvmike.minimalcalendarwidget.R

object GraphicResolver {

    // ADD VISUAL COMPONENTS TO WIDGET

    fun addToWidget(widgetRemoteView: RemoteViews, remoteView: RemoteViews) = widgetRemoteView.addView(R.id.calendar_days_layout, remoteView)

    // MONTH YEAR HEADER

    fun createMonthAndYearHeader(
        widgetRemoteView: RemoteViews,
        monthAndYear: String,
        headerRelativeYearSize: Float
    ) {
        val monthAndYearSpSt = SpannableString(monthAndYear)
        monthAndYearSpSt.setSpan(RelativeSizeSpan(headerRelativeYearSize), monthAndYear.length - 4, monthAndYear.length, 0)
        widgetRemoteView.setTextViewText(R.id.month_year_label, monthAndYearSpSt)
    }

    // DAY HEADER

    fun createDaysHeaderRow(context: Context) = getById(context, R.layout.row_header)

    fun addToDaysHeaderRow(
        context: Context,
        daysHeaderRow: RemoteViews,
        text: String,
        layoutId: Int,
        viewId: Int,
        dayHeaderBackgroundColour: Int?
    ) {
        val dayRv = getById(context, layoutId)
        dayRv.setTextViewText(android.R.id.text1, text)
        dayHeaderBackgroundColour?.let {
            setBackgroundColor(dayRv, viewId, it)
        }
        daysHeaderRow.addView(R.id.row_container, dayRv)
    }

    // DAY

    fun createDaysRow(context: Context) = getById(context, R.layout.row_week)

    @SuppressWarnings("LongParameterList")
    fun addToDaysRow(
        context: Context,
        weekRow: RemoteViews,
        dayLayout: Int,
        viewId: Int,
        cellText: String,
        dayOfMonthInBold: Boolean,
        instancesColour: Int,
        instancesRelativeSize: Float,
        dayBackgroundColour: Int?,
        generalRelativeSize: Float
    ) {
        val daySpSt = SpannableString(cellText)
        if (dayOfMonthInBold) {
            daySpSt.setSpan(StyleSpan(Typeface.BOLD), 0, daySpSt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            daySpSt.setSpan(StyleSpan(Typeface.BOLD), daySpSt.length - 1, daySpSt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        daySpSt.setSpan(ForegroundColorSpan(instancesColour), daySpSt.length - 1, daySpSt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        daySpSt.setSpan(RelativeSizeSpan(generalRelativeSize), 0, daySpSt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        daySpSt.setSpan(RelativeSizeSpan(instancesRelativeSize), daySpSt.length - 1, daySpSt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val dayRv = getById(context, dayLayout)
        dayRv.setTextViewText(android.R.id.text1, daySpSt)
        dayBackgroundColour?.let {
            setBackgroundColor(dayRv, viewId, it)
        }
        weekRow.addView(R.id.row_container, dayRv)
    }

    // COLOUR

    fun getColour(context: Context, id: Int) = ContextCompat.getColor(context, id)

    fun getColourAsString(context: Context, id: Int) = context.resources.getString(id)

    fun parseColour(colourString: String) = Color.parseColor(colourString)

    fun setBackgroundColor(
        remoteViews: RemoteViews,
        viewId: Int,
        colour: Int
    ) = remoteViews.setInt(viewId, "setBackgroundColor", colour)

    // INTERNAL UTILS

    private fun getById(context: Context, layoutId: Int) = RemoteViews(context.packageName, layoutId)

}
