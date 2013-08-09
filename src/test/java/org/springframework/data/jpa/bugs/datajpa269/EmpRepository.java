package org.springframework.data.jpa.bugs.datajpa269;

import org.springframework.data.jpa.bugs.domain.Employee;
import org.springframework.data.jpa.bugs.domain.EmployeePK;
import org.springframework.data.jpa.repository.JpaRepository;

interface EmpRepository extends JpaRepository<Employee, EmployeePK> {}
