package com.ibt.pdfgenerator.service

import com.ibt.pdfgenerator.model.Invoice
import com.ibt.pdfgenerator.model.PdfResponse
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.stream.Stream


@Service
class PdfGeneratorService {

    fun createPdf(invoices: List<Invoice>): PdfResponse {

        val document = Document()
        val fileName = "a.pdf"
        val filePath = String.format("%s/%s", getUploadDirectory(), fileName)
        val outFile = FileOutputStream(filePath)
        PdfWriter.getInstance(document, outFile)

        document.open()
        val font = FontFactory.getFont(FontFactory.COURIER, 16f, BaseColor.BLACK)
        val chunk = Chunk("Hello World", font)
        document.add(chunk)

        val table = PdfPTable(invoices.size)
        addTableHeader(table)
        addRows(invoices, table)
        document.add(table)

        document.close()

        return PdfResponse(UUID.randomUUID(), filePath)
    }

    private fun addTableHeader(table: PdfPTable) {
        Stream.of("name", "amount")
            .forEach { columnTitle ->
                val header = PdfPCell()
                header.backgroundColor = BaseColor.LIGHT_GRAY
                header.borderWidth = 2f
                header.phrase = Phrase(columnTitle)
                table.addCell(header)
            }
    }

    private fun addRows(invoices: List<Invoice>, table: PdfPTable) {
        invoices.forEach { invoice ->
            table.addCell("${invoice.name}, ${invoice.amount}")
        }
    }

    private fun getUploadDirectory(): String {
        val projectFolder = System.getProperty("user.dir")
        val uploadsDir = String.format("%s/src/main/resources/public", projectFolder)
        if (!File(uploadsDir).exists()) {
            File(uploadsDir).mkdir()
        }
        return uploadsDir
    }

    fun getPdf(fileName: String): ByteArray {
        val file: File = ResourceUtils.getFile("classpath:a.pdf")
        return file.readBytes()
    }
}