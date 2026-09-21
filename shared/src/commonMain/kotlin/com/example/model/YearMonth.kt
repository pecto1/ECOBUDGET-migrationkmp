package com.example.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Modèle immuable représentant un mois spécifique pour la navigation budgétaire.
 *
 * @property year Année (ex: 2026).
 * @property month Index du mois de 0 (Janvier) à 11 (Décembre).
 */
data class YearMonth(
    val year: Int,
    val month: Int
) {
    /**
     * Libellé formaté en français (ex: "Août 2026").
     */
    val displayLabel: String
        get() {
            val monthName = MONTH_NAMES_FR.getOrElse(month) { "" }
            return "$monthName $year"
        }

    /**
     * Retourne le YearMonth précédent.
     */
    fun previous(): YearMonth {
        return if (month == 0) {
            YearMonth(year - 1, 11)
        } else {
            YearMonth(year, month - 1)
        }
    }

    /**
     * Retourne le YearMonth suivant.
     */
    fun next(): YearMonth {
        return if (month == 11) {
            YearMonth(year + 1, 0)
        } else {
            YearMonth(year, month + 1)
        }
    }

    /**
     * Vérifie si un timestamp millisecondes appartient à ce mois précis.
     */
    fun containsTimestamp(timestamp: Long): Boolean {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val ldt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return ldt.year == year && (ldt.monthNumber - 1) == month
    }

    companion object {
        private val MONTH_NAMES_FR = listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )

        /**
         * Crée le YearMonth courant.
         */
        fun current(): YearMonth {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(
                year = now.year,
                month = now.monthNumber - 1
            )
        }

        /**
         * Crée le YearMonth correspondant à un timestamp.
         */
        fun fromTimestamp(timestamp: Long): YearMonth {
            val dateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(
                year = dateTime.year,
                month = dateTime.monthNumber - 1
            )
        }
    }
}
