package com.ejercicio.ms_caceres_anculle.util;

import com.ejercicio.ms_caceres_anculle.entity.Empresa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    public static String convertirAString(Empresa empresa){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(empresa);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertirDesdeString(String json, Class<T> tipoClase){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json,tipoClase);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
