package com.alexis.alexisfeedback.Utils

import android.app.Activity
import android.content.Context


/**
 * This common class to store the require data by using SharedPreferences.
 */
object SessionSave {
    //Store data's into sharedPreference
    fun saveUserSession(
        key: String?,
        value: String?,
        context: Context
    ) {
        val editor =
            context.getSharedPreferences("USER_DETAIL", Activity.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Get Data's from SharedPreferences
    fun getUserSession(key: String?, context: Context): String? {
        val prefs =
            context.getSharedPreferences("USER_DETAIL", Activity.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    fun deleteSession(context: Context) {
        val editor =
            context.getSharedPreferences("USER_DETAIL", Activity.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}
