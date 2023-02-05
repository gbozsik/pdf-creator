package com.ibt.pdfgenerator.util

import java.io.File

internal fun getUploadDirectory(): String {
    val projectFolder = System.getProperty("user.dir")
    val uploadsDir = "$projectFolder/src/main/resources/public/"
    if (!File(uploadsDir).exists()) {
        File(uploadsDir).mkdir()
    }
    return uploadsDir
}