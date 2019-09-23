package com.kstor.homeawaytest

import android.content.SharedPreferences
import com.kstor.homeawaytest.data.*
import com.kstor.homeawaytest.data.sp.SharedPreferenceData
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedPreferenceDataTest {

    private lateinit var mockSharedPreferencesData: SharedPreferenceData
    private lateinit var mockBrokenSharedPreferencesData: SharedPreferenceData
    private lateinit var mockNullSharedPreferencesData: SharedPreferenceData

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences
    @Mock
    private lateinit var mockBrokenSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockNullSharedPreferences: SharedPreferences
    @Mock private lateinit var mockEditor: SharedPreferences.Editor
    @Mock private lateinit var mockBrokenEditor: SharedPreferences.Editor

    @Before
    @Throws(Exception::class)
    fun before() {
        mockSharedPreferencesData = createMockSharedPreferenceData()
        mockBrokenSharedPreferencesData = createBrokenMockSharedPreference()
        mockNullSharedPreferencesData = createNullSharedPreference()
    }

    private fun createNullSharedPreference(): SharedPreferenceData {
        given(mockNullSharedPreferences.edit()).willReturn(null)
        return SharedPreferenceData(mockNullSharedPreferences)
    }

    private fun createMockSharedPreferenceData(): SharedPreferenceData {
        given(
            mockSharedPreferences.getFloat(
                PERSISTENT_STORAGE_KEY_LAT,
                PERSISTENT_STORAGE_DEF_VAL
            )
        )
            .willReturn(TEST_LAT)
        given(
            mockSharedPreferences.getFloat(
                PERSISTENT_STORAGE_KEY_LNG,
                PERSISTENT_STORAGE_DEF_VAL
            )
        )
            .willReturn(TEST_LNG)
        lenient().`when`(mockEditor.commit()).thenReturn(true)
        given(mockSharedPreferences.edit()).willReturn(mockEditor)
        return SharedPreferenceData(mockSharedPreferences)
    }

    private fun createBrokenMockSharedPreference(): SharedPreferenceData {
        given(
            mockBrokenSharedPreferences.getFloat(
                PERSISTENT_STORAGE_KEY_LAT,
                PERSISTENT_STORAGE_DEF_VAL
            )
        )
            .willReturn(PERSISTENT_STORAGE_DEF_VAL)
        given(
            mockBrokenSharedPreferences.getFloat(
                PERSISTENT_STORAGE_KEY_LNG,
                PERSISTENT_STORAGE_DEF_VAL
            )
        )
            .willReturn(PERSISTENT_STORAGE_DEF_VAL)
        lenient().`when`(mockBrokenEditor.commit()).thenReturn(false)
        given(mockBrokenSharedPreferences.edit()).willReturn(mockBrokenEditor)
        return SharedPreferenceData(mockBrokenSharedPreferences)
    }

    @Test
    fun sharedPreferencesData_saveAndReadCityCenterData() {
        Assert.assertTrue(mockSharedPreferencesData.setCityCenterInfo(TEST_LAT.toDouble(), TEST_LNG.toDouble()))
        val (lat, lng) = mockSharedPreferencesData.getCityCenterInfo()
        Assert.assertEquals(lat, TEST_LAT)
        Assert.assertEquals(lng, TEST_LNG)
    }

    @Test
    fun sharedPreferencesData_saveAndReadCityCenter_ReturnsFalse() {
        mockBrokenSharedPreferencesData.setCityCenterInfo(TEST_WRONG_LAT, TEST_WRONG_LNG)
        val (lat, lng) = mockBrokenSharedPreferencesData.getCityCenterInfo()
        Assert.assertEquals(lat, PERSISTENT_STORAGE_DEF_VAL)
        Assert.assertEquals(lng, PERSISTENT_STORAGE_DEF_VAL)
    }

    @Test
    fun sharedPreferencesData_saveAndReadCityCenterWithNullEditor_ReturnsFalse() {
        Assert.assertFalse(mockNullSharedPreferencesData.setCityCenterInfo(TEST_LAT.toDouble(), TEST_LNG.toDouble()))
        val (lat, lng) = mockNullSharedPreferencesData.getCityCenterInfo()
        Assert.assertEquals(lat, PERSISTENT_STORAGE_DEF_VAL)
        Assert.assertEquals(lng, PERSISTENT_STORAGE_DEF_VAL)
    }

    companion object {
        private const val TEST_LAT = 50F
        private const val TEST_LNG = 40F
        private const val TEST_WRONG_LAT = 0.0
        private const val TEST_WRONG_LNG = 0.0
    }
}
