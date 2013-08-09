package org.springframework.data.jpa.bugs.datajpa298.repository;

import org.springframework.data.jpa.bugs.datajpa298.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUser, String> {

}
