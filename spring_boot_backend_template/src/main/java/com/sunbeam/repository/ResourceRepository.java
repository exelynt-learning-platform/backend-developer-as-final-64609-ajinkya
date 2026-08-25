package com.sunbeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
