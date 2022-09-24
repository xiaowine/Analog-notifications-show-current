package cn.xiaowine.current

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Icon
import android.os.BatteryManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextPaint
import cn.fuckhome.xiaowine.utils.ActivityOwnSP
import cn.fuckhome.xiaowine.utils.ActivityOwnSP.ownSP as config
import kotlin.math.abs


class Service : Service() {
    private lateinit var bitmap: Bitmap
    private val chatId = 1030297
    private lateinit var builder: Notification.Builder
    private lateinit var canvas: Canvas
    private var current = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var currentTextPaintSm: TextPaint
    private lateinit var unitTextTPaint: TextPaint

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            flashData()

            handler.postDelayed(this, Integer.valueOf(config.getString("rate", "35")!!)
                .toLong())
        }
    }


    private fun notification(): Notification {
        if (!this::builder.isInitialized) {
            val notificationChannel = NotificationChannel("current", getString(R.string.current_summary), NotificationManager.IMPORTANCE_HIGH).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, "current")
                .apply {
                    setContentTitle(getString(R.string.now_current))
                    setOngoing(true)
                }
        }
        draw()
        builder.apply {
            setContentText("${current / 1000}mA")
            setSmallIcon(Icon.createWithBitmap(bitmap))
        }
        return builder.build()
    }

    fun flashData() {
        current = abs((getSystemService(BATTERY_SERVICE) as BatteryManager).getIntProperty(BatteryManager.BATTERY_PLUGGED_USB))
        notificationManager.notify(chatId, notification())
    }

    private fun draw() {
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        if (current > 1000000) {
            canvas.drawText("%.2f".format(current / 1000000.0), 31.0f, 35.0f, currentTextPaintSm)
            canvas.drawText("A", 31.0f, 63.0f, unitTextTPaint)
        } else {
            canvas.drawText("${current / 1000}", 31.0f, 35.0f, currentTextPaintSm)
            canvas.drawText("mA", 31.0f, 63.0f, unitTextTPaint)
        }
    }

    private fun creatPaint(): TextPaint {
        return TextPaint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
            isFakeBoldText = true
            typeface = if (config.getBoolean("font_face", false)) Typeface.DEFAULT_BOLD else Typeface.MONOSPACE
        }
    }

    override fun onCreate() {
        super.onCreate()
        ActivityOwnSP.context = baseContext

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)

        currentTextPaintSm = creatPaint()
        currentTextPaintSm.textSize = Integer.valueOf(config.getString("current_text_size", "35")!!)
            .toFloat()

        unitTextTPaint = creatPaint()
        unitTextTPaint.textSize = Integer.valueOf(config.getString("current_unit_size", "30")!!)
            .toFloat()
    }


    override fun onStartCommand(intent: Intent, i: Int, i2: Int): Int {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 0)
        startForeground(chatId, notification())
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}