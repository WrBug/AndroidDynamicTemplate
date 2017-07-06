package com.wrbug.dynamictemplatedemo.util

import android.content.Context
import java.io.ByteArrayInputStream

/**
 * ResourceHelper
 *
 * @author wrbug
 * @since 2017/7/6
 */
object ResourceHelper {
    fun readAssestFile(context: Context, path: String): ByteArray {
        var stream = context.assets.open(path)
        var data: ByteArray = ByteArray(stream.available())
        stream.read(data)
        return data
    }
}