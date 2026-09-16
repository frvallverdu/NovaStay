package org.example

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val hotel = NovaStay("NovaStay Centro")

    var opcion: String
    do {
        println()
        println("===== ${hotel.nombre} =====")
        println("1. Check-in")
        println("2. Check-out")
        println("3. Habitaciones disponibles")
        println("4. Reporte de cierre de turno")
        println("0. Salir")
        print("Elige una opcion: ")
        opcion = readln()

        when (opcion) {
            "1" -> registrarCheckIn(hotel)
            "2" -> registrarCheckOut(hotel)
            "3" -> println("Habitaciones disponibles: ${hotel.habitacionesDisponibles()}")
            "4" -> mostrarReporteCierre(hotel)
            "0" -> println("Cerrando sistema...")
            else -> println("Opción no valida.")
        }
    } while (opcion != "0")
}

suspend fun registrarCheckIn(hotel: NovaStay) {
    print("Codigo de reserva: ")
    val codigo = readln()
    print("Nombre del huesped: ")
    val nombre = readln()
    print("Tipo de cliente (regular/abonado/discapacitado): ")
    val tipoCliente = readln()
    print("Tipo de habitacion (individual/doble/suite): ")
    val tipoHabitacionDeseada = readln()

    println("Procesando check-in, comunicando con el sistema de cerraduras...")
    when (val resultado = checkIn(hotel, codigo, nombre, tipoCliente, tipoHabitacionDeseada)) {
        is CheckIn.Exito ->
            println("Check-in exitoso: habitacion ${resultado.habitacion.numero} (${resultado.habitacion.nombreTipo()}) asignada a $nombre.")
        is CheckIn.Error ->
            println("Error: ${resultado.error.mensaje}")
    }
}

suspend fun registrarCheckOut(hotel: NovaStay) {
    print("Codigo de reserva: ")
    val codigo = readln()
    print("Tiempo de estadia (minutos): ")
    val minutos: Int
    try {
        minutos = readln().toInt()
    } catch (e: NumberFormatException) {
        println("Error: el tiempo de estadía debe ser un número entero de minutos.")
        return
    }

    println("Procesando check-out, calculando tarifa...")
    when (val resultado = checkOut(hotel, codigo, minutos)) {
        is CheckOut.Exito -> {
            val ticket = resultado.ticket
            println("Check-out exitoso. Ticket #${ticket.numeroTicket}")
            println("  Tipo: ${ticket.tipoHabitacion}")
            println("  Codigo de reserva: ${ticket.reserva.codigoReserva}")
            println("  Tiempo de estadia: ${ticket.tiempoEstadiaMinutos} min")
            println("  Monto pagado: $${"%.0f".format(ticket.montoPagado)}")
        }
        is CheckOut.Error ->
            println("Error: ${resultado.error.mensaje}")
    }
}

fun mostrarReporteCierre(hotel: NovaStay) {
    println()
    println("===== Reporte de cierre de turno - ${hotel.nombre} =====")
    for (ticket in hotel.historialTickets) {
        println("Ticket #${ticket.numeroTicket} | ${ticket.tipoHabitacion} | ${ticket.reserva.codigoReserva} | ${ticket.tiempoEstadiaMinutos} min | $${"%.0f".format(ticket.montoPagado)}")
    }
    val cantidadHuespedes = hotel.historialTickets.size
    val ingresoPromedio = if (cantidadHuespedes > 0) hotel.recaudacionTotal / cantidadHuespedes else 0.0
    val tipoQueMasIngresoGenero = hotel.recaudacionPorTipo.maxByOrNull { it.value }?.key ?: "N/A"

    println()
    println("Total recaudado: $${"%.0f".format(hotel.recaudacionTotal)}")
    println("Huespedes atendidos: $cantidadHuespedes")
    println("Ingreso promedio por huesped: $${"%.0f".format(ingresoPromedio)}")
    println("Tipo de habitacion que más ingresos genero: $tipoQueMasIngresoGenero")
    println("Habitaciones disponibles al cierre: ${hotel.habitacionesDisponibles()}")
}