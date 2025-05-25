package ru.top.cinemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.top.cinemas.entities.Hall;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall,Long> {

    List<Hall> findAllByActiveHall(boolean isActive);

    boolean existsByName(String name);

}
