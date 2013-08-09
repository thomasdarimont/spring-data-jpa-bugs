package org.springframework.data.jpa.bugs.domain;

import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Person.class)
public abstract class Person_ {

	public static volatile SingularAttribute<Person, Long> id;
	public static volatile SingularAttribute<Person, String> name;
	public static volatile CollectionAttribute<Person, Address> addresses;

}

