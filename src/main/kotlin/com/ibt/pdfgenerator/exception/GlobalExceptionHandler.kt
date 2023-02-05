package com.ibt.pdfgenerator.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ClientException::class)
    fun handleClientException(clientException: ClientException): ResponseEntity<ApiError> =
        response(HttpStatus.BAD_REQUEST, Errors.VALIDATION_ERROR, clientException.message)

    @ExceptionHandler(TechnicalException::class)
    fun handleTechnicalException(technicalException: TechnicalException): ResponseEntity<ApiError> =
        response(HttpStatus.INTERNAL_SERVER_ERROR, Errors.TECHNICAL_ERROR, technicalException.message)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiError> {
        logger.error("Unexpected error", ex)
        return response(HttpStatus.INTERNAL_SERVER_ERROR, Errors.GENERAL_ERROR, ex.message)
    }

    fun response(httpStatus: HttpStatus, error: Errors, msg: String?): ResponseEntity<ApiError> {
        val response = ApiError(
            code = error.toString(),
            timestamp = Instant.now(),
            message = msg
        )
        logger.error("Error response: $response")
        return ResponseEntity(response, httpStatus)
    }
}
