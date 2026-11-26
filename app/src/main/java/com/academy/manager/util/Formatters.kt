package com.academy.manager.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Formatters {

    private val currencyFormat = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US))
    private val displayDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val monthYearFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    private val fullMonthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    fun formatCurrency(amount: Long): String {
        return "Rs ${currencyFormat.format(amount)}"
    }

    fun formatNumber(amount: Long): String {
        return currencyFormat.format(amount)
    }

    fun formatDate(epochMillis: Long): String {
        return displayDateFormat.format(Date(epochMillis))
    }

    fun formatIsoDate(isoDate: String): String {
        return try {
            val parsed = isoDateFormat.parse(isoDate)
            if (parsed != null) displayDateFormat.format(parsed) else isoDate
        } catch (_: Exception) {
            isoDate
        }
    }

    fun toIsoDate(epochMillis: Long): String {
        return isoDateFormat.format(Date(epochMillis))
    }

    fun isoDateToMillis(isoDate: String): Long {
        return try {
            isoDateFormat.parse(isoDate)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }

    fun todayIso(): String {
        return isoDateFormat.format(Date())
    }

    fun currentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun currentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun monthRangeMillis(month: Int, year: Int): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, (month - 1).coerceIn(0, 11))
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val end = (start.clone() as Calendar).apply {
            add(Calendar.MONTH, 1)
            add(Calendar.MILLISECOND, -1)
        }
        return start.timeInMillis to end.timeInMillis
    }

    fun monthIsoPrefix(month: Int, year: Int): String {
        return String.format(Locale.US, "%04d-%02d", year, month)
    }

    fun monthYearLabel(month: Int, year: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, (month - 1).coerceIn(0, 11))
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return monthYearFormat.format(cal.time)
    }

    fun fullMonthYearLabel(month: Int, year: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, (month - 1).coerceIn(0, 11))
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return fullMonthYearFormat.format(cal.time)
    }

    fun formatReceiptNo(sequence: Int): String {
        return String.format(Locale.US, "R-%04d", sequence)
    }

    fun calculateGrade(percentage: Double): String {
        return when {
            percentage >= 90.0 -> "A+"
            percentage >= 80.0 -> "A"
            percentage >= 70.0 -> "B"
            percentage >= 60.0 -> "C"
            percentage >= 50.0 -> "D"
            else -> "F"
        }
    }
}
