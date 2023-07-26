// Copyright (c) 2016, Miquel Martí <miquelmarti111@gmail.com>
// See LICENSE for licensing information
package cat.mvmike.minimalcalendarwidget.infrastructure.receiver

import android.content.Intent
import cat.mvmike.minimalcalendarwidget.BaseTest
import cat.mvmike.minimalcalendarwidget.application.RedrawWidgetUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CalendarChangeReceiverTest : BaseTest() {

    private val calendarChangeReceiver = CalendarChangeReceiver()

    @ParameterizedTest
    @ValueSource(
        strings = [
            "android.intent.action.TIME_SET",
            "android.intent.action.DATE_CHANGED",
            "android.intent.action.TIMEZONE_CHANGED",
            "android.intent.action.PROVIDER_CHANGED"
        ]
    )
    fun shouldRedrawWidgetWhenReceivingRegisteredIntentInAndroidManifest(intentAction: String) {
        mockIntent(intentAction)
        mockkObject(RedrawWidgetUseCase)
        every { RedrawWidgetUseCase.execute(context) } answers {}

        calendarChangeReceiver.onReceive(context, intent)

        verify { intent.action }
        verify { RedrawWidgetUseCase.execute(context) }
        confirmVerified(RedrawWidgetUseCase)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "some random string",
            Intent.ACTION_BATTERY_CHANGED,
            Intent.ACTION_TIME_TICK,
            Intent.ACTION_HEADSET_PLUG,
            ""
        ]
    )
    fun shouldDoNothingWhenReceivingNonRegisteredIntentInAndroidManifest(intentAction: String) {
        mockIntent(intentAction)
        mockkObject(RedrawWidgetUseCase)

        calendarChangeReceiver.onReceive(context, intent)

        verify { intent.action }
        confirmVerified(RedrawWidgetUseCase)
    }
}