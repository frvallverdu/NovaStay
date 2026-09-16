package org.example

sealed class ErrorNovaStay(val mensaje: String) {
    data class CodigoInvalido(val codigo: String) :
        ErrorNovaStay("Codigo de reserva invalido: '$codigo'. Formato esperado: dos letras, dos digitos, dos letras (ej: AB12CD).")

    data class TarifaInvalida(val monto: Double) :
        ErrorNovaStay("Error de datos: la tarifa calculada ($monto) no puede ser negativa ni igual a cero.")

    data class HuespedNoEncontrado(val codigo: String) :
        ErrorNovaStay("No se encontro ningun huésped con codigo de reserva '$codigo'.")

    object HotelSinCapacidad :
        ErrorNovaStay("El hotel no tiene habitaciones disponibles en este momento.")
}