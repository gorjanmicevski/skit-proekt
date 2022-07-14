package com.sorsix.album_collector.service

import org.springframework.stereotype.Service
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

@Service
class ImageEncoderService {
    fun encodeToString(imageBytes: ByteArray): String? {
        var imageString: String? = null
//        val bos = ByteArrayOutputStream()
        try {
//            ImageIO.write(image, type, bos)
//            val imageBytes: ByteArray = bos.toByteArray()


            val encoder = Base64Coder.encode(imageBytes)
            println(encoder)
            imageString = Base64Coder.encode(imageBytes).toString()
            println(imageString)
//            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imageString
    }
}