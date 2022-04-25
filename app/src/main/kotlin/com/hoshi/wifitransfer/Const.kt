package com.hoshi.wifitransfer

import android.os.Environment
import java.io.File

/**
 * 常量
 * Created by lv.qx on 2022/4/24
 */
object Const {
    const val BUS_KEY_REFRESH_FILE_LIST = "refresh_file_list" // 刷新文件列表事件 key
    const val HTTP_PORT = 12345 // 端口

    /**
     * 根目录下的文件夹
     */
    val DIR by lazy { File(Environment.getExternalStorageDirectory().toString() + File.separator + "WiFiTransfer") }
}