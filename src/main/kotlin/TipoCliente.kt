package org.example

sealed class TipoCliente(val descuento: Double) {

    object Regular: TipoCliente(0.0)

    object Abonado: TipoCliente(0.20)

    object Discapacitado: TipoCliente(0.50)

    companion object {
        fun desdeTexto(texto: String): TipoCliente? = when (texto.trim().lowercase()) {
            "regular" -> Regular
            "abonado" -> Abonado
            "discapacitado" -> Discapacitado
            else -> null
        }

    }
}

data class Cliente (val nombreCompleto: String, val tipoCliente: TipoCliente)