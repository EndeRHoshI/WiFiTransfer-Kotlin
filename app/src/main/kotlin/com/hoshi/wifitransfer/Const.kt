package com.hoshi.wifitransfer

import android.os.Environment
import java.io.File

/**
 * 常量
 * Created by lv.qx on 2022/4/24
 */
object Const {
    const val HTTP_PORT = 12345 // 端口
    private const val DIR_IN_SDCARD = "WifiTransfer"
    val DIR by lazy { File(Environment.getExternalStorageDirectory().toString() + File.separator + Const.DIR_IN_SDCARD) }
}