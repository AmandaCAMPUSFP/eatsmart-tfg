package com.eatsmart.eatsmart_backend.repository;

import com.eatsmart.eatsmart_backend.entity.RefreshTokenInvalidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Repositorio para la blacklist de refresh tokens invalidados.
 */
@Repository
public interface RefreshTokenInvalidadoRepository extends JpaRepository<RefreshTokenInvalidado, Long> {

    /**
     * Comprueba si un token (por su hash) está en la blacklist.
     */
    boolean existsByTokenHash(String tokenHash);

    /**
     * Borra todos los registros cuya fecha de expiración ya pasó.
     * Lo usa el job de limpieza programado.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenInvalidado r WHERE r.fechaExpiracion < :ahora")
    int eliminarExpirados(@Param("ahora") LocalDateTime ahora);
}