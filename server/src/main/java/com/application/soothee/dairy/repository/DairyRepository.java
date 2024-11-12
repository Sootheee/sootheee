package com.application.soothee.dairy.repository;

import com.application.soothee.dairy.domain.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyRepository extends JpaRepository<Dairy, Long> {
}
