@file:Suppress("DEPRECATION")

package cn.xiaowine.current.utils


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


object Utils {

    @SuppressLint("WorldReadableFiles")
    fun getSP(context: Context, key: String?): SharedPreferences? {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE)
    }



    fun Any?.isNull(callback: () -> Unit) {
        if (this == null) callback()
    }

    fun Any?.isNotNull(callback: () -> Unit) {
        if (this != null) callback()
    }

    fun Any?.isNull() = this == null

    fun Any?.isNotNull() = this != null
}