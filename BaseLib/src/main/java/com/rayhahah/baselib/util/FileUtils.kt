package com.rayhahah.baselib.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.io.IOException

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/8/11
 * desc  : 文件相关工具类
</pre> *
 */
class FileUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        /**
         * 根据文件路径获取文件
         *
         * @param filePath 文件路径
         * @return 文件
         */
        fun getFileByPath(filePath: String): File? {
            return if (filePath.isNullOrBlank()) null else File(filePath)
        }

        /**
         * 判断文件是否存在
         *
         * @param filePath 文件路径
         * @return `true`: 存在<br></br>`false`: 不存在
         */
        fun isFileExists(filePath: String): Boolean {
            return isFileExists(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在
         *
         * @param file 文件
         * @return `true`: 存在<br></br>`false`: 不存在
         */
        fun isFileExists(file: File?): Boolean {
            return file != null && file.exists()
        }

        /**
         * 重命名文件
         *
         * @param filePath 文件路径
         * @param newName  新名称
         * @return `true`: 重命名成功<br></br>`false`: 重命名失败
         */
        fun rename(filePath: String, newName: String): Boolean {
            return rename(getFileByPath(filePath), newName)
        }

        /**
         * 重命名文件
         *
         * @param file    文件
         * @param newName 新名称
         * @return `true`: 重命名成功<br></br>`false`: 重命名失败
         */
        fun rename(file: File?, newName: String): Boolean {
            // 文件为空返回false
            if (file == null) {
                return false
            }
            // 文件不存在返回false
            if (!file.exists()) {
                return false
            }
            // 新的文件名为空返回false
            if (newName.isNullOrBlank()) {
                return false
            }
            // 如果文件名没有改变返回true
            if (newName == file.name) {
                return true
            }
            val newFile = File(file.parent + File.separator + newName)
            // 如果重命名的文件已存在返回false
            return !newFile.exists() && file.renameTo(newFile)
        }

        /**
         * 判断是否是目录
         *
         * @param dirPath 目录路径
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isDir(dirPath: String): Boolean {
            return isDir(getFileByPath(dirPath))
        }

        /**
         * 判断是否是目录
         *
         * @param file 文件
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isDir(file: File?): Boolean {
            return isFileExists(file) && file!!.isDirectory
        }

        /**
         * 判断是否是文件
         *
         * @param filePath 文件路径
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isFile(filePath: String): Boolean {
            return isFile(getFileByPath(filePath))
        }

        /**
         * 判断是否是文件
         *
         * @param file 文件
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isFile(file: File?): Boolean {
            return isFileExists(file) && file!!.isFile
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param dirPath 目录路径
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsDir(dirPath: String): Boolean {
            return createOrExistsDir(getFileByPath(dirPath))
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsDir(file: File?): Boolean {
            // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param filePath 文件路径
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsFile(filePath: String): Boolean {
            return createOrExistsFile(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsFile(file: File?): Boolean {
            if (file == null) {
                return false
            }
            // 如果存在，是文件则返回true，是目录则返回false
            if (file.exists()) {
                return file.isFile
            }
            if (!createOrExistsDir(file.parentFile)) {
                return false
            }
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param filePath 文件路径
         * @return `true`: 创建成功<br></br>`false`: 创建失败
         */
        fun createFileByDeleteOldFile(filePath: String): Boolean {
            return createFileByDeleteOldFile(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param file 文件
         * @return `true`: 创建成功<br></br>`false`: 创建失败
         */
        fun createFileByDeleteOldFile(file: File?): Boolean {
            if (file == null) {
                return false
            }
            // 文件存在并且删除失败返回false
            if (file.exists() && file.isFile && !file.delete()) {
                return false
            }
            // 创建目录失败返回false
            if (!createOrExistsDir(file.parentFile)) {
                return false
            }
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }


        /**
         * 删除目录
         *
         * @param dirPath 目录路径
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteDir(dirPath: String): Boolean {
            return deleteDir(getFileByPath(dirPath))
        }

        /**
         * 删除目录
         *
         * @param dir 目录
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteDir(dir: File?): Boolean {
            if (dir == null) {
                return false
            }
            // 目录不存在返回true
            if (!dir.exists()) {
                return true
            }
            // 不是目录返回false
            if (!dir.isDirectory) {
                return false
            }
            // 现在文件存在且是文件夹
            val files = dir.listFiles()
            if (files != null && files.size != 0) {
                for (file in files) {
                    if (file.isFile) {
                        if (!deleteFile(file)) {
                            return false
                        }
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) {
                            return false
                        }
                    }
                }
            }
            return dir.delete()
        }

        /**
         * 删除文件
         *
         * @param srcFilePath 文件路径
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteFile(srcFilePath: String): Boolean {
            return deleteFile(getFileByPath(srcFilePath))
        }

        /**
         * 删除文件
         *
         * @param file 文件
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteFile(file: File?): Boolean {
            return file != null && (!file.exists() || file.isFile && file.delete())
        }

        /**
         * 删除目录下的所有文件
         *
         * @param dirPath 目录路径
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteFilesInDir(dirPath: String): Boolean {
            return deleteFilesInDir(getFileByPath(dirPath))
        }

        /**
         * 删除目录下的所有文件
         *
         * @param dir 目录
         * @return `true`: 删除成功<br></br>`false`: 删除失败
         */
        fun deleteFilesInDir(dir: File?): Boolean {
            if (dir == null) {
                return false
            }
            // 目录不存在返回true
            if (!dir.exists()) {
                return true
            }
            // 不是目录返回false
            if (!dir.isDirectory) {
                return false
            }
            // 现在文件存在且是文件夹
            val files = dir.listFiles()
            if (files != null && files.size != 0) {
                for (file in files) {
                    if (file.isFile) {
                        if (!deleteFile(file)) {
                            return false
                        }
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) {
                            return false
                        }
                    }
                }
            }
            return true
        }


        /**
         * 获取目录长度
         *
         * @param dirPath 目录路径
         * @return 文件大小
         */
        fun getDirLength(dirPath: String): Long {
            return getDirLength(getFileByPath(dirPath))
        }

        /**
         * 获取目录长度
         *
         * @param dir 目录
         * @return 文件大小
         */
        fun getDirLength(dir: File?): Long {
            if (!isDir(dir)) {
                return -1
            }
            var len: Long = 0
            val files = dir!!.listFiles()
            if (files != null && files.size != 0) {
                for (file in files) {
                    if (file.isDirectory) {
                        len += getDirLength(file)
                    } else {
                        len += file.length()
                    }
                }
            }
            return len
        }

        /**
         * 获取文件长度
         *
         * @param filePath 文件路径
         * @return 文件大小
         */
        fun getFileLength(filePath: String): Long {
            return getFileLength(getFileByPath(filePath))
        }

        /**
         * 获取文件长度
         *
         * @param file 文件
         * @return 文件大小
         */
        fun getFileLength(file: File?): Long {
            return if (!isFile(file)) {
                -1
            } else file!!.length()
        }


        /**
         * 获取全路径中的文件名
         *
         * @param file 文件
         * @return 文件名
         */
        fun getFileName(file: File?): String? {
            return if (file == null) {
                null
            } else getFileName(file.path)
        }

        /**
         * 获取全路径中的文件名
         *
         * @param filePath 文件路径
         * @return 文件名
         */
        fun getFileName(filePath: String): String {
            if (filePath.isNullOrBlank()) {
                return filePath
            }
            val lastSep = filePath.lastIndexOf(File.separator)
            return if (lastSep == -1) filePath else filePath.substring(lastSep + 1)
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param file 文件
         * @return 不带拓展名的文件名
         */
        fun getFileNameNoExtension(file: File?): String? {
            return if (file == null) {
                null
            } else getFileNameNoExtension(file.path)
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param filePath 文件路径
         * @return 不带拓展名的文件名
         */
        fun getFileNameNoExtension(filePath: String): String {
            if (filePath.isNullOrBlank()) {
                return filePath
            }
            val lastPoi = filePath.lastIndexOf('.')
            val lastSep = filePath.lastIndexOf(File.separator)
            if (lastSep == -1) {
                return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
            }
            return if (lastPoi == -1 || lastSep > lastPoi) {
                filePath.substring(lastSep + 1)
            } else filePath.substring(lastSep + 1, lastPoi)
        }


        /**
         * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
         */
        @SuppressLint("NewApi")
        fun getPathFromUri(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                          selectionArgs: Array<String>?): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }


        /**
         * 判断是够有SD卡
         *
         * @return
         */
        fun hasSDCard(): Boolean {
            val status = Environment.getExternalStorageState()
            return if (status != Environment.MEDIA_MOUNTED) {
                false
            } else true
        }

        /**
         * 获取根目录
         *
         * @return
         */
        // filePath:/sdcard/
        // filePath: /data/data/
        val rootFilePath: String
            get() = if (hasSDCard()) {
                Environment.getExternalStorageDirectory().absolutePath + "/"
            } else {
                Environment.getDataDirectory().absolutePath + "/data/"
            }
    }

}