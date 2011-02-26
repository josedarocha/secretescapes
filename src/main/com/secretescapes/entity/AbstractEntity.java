package com.secretescapes.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Common atributes of the entities.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -4772856753984128005L;

	protected Long id;

	/**
	 * @return The class of the entity, used to validate it.
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public abstract Class tellMeYourClass();

	/** Accesors for hibernate. **/

	/**
	 * @return Id of the entity.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            Id of the entity.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

}
