package com.hoshi.wifitransfer

import android.graphics.drawable.Drawable

/**
 * Created by lv.qx on 2022/4/25
 */
data class FileBean(
    val path: String = "", // 路径
    val version: String = "", // 版本
    val name: String = "", // 名称
    val packageName: String = "", // packageName
    val size: String = "", // 大小
    val icon: Drawable? = null // logo
)