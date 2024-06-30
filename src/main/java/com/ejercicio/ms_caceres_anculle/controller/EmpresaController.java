package com.ejercicio.ms_caceres_anculle.controller;

import com.ejercicio.ms_caceres_anculle.constants.Constante;
import com.ejercicio.ms_caceres_anculle.entity.Empresa;
import com.ejercicio.ms_caceres_anculle.request.EmpresaRequest;
import com.ejercicio.ms_caceres_anculle.response.ResponseBase;
import com.ejercicio.ms_caceres_anculle.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/v1/api/empresa")
@Tag(
        name = "CONSUMO DE API SUNAT RUC",
        description = "Contiene todos los endPoint para el mantenimiento de la API SUNAT RUC"
)
public class EmpresaController {
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @Operation(summary = "Crear una Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Empresa Creada Exitosamente",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Empresa.class))}),
            @ApiResponse(responseCode = "400",description = "Bad Request, El request no cumple con lo esperado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))})
    })
    public ResponseEntity<ResponseBase> crearEmpresa(@RequestBody EmpresaRequest empresaRequest) throws IOException {
        ResponseBase responseBase = empresaService.crearEmpresa(empresaRequest);
        if(responseBase.getCode() == Constante.CODIGO_EXITO){
            return ResponseEntity.ok(responseBase);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBase);
        }
    }

    @GetMapping("/{numDoc}")
    @Operation(summary = "Buscar una Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Empresa Encontrada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))}),
            @ApiResponse(responseCode = "404",description = "Not Found, Empresa no encontrada para ser actualizada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))})
    })
    public ResponseEntity<ResponseBase> buscarxDocumento(@PathVariable String numDoc){
        ResponseBase responseBase = empresaService.buscarxDocumento(numDoc);
        if(responseBase.getCode() == Constante.CODIGO_EXITO){
            return ResponseEntity.ok(responseBase);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBase);
        }
    }

    @GetMapping("/todos")
    @Operation(summary = "Buscar todas las Empresas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado Correcto de Empresas",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found, No se encontraron Empresas con el estado ACTIVO",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))})
    })
    public ResponseEntity<List<Empresa>> buscarTodasEmpresas(){
        return ResponseEntity.ok(empresaService.buscarTodos());
    }

    @PutMapping("/actualizar/{numDoc}")
    @Operation(summary = "Actualizar datos de una Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de Empresa Actualizada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))}),
            @ApiResponse(responseCode = "404", description = "Bad Request, El request no cumple con lo esperado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))})
    })
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable String numDoc, @RequestBody Empresa empresaEntity){
        return ResponseEntity.ok(empresaService.actualizar(numDoc,empresaEntity));
    }

    @DeleteMapping("/{numDoc}")
    public ResponseEntity<ResponseBase> eliminar(@PathVariable String numDoc){
        empresaService.eliminar(numDoc);
        return ResponseEntity.ok(new ResponseBase(Constante.CODIGO_EXITO,Constante.MENSAJE_EXITO_DELETE, Optional.empty()));
    }

    @PutMapping("/eliminadologico/{numDoc}")
    @Operation(summary = "Borrado lógico de una Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa Borrada Correctament",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))}),
            @ApiResponse(responseCode = "404", description = "Bad Request, El request no cumple con lo esperado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Empresa.class))})
    })
    public ResponseEntity<ResponseBase> borradoLogico(@PathVariable String numDoc){
        empresaService.borradoLogico(numDoc);
        return ResponseEntity.ok(new ResponseBase(Constante.CODIGO_EXITO,Constante.MENSAJE_EXITO_DELETE, Optional.empty()));
    }
}
