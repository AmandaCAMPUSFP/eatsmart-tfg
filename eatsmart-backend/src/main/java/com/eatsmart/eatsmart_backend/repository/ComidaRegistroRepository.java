package com.eatsmart.eatsmart_backend.repository;

import com.eatsmart.eatsmart_backend.entity.ComidaRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComidaRegistroRepository extends JpaRepository<ComidaRegistro, Long> {
    List<ComidaRegistro> findByUsuarioIdUsuario(Long idUsuario);
    List<ComidaRegistro> findByUsuarioIdUsuarioAndFecha(Long idUsuario, LocalDate fecha);
}