package com.ejercicio.ms_caceres_anculle.service.impl;

import com.ejercicio.ms_caceres_anculle.clients.ClienteSunat;
import com.ejercicio.ms_caceres_anculle.constants.Constante;
import com.ejercicio.ms_caceres_anculle.dao.EmpresaRepository;
import com.ejercicio.ms_caceres_anculle.entity.Empresa;
import com.ejercicio.ms_caceres_anculle.redis.RedisService;
import com.ejercicio.ms_caceres_anculle.request.EmpresaRequest;
import com.ejercicio.ms_caceres_anculle.response.ResponseBase;
import com.ejercicio.ms_caceres_anculle.response.ResponseSunat;
import com.ejercicio.ms_caceres_anculle.service.EmpresaService;
import com.ejercicio.ms_caceres_anculle.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ClienteSunat clienteSunat;
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    @Value("${token.api}")
    private String tokenApi;

    @Override
    public ResponseBase crearEmpresa(EmpresaRequest empresaRequest) throws IOException {
        Empresa personaEntity = getEntityRestTemplate(empresaRequest);
        if(personaEntity != null){
            empresaRepository.save(personaEntity);
            return new ResponseBase(Constante.CODIGO_EXITO, Constante.MENSAJE_EXITO, Optional.of(personaEntity));
        }else {
            return new ResponseBase(Constante.CODIGO_ERROR, Constante.MENSAJE_ERROR,Optional.empty());
        }
    }

    @Override
    public ResponseBase buscarxDocumento(String numDoc) {
        String redisInfo = redisService.getFromRedis(Constante.REDIS_KEY_GUARDAR+numDoc);
        if(redisInfo!=null){
            Empresa persona = Util.convertirDesdeString(redisInfo,Empresa.class);
            return new ResponseBase(Constante.CODIGO_EXITO, Constante.MENSAJE_EXITO_DESDE_REDIS, Optional.of(persona));
        }else {
            try {
                Optional<Empresa> persona = Optional.ofNullable(empresaRepository.findByNumDoc(numDoc));
                if (persona.isPresent()) {
                    String dataParaRedis = Util.convertirAString(persona.get());
                    redisService.saveInRedis(Constante.REDIS_KEY_GUARDAR + numDoc, dataParaRedis, 2);
                    return new ResponseBase(Constante.CODIGO_EXITO, Constante.MENSAJE_EXITO_DESDE_BD, persona);
                } else {
                    return new ResponseBase(Constante.CODIGO_ERROR, Constante.MENSAJE_ERROR_NUMDOC_NO_ENCONTRADO, Optional.empty());
                }
            } catch (EmptyResultDataAccessException e) {
                return new ResponseBase(Constante.CODIGO_ERROR, Constante.MENSAJE_ERROR_NUMDOC_NO_ENCONTRADO + numDoc, Optional.empty());
            } catch (IllegalArgumentException e) {
                return new ResponseBase(Constante.CODIGO_ERROR, Constante.MENSAJE_ERROR_DOC_NOVALIDO + numDoc, Optional.empty());
            }

        }
    }

    @Override
    public List<Empresa> buscarTodos() {
        return empresaRepository.findByEstado(Constante.ESTADO_ACTIVO).stream().toList();
    }

    @Override
    public Empresa actualizar(String numDoc, Empresa datosParaActualizar) {
        Empresa empresaRecuperada = empresaRepository.findByNumDoc(numDoc);
        if(empresaRecuperada!=null){
            empresaRecuperada.setNumDoc(datosParaActualizar.getNumDoc());
            empresaRecuperada.setRazonSocial(datosParaActualizar.getRazonSocial());
            empresaRecuperada.setTipoDocumento(datosParaActualizar.getTipoDocumento());
            empresaRecuperada.setEstado(datosParaActualizar.getEstado());
            empresaRecuperada.setCondicion(datosParaActualizar.getCondicion());
            empresaRecuperada.setDireccion(datosParaActualizar.getDireccion());
            empresaRecuperada.setEsAgenteRetencion(datosParaActualizar.isEsAgenteRetencion());
            empresaRecuperada.setFechaActualizacion(new Timestamp(System.currentTimeMillis()));
            empresaRecuperada.setUsuarioActualizacion(Constante.AUDIT_ADMIN);
            return empresaRepository.save(empresaRecuperada);
        }else{
            return null;
        }
    }

    @Override
    public void eliminar(String numDoc) {
        redisService.deleteKey(Constante.REDIS_KEY_GUARDAR+numDoc);
    }

    @Override
    public Empresa borradoLogico(String numDoc) {
        Empresa empresaRecuperada = empresaRepository.findByNumDoc(numDoc);
        if(empresaRecuperada!=null){
            empresaRecuperada.setEstado(Constante.ESTADO_INACTIVO);
            empresaRecuperada.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            empresaRecuperada.setUsuarioEliminacion(Constante.AUDIT_ADMIN);
            return empresaRepository.save(empresaRecuperada);
        }
        return null;
    }

    private ResponseSunat getExecutionReniec(String ruc){
        String auth = "Bearer "+tokenApi;
        ResponseSunat reniec = clienteSunat.getInfoReniec(ruc,auth);
        return reniec;
    }

    private Empresa getEntity(EmpresaRequest empresaRequest){
        Empresa empresaEntity = new Empresa();
        ResponseSunat responseSunat = getExecutionReniec(empresaRequest.getRuc());
        if(responseSunat != null){
            empresaEntity.setNumDoc(responseSunat.getNumeroDocumento());
            empresaEntity.setRazonSocial(responseSunat.getRazonSocial());
            empresaEntity.setTipoDocumento(responseSunat.getTipoDocumento());
            empresaEntity.setEstado(responseSunat.getEstado());
            empresaEntity.setCondicion(responseSunat.getCondicion());
            empresaEntity.setDireccion(responseSunat.getDireccion());
            empresaEntity.setEsAgenteRetencion(responseSunat.isEsAgenteRetencion());
            empresaEntity.setUsuarioCreacion(Constante.AUDIT_ADMIN);
            empresaEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            return empresaEntity;
        }else {
            return null;
        }

    }

    private Empresa getEntityRestTemplate(EmpresaRequest personaRequest){
        String url = "https://api.apis.net.pe/v2/sunat/ruc?numero="+personaRequest.getRuc();

        try {
            ResponseEntity<ResponseSunat> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders(tokenApi)),
                    ResponseSunat.class
            );
            ResponseSunat responseSunat = response.getBody();
            Empresa empresaEntity = new Empresa();

            empresaEntity.setNumDoc(responseSunat.getNumeroDocumento());
            empresaEntity.setRazonSocial(responseSunat.getRazonSocial());
            empresaEntity.setTipoDocumento(responseSunat.getTipoDocumento());
            empresaEntity.setEstado(responseSunat.getEstado());
            empresaEntity.setCondicion(responseSunat.getCondicion());
            empresaEntity.setDireccion(responseSunat.getDireccion());
            empresaEntity.setEsAgenteRetencion(responseSunat.isEsAgenteRetencion());
            empresaEntity.setUsuarioCreacion(Constante.AUDIT_ADMIN);
            empresaEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            return empresaEntity;
        }catch (HttpClientErrorException e){
            System.err.println("ERROR AL CONSUMIR EL API EXTERNA " +e.getStatusCode());
        }

        return null;
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + token);
        return headers;
    }
}
