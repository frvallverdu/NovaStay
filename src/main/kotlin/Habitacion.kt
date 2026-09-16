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

sealed class Habitacion(val numero: Int, var estado: EstadoHabitacion) {

    abstract fun calcularEstadia(horas: Double): Double
    abstract fun nombreTipo(): String

}

sealed class EstadoHabitacion {

    object Disponible: EstadoHabitacion()

    data class Ocupada(val reserva: Reserva): EstadoHabitacion()

    data class EnProceso(val mensaje: String): EstadoHabitacion()

    data class FueraDeServicio(val mensaje: String): EstadoHabitacion()

}

class HabitacionIndividual(numeroHabitacion: Int, estadoIni: EstadoHabitacion): Habitacion(numeroHabitacion, estadoIni){
    override fun calcularEstadia(horas: Double): Double {
        val tarifaBase: Int = 30000
        return (tarifaBase*horas)
    }

    override fun nombreTipo(): String {
        return "Individual"
    }

}

class HabitacionDoble(numeroHabitacion: Int, estadoIni: EstadoHabitacion): Habitacion(numeroHabitacion, estadoIni){
    override fun calcularEstadia(horas: Double): Double {
        if(horas<0.5){
            val tarifaBase: Double = 0.0
            return tarifaBase
        } else {
            val tarifaBase: Int = 45000
            return (tarifaBase*horas)
        }
    }

    override fun nombreTipo(): String {
        return "Doble"
    }

}

class HabitacionSuite(numeroHabitacion: Int, estadoIni: EstadoHabitacion, val premium: Boolean): Habitacion(numeroHabitacion, estadoIni){
    override fun calcularEstadia(horas: Double): Double {
        if(!premium){
            val tarifaBase: Int = 70000
            return (tarifaBase*horas)
        } else {
            val tarifaBase: Double = 70000*1.30
            return (tarifaBase*horas)
        }
    }

    override fun nombreTipo(): String {
        if(!premium){
            return "Suite"
        } else{
            return "Suite Premium"
        }
    }
}




fun calcularTotal(habitacion: Habitacion, cliente: Cliente, horas: Double): Double {
    var monto = habitacion.calcularEstadia(horas)

    if(habitacion is HabitacionIndividual && cliente.tipoCliente is TipoCliente.Abonado ){
        monto = monto * (1.0 - cliente.tipoCliente.descuento)
    }
        monto = monto * 1.19

    if(cliente.tipoCliente is TipoCliente.Discapacitado){
        monto = monto * (1.0 - cliente.tipoCliente.descuento)
    }

    return monto

    }




