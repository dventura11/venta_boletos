package com.tenochsoftware.repository;

import com.tenochsoftware.model.AsientoConcierto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsientoConciertoRepository extends JpaRepository<AsientoConcierto, Long> {
}
