package org.example

import kotlinx.coroutines.delay

sealed class CheckOut {
    data class Exito(val ticket: Ticket) : CheckOut()
    data class Error(val error: ErrorNovaStay) : CheckOut()
}

suspend fun checkOut(hotel: NovaStay, codigoReserva: String, minutosEstadia: Int): CheckOut {

    // 1) Localizar la habitación con ese código de reserva (R5/R6).
    val habitacion = hotel.buscarHabitacionPorCodigoReserva(codigoReserva)
        ?: return CheckOut.Error(ErrorNovaStay.HuespedNoEncontrado(codigoReserva))

    val estadoActual = habitacion.estado as EstadoHabitacion.Ocupada
    val reserva = estadoActual.reserva

    // 2) Mientras calcula la tarifa, la habitación queda "En proceso".
    habitacion.estado = EstadoHabitacion.EnProceso("Calculando tarifa")
    delay(6500) // simula la comunicación con el sistema de cerraduras / verificación de pago

    // 3) Tiempo de estadía (dato de prueba, no el reloj real -- así se puede probar sin esperar horas).
    val horas = minutosEstadia / 60.0

    // 4) Calcular el monto (R1 + R3: costo por tipo -> IVA -> descuento discapacidad).
    val monto = calcularTotal(habitacion, reserva.cliente, horas)

    // 5) Validar que la tarifa no sea negativa/cero, salvo el caso legítimo de doble < 30 min.
    val esCobroLegitimoCero = habitacion is HabitacionDoble && horas < 0.5
    Validaciones.validarTarifa(monto, esCobroLegitimoCero)?.let {
        habitacion.estado = estadoActual // deshace el "En proceso", vuelve a Ocupada
        return CheckOut.Error(it)
    }

    // 6) Emitir el ticket, registrar en el historial y liberar la habitación.
    val ticket = Ticket(
        numeroTicket = hotel.siguienteNumeroTicket(),
        reserva = reserva,
        tipoHabitacion = habitacion.nombreTipo(),
        tiempoEstadiaMinutos = minutosEstadia,
        montoPagado = monto
    )
    hotel.registrarTicket(ticket)
    habitacion.estado = EstadoHabitacion.Disponible

    return CheckOut.Exito(ticket)
}