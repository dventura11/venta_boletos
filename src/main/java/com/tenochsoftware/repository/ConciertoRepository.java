package com.tenochsoftware.repository;

import com.tenochsoftware.model.Concierto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConciertoRepository extends JpaRepository<Concierto, Long> {
    
    @Query("SELECT c FROM Concierto c WHERE LOWER(c.artista) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.lugar) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Concierto> buscarPorFiltros(@Param("query") String query);
}
