package edu.handong.cseemileage.util

import org.springframework.test.web.servlet.MvcResult

class ExtractExceptionMessage {
    companion object {
        fun getExceptionMessage(mvcResults: MvcResult): String {
            val responseBytes = mvcResults.response.contentAsByteArray
            val responseString = String(responseBytes, Charsets.UTF_8)

            val messageStart = responseString.indexOf("\"message\":") + "\"message\":".length
            val messageEnd = responseString.indexOf(",", messageStart)
            return responseString.substring(messageStart + 1, messageEnd - 1).trim()
        }
    }
}
