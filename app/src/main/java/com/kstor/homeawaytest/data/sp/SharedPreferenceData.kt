package com.kstor.homeawaytest.data.sp

import android.content.Context
import android.content.SharedPreferences
import com.kstor.homeawaytest.data.*

class SharedPreferenceData(val context: Context) {
    private val preference: SharedPreferences = context.applicationContext.getSharedPreferences(
        PERSISTENT_STORAGE_NAME,
        Context.MODE_PRIVATE
    )

    fun setCityCenterInfo(citCenterlat: Double, citCenterlng: Double) {
        preference.edit()?.let { editor ->
            val latData =
                preference.getFloat(PERSISTENT_STORAGE_KEY_LAT, PERSISTENT_STORAGE_DEF_VAL)
            if (latData == PERSISTENT_STORAGE_DEF_VAL && latData != citCenterlat.toFloat()) {
                editor.putFloat(PERSISTENT_STORAGE_KEY_LAT, citCenterlat.toFloat())
                editor.apply()
            }
            val lngData =
                preference.getFloat(PERSISTENT_STORAGE_KEY_LNG, PERSISTENT_STORAGE_DEF_VAL)
            if (lngData == PERSISTENT_STORAGE_DEF_VAL && lngData != citCenterlng.toFloat()) {
                editor.putFloat(PERSISTENT_STORAGE_KEY_LAT, citCenterlng.toFloat())
                editor.apply()
            }
        }
    }

    fun getCityCenterInfo(): Pair<Float, Float> {
        return preference.getFloat(
            PERSISTENT_STORAGE_KEY_LAT,
            PERSISTENT_STORAGE_DEF_VAL
        ) to preference.getFloat(PERSISTENT_STORAGE_KEY_LNG, PERSISTENT_STORAGE_DEF_VAL)
    }
}
