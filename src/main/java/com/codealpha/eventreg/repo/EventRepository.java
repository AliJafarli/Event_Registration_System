package com.codealpha.eventreg.repo;

import com.codealpha.eventreg.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);

    Page<Event> findByStatusAndStartAtAfterOrderByStartAtAsc(Event.Status status, OffsetDateTime after, Pageable pageable);

    Optional<Event> findByIdAndStatus(Long id, Event.Status status);
}
