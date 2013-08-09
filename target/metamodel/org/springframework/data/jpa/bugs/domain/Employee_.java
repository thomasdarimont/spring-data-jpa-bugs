package org.springframework.data.jpa.bugs.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Employee.class)
public abstract class Employee_ {

	public static volatile SingularAttribute<Employee, Department> department;
	public static volatile SingularAttribute<Employee, Long> empId;

}

