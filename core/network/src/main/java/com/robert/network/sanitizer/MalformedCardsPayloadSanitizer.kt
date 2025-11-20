package com.robert.network.sanitizer

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MalformedCardsPayloadSanitizer @Inject constructor() {

    fun normalize(rawPayload: String): String {
        val trimmed = rawPayload.trim()
        if (trimmed.isEmpty()) return rawPayload

        if (looksLikeProperCardsObject(trimmed)) {
            return rawPayload
        }

        if (trimmed.startsWith("\"cards\"") || trimmed.startsWith("'cards'")) {
            return wrapWithCardsObject(trimmed)
        }

        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            val innerSection = trimmed.substring(1, trimmed.length - 1).trim()
            if (innerSection.isEmpty()) return rawPayload

            if (innerSection.startsWith("\"cards\"")) {
                return wrapWithCardsObject(innerSection)
            }

            if (innerSection.startsWith("{")) {
                val payloadHasMultipleObjects = innerSection.count { it == '{' } > 1
                if (payloadHasMultipleObjects) {
                    val arrayWrapped = if (innerSection.startsWith("[")) {
                        innerSection
                    } else {
                        "[\n$innerSection\n]"
                    }
                    return "{\"cards\":$arrayWrapped}"
                }
            }
        }

        return rawPayload
    }

    private fun looksLikeProperCardsObject(payload: String): Boolean =
        payload.startsWith("{") &&
            payload.contains("\"cards\"") &&
            payload.contains("[") &&
            payload.contains("]")

    private fun wrapWithCardsObject(content: String): String {
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
}
