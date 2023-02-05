package com.ibt.pdfgenerator.service

import com.ibt.pdfgenerator.exception.ClientException
import com.ibt.pdfgenerator.exception.TechnicalException
import com.ibt.pdfgenerator.model.InvoiceItem
import com.ibt.pdfgenerator.model.PdfResponse
import com.ibt.pdfgenerator.util.getUploadDirectory
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream


@Service
class PdfGeneratorService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createPdf(invoiceItems: List<InvoiceItem>): PdfResponse {
        validateRequest(invoiceItems)
        return getGeneratedInvoiceName(invoiceItems)
    }

    private fun validateRequest(invoiceItems: List<InvoiceItem>) {
        if (invoiceItems.isEmpty()) {
            throw ClientException("Invoice item list can not be empty")
        }
        }

    private fun getGeneratedInvoiceName(invoiceItems: List<InvoiceItem>): PdfResponse {
        try {
            val fileName = "Invoice_${LocalDateTime.now()}"
            val document = getocument(fileName)
            createHeader(document)
            createInvoiceTable(invoiceItems, document)
            return PdfResponse(UUID.randomUUID(), fileName)
        } catch (e: Exception) {
            val errorMessage = "Could not generate PDF"
            logger.error(errorMessage, e)
            throw TechnicalException(errorMessage)
        }
    }

    private fun createHeader(document: Document) {
        val font = FontFactory.getFont(FontFactory.COURIER, 16f, BaseColor.BLACK)
        val chunk = Chunk("Invoice", font)
        document.add(Paragraph(chunk))
        document.add(Paragraph(Chunk.NEWLINE))
    }

    private fun createInvoiceTable(
        invoiceItems: List<InvoiceItem>,
        document: Document
    ) {
        val table = PdfPTable(2)
        addTableHeader(table)
        addRows(invoiceItems, table)
        addTotalAmountRow(invoiceItems, table)
        document.add(table)

        document.close()
    }

    private fun getocument(fileName: String): Document {
        val document = Document()
        val filePath = String.format("%s/%s", getUploadDirectory(), fileName)
        val outFile = FileOutputStream(filePath)
        PdfWriter.getInstance(document, outFile)
        document.open()
        return document
    }

    private fun addTotalAmountRow(invoiceItems: List<InvoiceItem>, table: PdfPTable) {
        val totalAmount: Double = getTotalAmount(invoiceItems)
        table.addCell(createTotalMountCell("Total amount"))
        table.addCell(createTotalMountCell("$totalAmount"))
    }

    private fun getTotalAmount(invoiceItems: List<InvoiceItem>): Double {
        val totalAmount: Double = invoiceItems.stream()
            .map { it.amount }
            .reduce { amount, amount2 -> amount + amount2 }
            .orElse(0.0)
        return totalAmount
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

    private fun addRows(invoiceItems: List<InvoiceItem>, table: PdfPTable) {
        invoiceItems.forEach { invoice ->
            table.addCell(invoice.name)
            table.addCell("${invoice.amount}")
        }
    }
}