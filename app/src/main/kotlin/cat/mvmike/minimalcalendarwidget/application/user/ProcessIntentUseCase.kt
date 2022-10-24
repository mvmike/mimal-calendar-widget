// Copyright (c) 2016, Miquel Martí <miquelmarti111@gmail.com>
// See LICENSE for licensing information
package cat.mvmike.minimalcalendarwidget.application.user

import android.content.Context
import cat.mvmike.minimalcalendarwidget.application.RedrawWidgetUseCase
import cat.mvmike.minimalcalendarwidget.domain.intent.ActionableView.OPEN_CALENDAR
import cat.mvmike.minimalcalendarwidget.domain.intent.ActionableView.OPEN_CONFIGURATION
import cat.mvmike.minimalcalendarwidget.infrastructure.activity.CalendarActivity
import cat.mvmike.minimalcalendarwidget.infrastructure.activity.ConfigurationActivity
import cat.mvmike.minimalcalendarwidget.infrastructure.activity.PermissionsActivity
import cat.mvmike.minimalcalendarwidget.infrastructure.resolver.CalendarResolver
import cat.mvmike.minimalcalendarwidget.infrastructure.resolver.SystemResolver

object ProcessIntentUseCase {

    fun execute(context: Context, action: String?) = when (action) {
        OPEN_CONFIGURATION.action -> { { ConfigurationActivity.start(context) } }
        OPEN_CALENDAR.action -> { { CalendarActivity.start(context, SystemResolver.getSystemInstant()) } }
        else -> null
    }?.let { context.executeAndRedrawOrAskForPermissions(it) }

    private fun Context.executeAndRedrawOrAskForPermissions(function: () -> Unit) =
        when (CalendarResolver.isReadCalendarPermitted(this)) {
            true -> {
                function.invoke()
                RedrawWidgetUseCase.execute(this, true)
            }
            else -> PermissionsActivity.start(this)
        }
}
