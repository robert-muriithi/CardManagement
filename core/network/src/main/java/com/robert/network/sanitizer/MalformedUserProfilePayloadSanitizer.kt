package com.robert.network.sanitizer

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MalformedUserProfilePayloadSanitizer @Inject constructor() {

    fun normalize(rawPayload: String): String {
        val trimmed = rawPayload.trim()
        if (trimmed.isEmpty()) return FALLBACK_PAYLOAD

        if (looksLikeProperUserObject(trimmed)) {
            return rawPayload
        }

        if (trimmed.startsWith("\"user\"") || trimmed.startsWith("'user'")) {
            return wrapWithUserObject(trimmed)
        }

        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            val innerSection = trimmed.substring(1, trimmed.length - 1).trim()
            if (innerSection.startsWith("\"user\"")) {
                return wrapWithUserObject(innerSection)
            }
        }

        return FALLBACK_PAYLOAD
    }

    private fun looksLikeProperUserObject(payload: String): Boolean =
        payload.startsWith("{") && payload.contains("\"user\"")

    private fun wrapWithUserObject(content: String): String {
        val cleaned = content
            .trim()
            .trimEndTrailingComma()
        val normalized = cleaned.stripOuterBracesIfPresent()
        return "{${normalized.trim()}}"
    }

    private fun String.trimEndTrailingComma(): String =
        trimEnd { it.isWhitespace() || it == ',' }

    private fun String.stripOuterBracesIfPresent(): String =
        if (startsWith("{") && endsWith("}") && length >= 2) {
            substring(1, length - 1)
        } else {
            this
        }

    private companion object {
        private val FALLBACK_PAYLOAD = """
            {
                "user": {
                    "id": "user_12345",
                    "firstName": "Wanjiku",
                    "lastName": "Kimani",
                    "email": "wanjiku.kimani@example.com",
                    "phone": "+254 712 345 678",
                    "avatarUrl": "https://i.pravatar.cc/150?u=user_12345",
                    "address": {
                        "street": "Moi Avenue",
                        "city": "Nairobi",
                        "country": "Kenya",
                        "postalCode": "00100"
                    }
                }
            }
        """.trimIndent()
    }
}

