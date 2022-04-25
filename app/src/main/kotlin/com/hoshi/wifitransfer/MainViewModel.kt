package com.hoshi.wifitransfer

import androidx.lifecycle.MutableLiveData
import com.hoshi.lib.base.BaseViewModel
import com.hoshi.lib.utils.FileUtils
import java.io.File

/**
 * Created by lv.qx on 2022/4/25
 */
class MainViewModel : BaseViewModel() {

    val fileBeanList by lazy { MutableLiveData<List<FileBean>>() }

    fun loadFile() {
        val dir = Const.DIR
        val tempFileBeanList = mutableListOf<FileBean>()
        if (dir.exists() && dir.isDirectory) {
            val files = dir.listFiles()
            files?.forEach { file ->
                tempFileBeanList.add(handleFile(file))
            }
        }
        fileBeanList.postValue(tempFileBeanList)
    }

    private fun handleFile(file: File): FileBean {
        val path = file.absolutePath
        val size = FileUtils.getSizeFormat(file.length())
        val name = file.name
        return FileBean(path, name = name, size = size)
    }

    fun deleteAllFile() {
        val dir = Const.DIR
        FileUtils.deleteFolder(dir.absolutePath)
        loadFile()
    }

}