// Copyright (c) 2016, Miquel Martí <miquelmarti111@gmail.com>
// See LICENSE for licensing information
package cat.mvmike.minimalcalendarwidget.infrastructure.resolver

import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.core.text.util.LocalePreferences
import cat.mvmike.minimalcalendarwidget.BaseTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.util.Locale
import java.util.stream.Stream

internal class SystemResolverTest : BaseTest() {

    @ParameterizedTest
    @CsvSource(
        "mon,MONDAY",
        "tue,TUESDAY",
        "wed,WEDNESDAY",
        "thu,THURSDAY",
        "fri,FRIDAY",
        "sat,SATURDAY",
        "sun,SUNDAY"
    )
    fun getSystemFirstDayOfWeek_shouldReturnLocalePreferenceValue(
        localPreferenceValue: String,
        expectedDayOfWeek: DayOfWeek
    ) {
        mockkStatic(LocalePreferences::class)
        every {
            LocalePreferences.getFirstDayOfWeek()
        } returns localPreferenceValue

        val result = SystemResolver.getSystemFirstDayOfWeek()

        assertThat(result).isEqualTo(expectedDayOfWeek)
        verify { SystemResolver.getSystemFirstDayOfWeek() }
    }

    @ParameterizedTest
    @MethodSource("getLocalDatesWithExpectations")
    fun getSystemFirstDayOfWeek_shouldReturnLocaleDefaultWhenNoPreferenceValue(
        locale: Locale,
        expectedDayOfWeek: DayOfWeek
    ) {
        mockkStatic(LocalePreferences::class)
        every { LocalePreferences.getFirstDayOfWeek() } returns ""
        mockkStatic(Resources::class)
        val resources = mockk<Resources>()
        every { Resources.getSystem() } returns resources
        val configuration = mockk<Configuration>()
        every { resources.configuration } returns configuration
        val localeList = mockk<LocaleList>()
        every { configuration.locales } returns localeList
        every { localeList[0] } returns locale

        val result = SystemResolver.getSystemFirstDayOfWeek()

        assertThat(result).isEqualTo(expectedDayOfWeek)
        verify { SystemResolver.getSystemFirstDayOfWeek() }
    }

    private fun getLocalDatesWithExpectations() = Stream.of(
        of(Locale.ENGLISH, DayOfWeek.SUNDAY),
        of(Locale("en", "GB"), DayOfWeek.MONDAY),
        of(Locale("ru", "RU"), DayOfWeek.MONDAY),
        of(Locale("en", "US"), DayOfWeek.SUNDAY),
        of(Locale("ca", "ES"), DayOfWeek.MONDAY),
        of(Locale("es", "ES"), DayOfWeek.MONDAY),
        of(Locale("fr", "FR"), DayOfWeek.MONDAY),
        of(Locale("iw", "IL"), DayOfWeek.SUNDAY),
        of(Locale("he", "IL"), DayOfWeek.SUNDAY),
        of(Locale("yue", "CN"), DayOfWeek.SUNDAY),
        of(Locale("tr", "TR"), DayOfWeek.MONDAY)
    )
}