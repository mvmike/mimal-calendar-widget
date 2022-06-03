// Copyright (c) 2016, Miquel Martí <miquelmarti111@gmail.com>
// See LICENSE for licensing information
package cat.mvmike.minimalcalendarwidget.domain.configuration

import cat.mvmike.minimalcalendarwidget.BaseTest
import cat.mvmike.minimalcalendarwidget.domain.configuration.item.Colour
import cat.mvmike.minimalcalendarwidget.domain.configuration.item.SymbolSet
import cat.mvmike.minimalcalendarwidget.domain.configuration.item.Theme
import cat.mvmike.minimalcalendarwidget.domain.configuration.item.Transparency
import io.mockk.Called
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.DayOfWeek

internal class ConfigurationTest : BaseTest() {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 5, 17, 50, 51, 72, 80, 99, 100])
    fun getWidgetTransparency_shouldReturnSharedPreferencesValue(percentage: Int) {
        val transparency = Transparency(percentage)
        mockSharedPreferences()
        mockWidgetTransparency(transparency)

        val result = Configuration.WidgetTransparency.get(context)

        assertThat(result).isEqualTo(transparency)
        verifyWidgetTransparency()
        verify { editor wasNot Called }
    }

    @ParameterizedTest
    @EnumSource(value = Theme::class)
    fun getCalendarTheme_shouldReturnSharedPreferencesValue(theme: Theme) {
        mockSharedPreferences()
        mockWidgetTheme(theme)

        val result = EnumConfiguration.WidgetTheme.get(context)

        assertThat(result).isEqualTo(theme)
        verifyWidgetTheme()
        verify { editor wasNot Called }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class)
    fun getFirstDayOfWeek_shouldReturnSharedPreferencesValue(dayOfWeek: DayOfWeek) {
        mockSharedPreferences()
        mockFirstDayOfWeek(dayOfWeek)

        val result = EnumConfiguration.FirstDayOfWeek.get(context)

        assertThat(result).isEqualTo(dayOfWeek)
        verifyFirstDayOfWeek()
        verify { editor wasNot Called }
    }

    @ParameterizedTest
    @EnumSource(value = SymbolSet::class)
    fun getInstancesSymbolSet_shouldReturnSharedPreferencesValue(symbolSet: SymbolSet) {
        mockSharedPreferences()
        mockInstancesSymbolSet(symbolSet)

        val result = EnumConfiguration.InstancesSymbolSet.get(context)

        assertThat(result).isEqualTo(symbolSet)
        verifyInstancesSymbolSet()
        verify { editor wasNot Called }
    }

    @ParameterizedTest
    @EnumSource(value = Colour::class)
    fun getInstancesColour_shouldReturnSharedPreferencesValue(colour: Colour) {
        mockSharedPreferences()
        mockInstancesColour(colour)

        val result = EnumConfiguration.InstancesColour.get(context)

        assertThat(result).isEqualTo(colour)
        verifyInstancesColour()
        verify { editor wasNot Called }
    }

    @Test
    fun clearAllConfiguration_shouldRemoveAllApplicationPreferences() {
        mockSharedPreferences()
        clearAllConfiguration(context)

        verifySharedPreferencesAccess()
        verifySharedPreferencesEdit()
        verify { editor.clear() }
        verify { editor.apply() }
    }
}
