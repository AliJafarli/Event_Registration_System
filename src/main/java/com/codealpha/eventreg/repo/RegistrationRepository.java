package com.codealpha.eventreg.repo;

import com.codealpha.eventreg.domain.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    long countByEventIdAndStatus(Long eventId, Registration.Status status);
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
    Page<Registration> findByUserIdOrderByRegisteredAtDesc(Long userId, Pageable pageable);
    Page<Registration> findByEventIdOrderByRegisteredAtDesc(Long eventId, Pageable pageable);

    @Query("""
    select r from Registration r
    join fetch r.event
    where r.user.id = :userId
    order by r.registeredAt desc
""")
    Page<Registration> findByUserIdWithEvent(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
    select r from Registration r
    join fetch r.event
    where r.event.id = :eventId
    order by r.registeredAt desc
""")
    Page<Registration> findByEventIdWithEvent(
            @Param("eventId") Long eventId,
            Pageable pageable
    );


}
