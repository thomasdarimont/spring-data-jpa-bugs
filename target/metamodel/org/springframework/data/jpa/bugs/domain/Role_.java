package org.springframework.data.jpa.bugs.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Role.class)
public abstract class Role_ {

	public static volatile SingularAttribute<Role, Integer> id;
	public static volatile SingularAttribute<Role, String> name;

}

