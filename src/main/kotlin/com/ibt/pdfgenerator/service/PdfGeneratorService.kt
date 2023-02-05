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
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream


@Service
class PdfGeneratorService {

    fun createPdf(invoices: List<Invoice>): PdfResponse {

        val document = Document()
        val fileName = "Invoice_${LocalDateTime.now()}"
        val filePath = String.format("%s/%s", getUploadDirectory(), fileName)
        val outFile = FileOutputStream(filePath)
        PdfWriter.getInstance(document, outFile)

        document.open()
        val font = FontFactory.getFont(FontFactory.COURIER, 16f, BaseColor.BLACK)
        val chunk = Chunk("Invoice", font)

        document.add(Paragraph(chunk))
        document.add(Paragraph(Chunk.NEWLINE))


        val table = PdfPTable(2)
        addTableHeader(table)
        addRows(invoices, table)
        addTotalAmountRow(invoices, table)
        document.add(table)

        document.close()

        return PdfResponse(UUID.randomUUID(), fileName)
    }

    private fun addTotalAmountRow(invoices: List<Invoice>, table: PdfPTable) {
        val totalAmount: Double = invoices.stream()
            .map { it.amount }
            .reduce { amount, amount2 -> amount + amount2 }
            .orElse(0.0)

        table.addCell(createTotalMountCell("Total amount"))
        table.addCell(createTotalMountCell("$totalAmount"))
    }

    private fun createTotalMountCell(value: String): PdfPCell {
        val cell = PdfPCell()
        cell.borderWidth = 2f
        cell.phrase = Phrase(value)
        return cell
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
            table.addCell(invoice.name)
            table.addCell("${invoice.amount}")
        }
    }

    private fun getUploadDirectory(): String {
        val projectFolder = System.getProperty("user.dir")
        val uploadsDir = "$projectFolder/src/main/resources/public/"
        if (!File(uploadsDir).exists()) {
            File(uploadsDir).mkdir()
        }
        return uploadsDir
    }

    fun getPdf(fileName: String): ByteArray {
        val file: File = ResourceUtils.getFile("${getUploadDirectory()}$fileName")
        return file.readBytes()
    }
}