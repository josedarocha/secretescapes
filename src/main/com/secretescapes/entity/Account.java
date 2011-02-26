package com.secretescapes.entity;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.validator.Email;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

/**
 * Account of a person.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Entity
public class Account extends AbstractEntity {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -429443491778058126L;

	private String name;
	private String email;
	private Double balance = 200D;

	/**
	 * Constructor.
	 */
	public Account() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Owner's name.
	 * @param email
	 *            Owner's email.
	 */
	public Account(final String name, final String email) {
		super();
		this.name = name;
		this.email = email;
	}

	/**
	 * Check if there is enough money in the account.
	 * 
	 * @param amount
	 *            Amount of money to extract.
	 * @return <code>TRUE</code> if there is enough money in the account or
	 *         <code>FALSE</code> if not.
	 */
	public boolean canIExtractThisAmount(final Double amount) {
		return balance >= amount;
	}

	/**
	 * Extract money from the account.
	 * 
	 * @param amount
	 *            Amount of money to extract.
	 */
	public void extractMoney(final Double amount) {
		balance -= amount;
	}

	/**
	 * Add money from the account.
	 * 
	 * @param amount
	 *            Amount of money to add.
	 */
	public void addMoney(final Double amount) {
		balance += amount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transient
	public Class tellMeYourClass() {
		return Account.class;
	}

	/**
	 * @return Owner's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Owner's name.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Owner's email.
	 */
	@Email
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            Owner's email.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @return Account's current balance. Must be always positive.
	 */
	@Min(value = 0, message = "#{messages.balanceMustBePositive}")
	@NotNull
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            Account's current balance.
	 */
	public void setBalance(final Double balance) {
		this.balance = balance;
	}
}