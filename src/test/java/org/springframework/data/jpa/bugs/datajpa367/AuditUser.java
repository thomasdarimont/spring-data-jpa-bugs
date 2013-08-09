/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.bugs.datajpa367;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

/**
 * @author Thomas Darimont
 */
@Entity
public class AuditUser implements Auditable<AuditUser, Long> {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue Long id;
	String name;

	DateTime createdDate;
	DateTime lastModifiedDate;

	AuditUser createdBy;
	AuditUser lastModifiedBy;

	/* (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return id == null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the createdDate
	 */
	public DateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the createdBy
	 */
	public AuditUser getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(AuditUser createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public AuditUser getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(AuditUser lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
