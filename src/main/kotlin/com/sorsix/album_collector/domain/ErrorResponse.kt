package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.util.Date

data class ErrorResponse(
    val code: Int,
    val status: String,
    val message: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    val timestamp: Date = Date()
) {

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//    val timestamp: Date = Date()


//    code = status.value ()
//    statusString = status.name
}