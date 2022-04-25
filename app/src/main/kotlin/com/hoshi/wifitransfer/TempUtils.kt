package com.hoshi.wifitransfer

import java.text.DecimalFormat

/**
 * 临时工具类，后续根据类型并入对应的工具类中
 * Created by lv.qx on 2022/4/25
 */
object TempUtils {

    fun getFileSizeFormat(length: Long): String {
        val df = DecimalFormat("######0.00")
        val l = length / 1000 // KB
        if (l < 1024) {
            return df.format(l) + "KB"
        } else if (l < 1024 * 1024F) {
            return df.format((l / 1024F)) + "MB"
        }
        return df.format(l / 1024F / 1024F) + "GB"
    }

}