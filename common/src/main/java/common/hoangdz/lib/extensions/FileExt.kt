package common.hoangdz.lib.extensions

import java.io.File

/**
 * Created by HoangDepTrai on 17, August, 2022 at 2:53 PM
 */

fun File.findByExtension(extension: String) = listFiles()?.find { it.extension == extension }

fun File.findByName(name: String) = listFiles()?.find { it.nameWithoutExtension == name }

fun File.createIfNotExist() {
    absolutePath.removeSuffix(name).takeIf { it.isNotEmpty() }?.let {
        File(it).takeIf { !it.exists() }?.mkdirs()
    }
    takeIf { !it.exists() }?.createNewFile()
}