package com.tenochsoftware.repository;

import com.tenochsoftware.model.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecintoRepository extends JpaRepository<Recinto, Long> {
}
