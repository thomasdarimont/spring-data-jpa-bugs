package org.springframework.data.jpa.bugs.datajpa105;

import org.springframework.data.jpa.bugs.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long>,
		JpaSpecificationExecutor<Person> {

}
