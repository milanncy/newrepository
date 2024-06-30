package com.ejercicio.ms_caceres_anculle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="empresa")
@Getter
@Setter
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="numero_documento", nullable = false,unique = true)
    private String numDoc;
    @Column(name="razon_social",nullable = false)
    private String razonSocial;
    @Column(name="tipo_documento",nullable = false)
    private String tipoDocumento;
    @Column(name = "estado",nullable = false)
    private String estado;
    @Column(name = "condicion",nullable = false)
    private String condicion;
    @Column (name = "direccion")
    private String direccion;
    @Column (name = "ubigeo")
    private String ubigeo;
    @Column (name = "via_tipo")
    private String viaTipo;
    @Column (name = "zona_codigo")
    private String zonaCodigo;
    @Column (name = "zona_tipo")
    private String zonaTipo;
    @Column (name = "numero")
    private String numero;
    @Column (name = "interior")
    private String interior;
    @Column (name = "lote")
    private String lote;
    @Column (name = "dpto")
    private String dpto;
    @Column (name = "manzana")
    private String manzana;
    @Column (name = "kilometro")
    private String kilometro;
    @Column (name = "distrito")
    private String distrito;
    @Column (name = "provincia")
    private String provincia;
    @Column (name = "departamento")
    private String departamento;
    @Column (name = "es_agente_retencion",nullable = false)
    private boolean esAgenteRetencion;
    @Column (name = "usuario_creacion")
    private String usuarioCreacion;

    @Column (name = "fecha_creacion")
    private Timestamp fechaCreacion;
    @Column (name = "usuario_actualizacion")
    private String usuarioActualizacion;
    @Column (name = "fecha_actualizacion")
    private Timestamp fechaActualizacion;
    @Column (name = "usuario_eliminacion")
    private String usuarioEliminacion;
    @Column (name = "fecha_eliminacion")
    private Timestamp fechaEliminacion;


}
