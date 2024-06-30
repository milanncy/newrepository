package com.ejercicio.ms_caceres_anculle.service;

import com.ejercicio.ms_caceres_anculle.entity.Empresa;
import com.ejercicio.ms_caceres_anculle.request.EmpresaRequest;
import com.ejercicio.ms_caceres_anculle.response.ResponseBase;

import java.io.IOException;
import java.util.List;

public interface EmpresaService {
    ResponseBase crearEmpresa(EmpresaRequest empresaRequest) throws IOException;
    ResponseBase buscarxDocumento(String numDoc);
    List<Empresa> buscarTodos();
    Empresa actualizar(String numDoc, Empresa personaEntity);
    void eliminar(String numeroDoumento);
    Empresa borradoLogico(String numDoc);
}
