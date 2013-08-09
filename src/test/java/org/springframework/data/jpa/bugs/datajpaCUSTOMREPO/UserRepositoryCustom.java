package org.springframework.data.jpa.bugs.datajpaCUSTOMREPO;

import java.util.List;

import org.springframework.data.jpa.bugs.domain.User;

/**
 * Interface for repository functionality that ought to be implemented manually.
 * 
 * @author Oliver Gierke
 */
public interface UserRepositoryCustom {

	/**
	 * Custom repository operation.
	 * 
	 * @return
	 */
	List<User> myCustomBatchOperation();
}
