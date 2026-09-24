package org.example.footgamers.exception;

public class MensajeException {

    private MensajeException() {
    }

    public static final String VALOR_NO_ENCONTRADO = "El %s con id %s no se ha encontrado";
    public static final String VALOR_YA_EN_USO = "El %s con %s %s ya se encuentra registrado";
    public static final String PARTICIPACIONES_NO_CONFIRMADAS =
            "Todas las participaciones deben estar confirmadas para asignar el ganador";
}