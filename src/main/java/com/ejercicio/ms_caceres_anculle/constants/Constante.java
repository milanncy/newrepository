package com.ejercicio.ms_caceres_anculle.constants;

public class Constante {
    public static final Integer CODIGO_EXITO=2000;
    public static final Integer CODIGO_ERROR=2005;
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_INACTIVO = "INACTIVO";

    public static final String MENSAJE_EXITO="Empresa Insertada Correctamente";
    public static final String MENSAJE_EXITO_DESDE_BD="Empresa Encontrada en BD";
    public static final String MENSAJE_EXITO_DESDE_REDIS="Empresa Encontrada en REDIS";
    public static final String MENSAJE_EXITO_DELETE="Empresa eliminada de REDIS";
    public static final String MENSAJE_ERROR="Ocurrio un Error en la transacción";
    public static final String MENSAJE_ERROR_NUMDOC_NO_ENCONTRADO="Ocurrio un Error, Documento no encontrado";
    public static final String MENSAJE_ERROR_DOC_NOVALIDO="Ocurrio un Error, Documento ingresado no es válido";


    public static final String AUDIT_ADMIN="MCACERES";

    public static final String REDIS_KEY_GUARDAR="API:CONSUMO:EXTERNA:";
}
