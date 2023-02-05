package com.ibt.pdfgenerator.service

import com.ibt.pdfgenerator.exception.ClientException
import com.ibt.pdfgenerator.model.InvoiceItem
import com.ibt.pdfgenerator.util.getUploadDirectory
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID

@Service
class InvoiceService {


    fun getAllInvoiceItem(): List<InvoiceItem> {
        return listOf(InvoiceItem(UUID.randomUUID().toString(),"John", 123.0),
            InvoiceItem(UUID.randomUUID().toString(),"Jane", 321.2),
            InvoiceItem(UUID.randomUUID().toString(),"Joshua", 654.3),
            InvoiceItem(UUID.randomUUID().toString(),"Kate", 4356.2),
            InvoiceItem(UUID.randomUUID().toString(),"Jolly", 37346.0))
    }

    fun getPdf(fileName: String): ByteArray {
        try {
            val file: File = ResourceUtils.getFile("${getUploadDirectory()}$fileName")
            return file.readBytes()
        } catch (e: FileNotFoundException) {
            throw ClientException("Invoice not found with the name: $fileName")
        }
    }
}