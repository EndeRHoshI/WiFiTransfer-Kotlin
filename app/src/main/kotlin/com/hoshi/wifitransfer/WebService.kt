package com.hoshi.wifitransfer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.HttpServerRequestCallback
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat

/**
 * 本地服务器 Service
 * Created by lv.qx on 2022/4/24
 */
class WebService : Service() {

    private val httpServer by lazy { AsyncHttpServer() }
    private val asyncServer by lazy { AsyncServer() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.action
            if (ACTION_START_WEB_SERVICE == action) {
                startServer()
            } else if (ACTION_STOP_WEB_SERVICE == action) {
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startServer() {
        httpServer.get("/images/.*", httpServerRequestCallback)
        httpServer.get("/scripts/.*", httpServerRequestCallback)
        httpServer.get("/css/.*", httpServerRequestCallback)

        // index page，读取 index.html 并展示
        httpServer.get("/") { _, response ->
            try {
                response.send(getIndexContent())
            } catch (e: IOException) {
                e.printStackTrace()
                response.code(500).end()
            }
        }

        // query upload list
        httpServer.get("/files") { _, response ->
            val array = JSONArray()
            val dir = Const.DIR
            if (dir.exists() && dir.isDirectory) {
                dir.list()?.forEach { fileName ->
                    val file = File(dir, fileName)
                    if (file.exists() && file.isFile) {
                        try {
                            val jsonObject = JSONObject()
                            jsonObject.put("name", fileName)
                            val fileLen = file.length()
                            val df = DecimalFormat("0.00")
                            when {
                                fileLen > 1024 * 1024 -> jsonObject.put("size", df.format(fileLen * 1f / 1024 / 1024) + "MB")
                                fileLen > 1024 -> jsonObject.put("size", df.format(fileLen * 1f / 1024) + "KB")
                                else -> jsonObject.put("size", "" + fileLen + "B")
                            }
                            array.put(jsonObject)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            response.send(array.toString())
        }
        httpServer.listen(asyncServer, Const.HTTP_PORT)
    }

    private val httpServerRequestCallback = HttpServerRequestCallback { request, response ->
        try {
            var fullPath = request.path
            fullPath = fullPath.replace("%20", " ")
            var resourceName = fullPath
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1)
            }
            if (resourceName.indexOf("?") > 0) {
                resourceName = resourceName.substring(0, resourceName.indexOf("?"))
            }
            if (getContentTypeByResourceName(resourceName).isNotEmpty()) {
                response.setContentType(getContentTypeByResourceName(resourceName))
            }
            val bInputStream = BufferedInputStream(assets.open("wifi/$resourceName"))
            response.sendStream(bInputStream, bInputStream.available().toLong())
        } catch (e: IOException) {
        }
    }

    private fun getContentTypeByResourceName(resourceName: String) = when {
        resourceName.endsWith(".css") -> "text/css;charset=utf-8"
        resourceName.endsWith(".js") -> "application/javascript"
        resourceName.endsWith(".swf") -> "application/x-shockwave-flash"
        resourceName.endsWith(".png") -> "application/x-png"
        resourceName.endsWith(".jpg") || resourceName.endsWith(".jpeg") -> "application/jpeg"
        resourceName.endsWith(".woff") -> "application/x-font-woff"
        resourceName.endsWith(".ttf") -> "application/x-font-truetype"
        resourceName.endsWith(".svg") -> "image/svg+xml"
        resourceName.endsWith(".eot") -> "image/vnd.ms-fontobject"
        resourceName.endsWith(".mp3") -> "audio/mp3"
        resourceName.endsWith(".mp4") -> "video/mpeg4"
        else -> ""
    }

    /**
     * @return String index.html 的内容
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun getIndexContent(): String {
        BufferedInputStream(assets.open("wifi/index.html")).use { bInputStream ->
            ByteArrayOutputStream().use { bOutputStream ->
                var len: Int
                val tmp = ByteArray(10240)
                while (bInputStream.read(tmp).also { len = it } > 0) {
                    bOutputStream.write(tmp, 0, len)
                }
                return String(bOutputStream.toByteArray(), StandardCharsets.UTF_8)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        httpServer.stop()
        asyncServer.stop()
    }

    companion object {

        private const val ACTION_START_WEB_SERVICE = "start_web_service"
        private const val ACTION_STOP_WEB_SERVICE = "stop_web_service"

        fun start(context: Context) {
            val intent = Intent(context, WebService::class.java)
            intent.action = ACTION_START_WEB_SERVICE
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, WebService::class.java)
            intent.action = ACTION_STOP_WEB_SERVICE
            context.startService(intent)
        }

    }

}