package com.tenochsoftware.repository;

import com.tenochsoftware.model.Artista;
import com.tenochsoftware.model.Concierto;
import com.tenochsoftware.model.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ConciertoRepository extends JpaRepository<Concierto, Long> {

    @Query("SELECT DISTINCT c FROM Concierto c LEFT JOIN c.artistas a WHERE "
            + "LOWER(a.nombre) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(c.recinto.nombre) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(c.recinto.ciudad) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Concierto> buscarPorFiltros(@Param("query") String query);

    @Query("SELECT COUNT(c) > 0 FROM Concierto c JOIN c.artistas a "
            + "WHERE a IN :artistas AND c.fecha = :fecha AND c.id <> :id")
    boolean existeArtistaOcupado(@Param("artistas") List<Artista> artistas,
                                 @Param("fecha") LocalDate fecha,
                                 @Param("id") Long id);

    boolean existsByRecintoAndFechaAndIdNot(Recinto recinto, LocalDate fecha, Long id);
}
