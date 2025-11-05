package org.example.repository;

import org.example.domain.Handover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverRepository extends JpaRepository<Handover, Long> { }
