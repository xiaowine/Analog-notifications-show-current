@file:Suppress("DEPRECATION")

package cn.xiaowine.current

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.Gravity
import android.widget.TextView


@SuppressLint("ExportedPreferenceActivity")
class SettingsActivity : Activity() {
    @Deprecated("Deprecated in Java")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
        val textView = TextView(this).apply {
            text = "不显示，就强行停止重新打开\nMiui和ColorOS魔改了系统的通知，导致无法显示\n可以安装模块恢复"
            gravity = Gravity.CENTER
        }
        setContentView(textView)
        startService(Intent(this, Service::class.java))
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
        }
    }
}