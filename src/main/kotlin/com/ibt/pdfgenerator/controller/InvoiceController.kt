package com.ibt.pdfgenerator.controller

import com.ibt.pdfgenerator.model.InvoiceItem
import com.ibt.pdfgenerator.model.PdfResponse
import com.ibt.pdfgenerator.service.InvoiceService
import com.ibt.pdfgenerator.service.PdfGeneratorService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/pdf")
class InvoiceController(private val pdfGeneratorService: PdfGeneratorService,
                        private val invoiceService: InvoiceService) {

    @PostMapping
    fun createPdf(@RequestBody invoiceItems: List<InvoiceItem>): ResponseEntity<PdfResponse> {
        return ResponseEntity(pdfGeneratorService.createPdf(invoiceItems), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllInvliceItems(): ResponseEntity<List<InvoiceItem>> {
        return ResponseEntity(invoiceService.getAllInvoiceItem(), HttpStatus.OK)
    }

    @GetMapping("/{fileName}", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPdf(@PathVariable("fileName") fileName: String): ResponseEntity<ByteArray> {
        return ResponseEntity(invoiceService.getPdf(fileName), HttpStatus.CREATED)
    }
}