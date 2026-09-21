package com.example.data.repository

import com.example.model.Category
import com.example.model.Transaction
import com.example.utils.generateUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Implémentation factice (Mock/In-Memory) de [TransactionRepository] pour simuler l'accès
 * aux données sans base de données réelle.
 *
 * Entièrement neutralisée des dépendances Java (java.util.Calendar, java.util.UUID).
 */
class FakeTransactionRepository : TransactionRepository {

    private val _transactionsFlow: MutableStateFlow<List<Transaction>>

    init {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = now.year
        val currentMonth = now.monthNumber - 1 // 0-based

        fun getTimeForMonth(monthOffset: Int, day: Int, hour: Int): Long {
            var targetYear = currentYear
            var targetMonth = currentMonth + monthOffset
            if (targetMonth < 0) {
                targetYear -= 1
                targetMonth += 12
            } else if (targetMonth > 11) {
                targetYear += 1
                targetMonth -= 12
            }
            val ldt = LocalDateTime(
                year = targetYear,
                monthNumber = targetMonth + 1,
                dayOfMonth = day,
                hour = hour,
                minute = 0,
                second = 0,
                nanosecond = 0
            )
            return ldt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        }

        val initialList = listOf(
            // Mois actuel (0)
            Transaction(
                id = generateUUID(),
                title = "Supermarché Bio",
                amount = 45000.0,
                date = getTimeForMonth(0, 22, 14),
                category = Category.ALIMENTATION
            ),
            Transaction(
                id = generateUUID(),
                title = "Session Tennis",
                amount = 12000.0,
                date = getTimeForMonth(0, 20, 10),
                category = Category.LOISIRS
            ),
            Transaction(
                id = generateUUID(),
                title = "Ticket de Bus Express",
                amount = 2500.0,
                date = getTimeForMonth(0, 18, 8),
                category = Category.TRANSPORT
            ),
            Transaction(
                id = generateUUID(),
                title = "Loyer Mensuel",
                amount = 250000.0,
                date = getTimeForMonth(0, 5, 9),
                category = Category.LOGEMENT
            ),
            Transaction(
                id = generateUUID(),
                title = "Boulangerie & Pâtisserie",
                amount = 4800.0,
                date = getTimeForMonth(0, 15, 16),
                category = Category.ALIMENTATION
            ),
            Transaction(
                id = generateUUID(),
                title = "Recharge Vélo Électrique",
                amount = 3500.0,
                date = getTimeForMonth(0, 12, 11),
                category = Category.TRANSPORT
            ),
            Transaction(
                id = generateUUID(),
                title = "Facture Électricité",
                amount = 48000.0,
                date = getTimeForMonth(0, 8, 15),
                category = Category.LOGEMENT
            ),

            // Mois précédent (-1)
            Transaction(
                id = generateUUID(),
                title = "Loyer Mois Précédent",
                amount = 250000.0,
                date = getTimeForMonth(-1, 5, 9),
                category = Category.LOGEMENT
            ),
            Transaction(
                id = generateUUID(),
                title = "Courses du mois",
                amount = 65000.0,
                date = getTimeForMonth(-1, 10, 15),
                category = Category.ALIMENTATION
            ),
            Transaction(
                id = generateUUID(),
                title = "Abonnement Transport",
                amount = 35000.0,
                date = getTimeForMonth(-1, 2, 8),
                category = Category.TRANSPORT
            ),
            Transaction(
                id = generateUUID(),
                title = "Sortie Restaurant",
                amount = 22000.0,
                date = getTimeForMonth(-1, 20, 20),
                category = Category.LOISIRS
            ),

            // Mois suivant (+1)
            Transaction(
                id = generateUUID(),
                title = "Avance Loyer Prévue",
                amount = 250000.0,
                date = getTimeForMonth(1, 1, 9),
                category = Category.LOGEMENT
            ),
            Transaction(
                id = generateUUID(),
                title = "Abonnement Salle de Sport",
                amount = 20000.0,
                date = getTimeForMonth(1, 3, 10),
                category = Category.LOISIRS
            )
        )

        _transactionsFlow = MutableStateFlow(initialList)
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return _transactionsFlow.asStateFlow()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        _transactionsFlow.update { currentList ->
            listOf(transaction) + currentList
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        _transactionsFlow.update { currentList ->
            currentList.map { if (it.id == transaction.id) transaction else it }
        }
    }

    override suspend fun deleteTransaction(id: String) {
        _transactionsFlow.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }
}
