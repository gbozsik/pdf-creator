package com.ibt.pdfgenerator.controller

import com.ibt.pdfgenerator.model.Invoice
import com.ibt.pdfgenerator.model.PdfResponse
import com.ibt.pdfgenerator.service.PdfGeneratorService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/pdf")
class PdfController(private val pdfGeneratorService: PdfGeneratorService) {

    @PostMapping
    fun createPdf(@RequestBody invoices: List<Invoice>): ResponseEntity<PdfResponse> {
        return ResponseEntity(pdfGeneratorService.createPdf(invoices), HttpStatus.CREATED)
    }

    @GetMapping("/{fileName}", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPdf(@PathVariable("fileName") fileName: String): ResponseEntity<ByteArray> {
        return ResponseEntity(pdfGeneratorService.getPdf(fileName), HttpStatus.CREATED)
    }
}