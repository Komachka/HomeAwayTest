package com.kstor.homeawaytest.data.sp

import android.content.SharedPreferences
import com.kstor.homeawaytest.data.*
import javax.inject.Inject

class SharedPreferenceData @Inject constructor (private val preference: SharedPreferences) {
    fun setCityCenterInfo(citCenterlat: Double, citCenterlng: Double): Boolean {
        return isDataSaved(citCenterlat, PERSISTENT_STORAGE_KEY_LAT) &&
                isDataSaved(citCenterlng, PERSISTENT_STORAGE_KEY_LNG)
    }

    private fun isDataSaved(data: Double, key: String): Boolean {
        preference.edit()?.let { editor ->
            val storedData =
                preference.getFloat(key, PERSISTENT_STORAGE_DEF_VAL)
            if (storedData == PERSISTENT_STORAGE_DEF_VAL && storedData != data.toFloat()) { // TODO fix it to change place if it needed not only first time
                editor.putFloat(key, data.toFloat())
                editor.apply()
                return true
            }
            if (storedData == data.toFloat()) return true
        }
        return false
    }

    fun getCityCenterInfo(): Pair<Float, Float> {
        return preference.getFloat(
            PERSISTENT_STORAGE_KEY_LAT,
            PERSISTENT_STORAGE_DEF_VAL
        ) to preference.getFloat(PERSISTENT_STORAGE_KEY_LNG, PERSISTENT_STORAGE_DEF_VAL)
    }

    fun isDataValid(data:Pair<Float, Float>) : Boolean
    {
        return data.first != PERSISTENT_STORAGE_DEF_VAL && data.second != PERSISTENT_STORAGE_DEF_VAL
    }
}
