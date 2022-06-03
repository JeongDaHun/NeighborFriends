package com.example.neighborfriends.util

import android.content.Context
import kotlin.jvm.Synchronized
import android.os.Environment
import java.io.File
import java.io.IOException

/**
 * Files : 파일 관련
 *
 * @author Jeong.Da.Hun
 * @version 1.0.0
 * @since 2022-04-08
 */

object Files {
    /**
     * 디렉토리 생성
     *
     * @param path 폴더위치
     * @return boolean 폴더생성 성공실패
     */
    @Synchronized
    fun makeDir(path: String?) {
        val file = File(path)
        if (!file.exists()) {
            if (!file.mkdir()) {
                BLog.e("failed to create directory")
            }
        }
    }

    /**
     * 파일 생성
     *
     * @param filePath 파일위치
     * @param fileName 파일이름
     * @return boolean 파일생성 성공실패
     */
    @Synchronized
    fun createFile(filePath: String, fileName: String): Boolean {
        val file = File(filePath + File.separator + fileName)
        try {
            if (!file.exists()) return file.createNewFile()
        } catch (e: IOException) {
//            Logs.printException(e);
            e.printStackTrace()
        }
        return false
    }

    /**
     * 폴더 삭제
     *
     * @param dir
     * @return boolean 삭제성공실패
     */
    @JvmStatic
    @Synchronized
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val items = dir.list()
            for (f in items) {
                val file = File(dir, f)
                file.delete()
            }
        }
        return dir!!.delete()
    }

    /**
     * 파일 삭제
     * @param path 파일위치
     * @return
     */
    @Synchronized
    fun deleteFile(path: String?): Boolean {
        val file = File(path)
        return if (file.exists()) {
            file.delete()
        } else false
    }

    /**
     * 앱에서 사용할 앱 폴더 위치. 폴더가 없으면 생성한다.
     *
     * @param context
     * @param dirName 앱에서 사용할 폴더 이름
     * @return File 미디어위치
     */
    fun getOutputMediaPath(context: Context, dirName: String?): File? {
        val mediaStorageDir: File?
        mediaStorageDir = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(Environment.getExternalStorageDirectory(), dirName)
        } else {
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        }

        // 없는 경로라면 따로 생성한다.
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        return mediaStorageDir
    }

    /**
     * 특정 확장자를 가진 파일들을 삭제한다.
     *
     * @param path
     * @param ext
     */
    fun deleteExtFile(path: String?, ext: String?) {
        val dir = File(path)
        if (dir != null) {
            val files = dir.listFiles() ?: return
            for (f in files) {
                val filename = f.name
                if (filename.contains(ext!!)) f.delete()
            }
        }
    }
}