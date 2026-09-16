package org.example

import kotlinx.coroutines.delay

sealed class CheckIn {
    data class Exito(val habitacion: Habitacion, val reserva: Reserva) : CheckIn()
    data class Error(val error: ErrorNovaStay) : CheckIn()
}

suspend fun checkIn(
    novaStay: NovaStay,
    codigoReserva: String,
    nombreCliente: String,
    tipoClienteTexto: String,
    tipoHabitacionDeseado: String
): CheckIn {

    // 1) Validar código de reserva (R6).
    Validaciones.validarCodigo(codigoReserva)?.let { return CheckIn.Error(it) }

    // 2) Validar tipo de cliente (R3).
    val tipoCliente = TipoCliente.desdeTexto(tipoClienteTexto)
        ?: return CheckIn.Error(ErrorNovaStay.CodigoInvalido(tipoClienteTexto))

    // 3) Buscar la primera habitación disponible del tipo pedido (R5).
    val habitacion = novaStay.habitaciones.firstOrNull {
        it.estado is EstadoHabitacion.Disponible && it.nombreTipo().startsWith(tipoHabitacionDeseado, ignoreCase = true)
    } ?: return CheckIn.Error(ErrorNovaStay.HotelSinCapacidad)

    // 4) Mientras espera al sistema de cerraduras, la habitación queda "En proceso".
    habitacion.estado = EstadoHabitacion.EnProceso("Registrando check-in")
    delay(3000) // simula la comunicación con el sistema de cerraduras (no bloquea el hilo principal)

    // 5) Confirmada la operación: se crea la reserva y la habitación pasa a Ocupada.
    val cliente = Cliente(nombreCliente, tipoCliente)
    val reserva = Reserva(codigoReserva, cliente, habitacion.numero, java.time.LocalDateTime.now())
    habitacion.estado = EstadoHabitacion.Ocupada(reserva)

    return CheckIn.Exito(habitacion, reserva)
}