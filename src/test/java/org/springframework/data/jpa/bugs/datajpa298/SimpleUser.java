package org.springframework.data.jpa.bugs.datajpa298;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

@Configurable
public class SimpleUser implements User {

	// @Autowired //
	@Inject//
	private Environment environment;

	private String username;

	public SimpleUser(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Transactional
	public Environment getEnvironment() {
		return environment;
	}

}
