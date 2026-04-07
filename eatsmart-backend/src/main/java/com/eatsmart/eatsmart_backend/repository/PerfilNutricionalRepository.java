package com.eatsmart.eatsmart_backend.repository;

import com.eatsmart.eatsmart_backend.entity.PerfilNutricional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilNutricionalRepository extends JpaRepository<PerfilNutricional, Long> {
}