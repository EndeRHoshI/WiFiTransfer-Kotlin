package com.hoshi.wifitransfer

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import com.hoshi.lib.AppState
import com.hoshi.lib.utils.HLog
import com.hoshi.lib.utils.PanelUtils

/**
 * TODO lv.qx 后续并入 Armor 中
 * Created by lv.qx on 2022/4/19
 */
object WiFiUtils {

    private const val TAG = "WiFiUtils"

    /**
     * 打开 WiFi
     * @return Boolean
     */
    fun openWifi(): Boolean {
        val wifiManager = AppState.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        return if (!wifiManager.isWifiEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PanelUtils.showWiFi() // 使用 Panel 打开 WiFi
                // fragmentActivity.startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)) // 跳转到 WiFi 设置页面
                HLog.d(TAG, "WiFi 不可用")
                false
            } else {
                wifiManager.isWifiEnabled = true
                HLog.d(TAG, "WiFi 不可用，但是已经打开")
                true
            }
        } else {
            HLog.d(TAG, "WiFi 可用")
            true
        }
    }

    /**
     * @param context 上下文
     * @return String? 当前 WiFi 的 ip
     */
    fun getWiFiIp(context: Context): String? {
        val wifiManager = AppState.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo?: return null
        return intToIp(wifiInfo.ipAddress)
    }

    private fun intToIp(i: Int) = (i and 0xFF).toString() + "." + (i shr 8 and 0xFF) + "." + (i shr 16 and 0xFF) + "." + (i shr 24 and 0xFF)

}