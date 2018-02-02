package com.rayhahah.baselib.net

import java.io.IOException

import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio


class ProgressResponseBody(private val response: Response, private val progressListener: ProgressListener) : ResponseBody() {
    private val bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return response.body()!!.contentType()
    }


    override fun contentLength(): Long {
        return response.body()!!.contentLength()
    }

    override fun source(): BufferedSource {
        return Okio.buffer(object : ForwardingSource(response.body()!!.source()) {
            internal var bytesReaded: Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                bytesReaded += if (bytesRead.toInt() == -1) 0 else bytesRead
                progressListener.onProgressChange(bytesReaded, contentLength(), false)
                return bytesRead
            }
        })
    }

}
