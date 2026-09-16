package org.example

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




