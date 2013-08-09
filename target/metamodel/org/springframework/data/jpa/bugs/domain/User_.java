package org.springframework.data.jpa.bugs.domain;

import java.util.Date;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, User> manager;
	public static volatile SingularAttribute<User, Date> createdAt;
	public static volatile SetAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, Integer> age;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Boolean> active;
	public static volatile SingularAttribute<User, String> lastname;
	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SetAttribute<User, User> colleagues;

}

