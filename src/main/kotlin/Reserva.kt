package org.example

import java.time.LocalDateTime

data class Reserva(
    val codigoReserva: String,
    val cliente: Cliente,
    val numeroHabitacion: Int,
    val horaCheckIn: LocalDateTime
) {
    companion object {
        private val FORMATO = Regex("^[A-Za-z]{2}\\d{2}[A-Za-z]{2}$")

        fun formatoValido(codigo: String): Boolean = FORMATO.matches(codigo)
    }
}

data class Ticket(
    val numeroTicket: Int,
    val reserva: Reserva,
    val tipoHabitacion: String,
    val tiempoEstadiaMinutos: Int,
    val montoPagado: Double
)

object Validaciones {

    fun validarCodigo(codigo: String): ErrorNovaStay? {
        if (Reserva.formatoValido(codigo))
            return null
        else
            return ErrorNovaStay.CodigoInvalido(codigo)
    }

    fun validarTarifa(monto: Double, esCobroLegitimoCero: Boolean): ErrorNovaStay? {
        if (esCobroLegitimoCero)
            return null
        if (monto <= 0.0)
            return ErrorNovaStay.TarifaInvalida(monto)
        else
            return null
    }
}