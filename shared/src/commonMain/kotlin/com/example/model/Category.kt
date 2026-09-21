package com.example.model

import com.example.shared.resources.*
import org.jetbrains.compose.resources.StringResource

enum class Category(
    val labelRes: StringResource,
    val emoji: String
) {
    TRANSPORT(Res.string.category_transport, "🚌"),
    ALIMENTATION(Res.string.category_alimentation, "🍱"),
    LOISIRS(Res.string.category_loisirs, "🎾"),
    LOGEMENT(Res.string.category_logement, "🏠")
}