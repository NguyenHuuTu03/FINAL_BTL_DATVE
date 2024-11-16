package org.example.final_btl_datve.repository;

import org.example.final_btl_datve.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
}
