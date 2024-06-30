package com.ejercicio.ms_caceres_anculle.dao;

import com.ejercicio.ms_caceres_anculle.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Empresa findByNumDoc(String numDoc);
    List<Empresa> findByEstado(String estado);
}
