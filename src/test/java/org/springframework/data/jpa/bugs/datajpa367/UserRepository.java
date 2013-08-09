package org.springframework.data.jpa.bugs.datajpa367;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.bugs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * @see DATAJPA-354
	 */
	@Query("select u from User u where u.firstname like CONCAT('%',:firstname,'%') and u.lastname like CONCAT('%',:lastname,'%')")
	List<User> findByFirstnameAndLastnameLikeWithConcat(@Param("firstname") String firstname,
			@Param("lastname") String lastname);

	@Query("select u from User u where LOWER(u.firstname) like LOWER(CONCAT('%',:firstname,'%')) and LOWER(u.lastname) like LOWER(CONCAT('%',:lastname,'%'))")
	List<User> findByFirstnameAndLastNameLikeLower(@Param("firstname") String firstname,
			@Param("lastname") String lastname);

	// @Query("select u from User u where u.firstname like concat(:firstname,'%') order by u.firstname")
	@Query("select u from User u where u.firstname like concat(:firstname,'%')")
	List<User> findByFirstname(@Param("firstname") String firstname, Pageable page);
}
