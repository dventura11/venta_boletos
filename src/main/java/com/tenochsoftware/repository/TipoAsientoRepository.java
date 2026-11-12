package com.tenochsoftware.repository;

import com.tenochsoftware.model.TipoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAsientoRepository extends JpaRepository<TipoAsiento, Long> {
}
