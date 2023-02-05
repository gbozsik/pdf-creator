package com.ibt.pdfgenerator.exception

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

class ApiError(
    @JsonProperty("code") val code: String,
    @JsonProperty("timestamp") val timestamp: Instant,
    @JsonProperty("message") val message: String? = null
)