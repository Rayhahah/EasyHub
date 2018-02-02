package com.rayhahah.baselib.net

/**
 * Created by a on 2017/5/15.
 */

interface ProgressListener {

    fun onProgressChange(progress: Long, total: Long, done: Boolean)
}
