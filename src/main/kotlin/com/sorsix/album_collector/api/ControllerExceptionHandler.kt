package com.sorsix.album_collector.api

import com.sorsix.album_collector.domain.ErrorResponse
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import javax.persistence.EntityNotFoundException
import javax.persistence.NoResultException

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(
        HttpClientErrorException::class,
        MethodArgumentNotValidException::class,
        MissingServletRequestParameterException::class,
        IllegalArgumentException::class
    )
    fun constraintViolationException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", e)
    }

    @ExceptionHandler(
        EntityNotFoundException::class,
        NoSuchElementException::class,
        NoResultException::class,
        EmptyResultDataAccessException::class,
        IndexOutOfBoundsException::class,
        KotlinNullPointerException::class
    )
    fun notFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", e)
    }

    @ExceptionHandler(
        ConstraintViolationException::class,
    )
    fun conflictException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.CONFLICT, "Internal Conflict", e)
    }

    @ExceptionHandler(
        Exception::class
    )
    fun internalServerErrorException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Generic internal error", e)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String,
        e: Exception
    ): ResponseEntity<ErrorResponse> {
        if (e.message == null)
            return ResponseEntity(ErrorResponse(status.value(), status.name, message), status)
        return ResponseEntity(ErrorResponse(status.value(), status.name, e.message!!), status)
    }

}