package com.sorsix.album_collector.service

import com.sorsix.album_collector.domain.CsvSticker
import com.sun.jdi.request.InvalidRequestStateException
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Paths

@Service
class CsvService {
    fun readCsvFile(file: MultipartFile): List<CsvSticker> {
        val bufferedReader = BufferedReader(InputStreamReader(file.inputStream))
        val csvParser = CSVParser(
            bufferedReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
        )
        val records = csvParser.map { CsvSticker(it.get("\uFEFFnumber"), it.get("name"), it.get("page")) }
        println(records)
        return records
    }
}