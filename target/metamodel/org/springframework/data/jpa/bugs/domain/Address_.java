package org.springframework.data.jpa.bugs.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Address.class)
public abstract class Address_ {

	public static volatile SingularAttribute<Address, Long> id;
	public static volatile SingularAttribute<Address, Person> person;
	public static volatile SingularAttribute<Address, String> street;

}

