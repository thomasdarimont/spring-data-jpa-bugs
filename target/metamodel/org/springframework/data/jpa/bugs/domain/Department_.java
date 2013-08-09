package org.springframework.data.jpa.bugs.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Department.class)
public abstract class Department_ {

	public static volatile SingularAttribute<Department, String> name;
	public static volatile SingularAttribute<Department, Long> departmentId;

}

