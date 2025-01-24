package com.svengali.jpa.repository;

import com.svengali.jpa.models.Hash;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface HashRepository extends CrudRepository<Hash, Long>, JpaSpecificationExecutor<Hash> {
    Hash findByHash(String hash);
}