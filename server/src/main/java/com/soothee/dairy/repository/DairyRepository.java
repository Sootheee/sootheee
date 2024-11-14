package com.soothee.dairy.repository;

import com.soothee.dairy.domain.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyRepository extends JpaRepository<Dairy, Long> {
}
