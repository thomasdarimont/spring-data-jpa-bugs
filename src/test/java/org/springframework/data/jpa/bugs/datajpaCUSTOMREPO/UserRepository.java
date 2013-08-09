package org.springframework.data.jpa.bugs.datajpaCUSTOMREPO;

import java.util.List;

import org.springframework.data.jpa.bugs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link User} instances. Provides basic CRUD operations due to the extension of
 * {@link JpaRepository}. Includes custom implemented functionality by extending {@link UserRepositoryCustom}.
 * 
 * @author Oliver Gierke
 */
public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom {

	/**
	 * Find all users with the given lastname. This method will be translated into a query by constructing it directly
	 * from the method name as there is no other query declared.
	 * 
	 * @param lastname
	 * @return
	 */
	List<User> findByLastname(String lastname);
}
