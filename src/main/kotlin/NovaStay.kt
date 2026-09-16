package org.example

class NovaStay(val nombre: String) {

    val habitaciones: MutableList<Habitacion> = mutableListOf(
        HabitacionIndividual(1, EstadoHabitacion.Disponible),
        HabitacionIndividual(2, EstadoHabitacion.Disponible),
        HabitacionIndividual(3, EstadoHabitacion.Disponible),
        HabitacionDoble(4, EstadoHabitacion.Disponible),
        HabitacionDoble(5, EstadoHabitacion.Disponible),
        HabitacionDoble(6, EstadoHabitacion.Disponible),
        HabitacionSuite(7, EstadoHabitacion.Disponible, premium = false),
        HabitacionSuite(8, EstadoHabitacion.Disponible, premium = false),
        HabitacionSuite(9, EstadoHabitacion.Disponible, premium = true),
        HabitacionSuite(10, EstadoHabitacion.Disponible, premium = true)
    )

    val historialTickets: MutableList<Ticket> = mutableListOf()
    var recaudacionTotal: Double = 0.0
    val recaudacionPorTipo: MutableMap<String, Double> = mutableMapOf()
    var contadorTickets = 0

    fun buscarHabitacionDisponible(): Habitacion? =
        habitaciones.firstOrNull { it.estado is EstadoHabitacion.Disponible }

    fun buscarHabitacionPorCodigoReserva(codigo: String): Habitacion? =
        habitaciones.firstOrNull { habitacion ->
            val estado = habitacion.estado
            estado is EstadoHabitacion.Ocupada && estado.reserva.codigoReserva == codigo
        }

    fun habitacionesDisponibles(): Int =
        habitaciones.count { it.estado is EstadoHabitacion.Disponible }

    fun siguienteNumeroTicket(): Int {
        contadorTickets++
        return contadorTickets
    }

    fun registrarTicket(ticket: Ticket) {
        historialTickets.add(ticket)
        recaudacionTotal += ticket.montoPagado
        recaudacionPorTipo[ticket.tipoHabitacion] =
            (recaudacionPorTipo[ticket.tipoHabitacion] ?: 0.0) + ticket.montoPagado
    }
}