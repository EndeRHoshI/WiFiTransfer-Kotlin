package com.hoshi.wifitransfer

import com.hoshi.core.utils.FileUtils
import com.hoshi.core.utils.HLog
import java.io.*

/**
 * 上传 Holder，用于处理上传时的各种逻辑
 * Created by lv.qx on 2022/4/25
 */
class FileUploadHolder {

    companion object {
        private const val TAG = "FileUploadHolder"
    }

    private var fileName: String? = null
    private var receivedFile: File? = null
    private var fileOutPutStream: BufferedOutputStream? = null
    private var totalSize: Long = 0

    fun getFileName() = fileName
    fun getTotalSize() = totalSize
    fun getFileOutPutStream() = fileOutPutStream

    fun setFileName(fileName: String) {
        this.fileName = fileName
        totalSize = 0

        FileUtils.createIfNoExists(Const.DIR)
        this.receivedFile = File(Const.DIR, fileName)
        HLog.d(TAG, receivedFile?.absolutePath.orEmpty())
        try {
            fileOutPutStream = BufferedOutputStream(FileOutputStream(receivedFile))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun reset() {
        try {
            fileOutPutStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        fileOutPutStream = null
    }

    fun write(data: ByteArray) {
        if (fileOutPutStream != null) {
            try {
                fileOutPutStream?.write(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        totalSize += data.size
    }

}