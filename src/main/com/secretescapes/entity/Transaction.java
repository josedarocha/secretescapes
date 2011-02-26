package com.secretescapes.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.validator.AssertTrue;
import org.hibernate.validator.Min;
import org.hibernate.validator.Valid;

/**
 * Transaction to remove money from an account and add to othe account.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Entity
public class Transaction extends AbstractEntity {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -4772856753984128005L;

	private Date date = new Date();
	private Account payer;
	private Account payee;
	private Double amount;

	/**
	 * Constructor.
	 */
	public Transaction() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param payer
	 *            Payer of the transaction.
	 * @param payee
	 *            Payee of the transaction.
	 * @param amount
	 *            Amount of money.
	 */
	public Transaction(final Account payer, final Account payee,
			final Double amount) {
		super();
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
	}

	/**
	 * Check if there is enough money in the account.
	 * 
	 * @param amount
	 *            Amount of money to extract.
	 * @return <code>TRUE</code> if the payer is different from the payee or
	 *         <code>FALSE</code> if not.
	 */
	@AssertTrue(message = "#{messages.payerAndPayeeMustBeDifferent}")
	public boolean checkThatThePayerAndThePayeeAreNotTheSame() {
		return payer.getId() != payee.getId();
	}

	/**
	 * Extract money from the payer's account and add to the payee's account.
	 */
	public void commit() {
		getPayer().extractMoney(amount);
		getPayee().addMoney(amount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transient
	public Class tellMeYourClass() {
		return Transaction.class;
	}

	/**
	 * @return Date when the transaction is done.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            Date when the transaction is done.
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * @return Account from which you remove the amount of money.
	 */
	@ManyToOne
	@Valid
	public Account getPayer() {
		return payer;
	}

	/**
	 * @param payer
	 *            Account from which you remove the amount of money.
	 */
	public void setPayer(final Account payer) {
		this.payer = payer;
	}

	/**
	 * @return Account to which you add the amount of money.
	 */
	@ManyToOne
	@Valid
	public Account getPayee() {
		return payee;
	}

	/**
	 * @param payee
	 *            Account to which you add the amount of money.
	 */
	public void setPayee(final Account payee) {
		this.payee = payee;
	}

	/**
	 * @return Amount of money. Must be positive.
	 */
	@Min(value = 1, message = "#{messages.transactionAmountMustBePositive}")
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            Amount of money.
	 */
	public void setAmount(final Double amount) {
		this.amount = amount;
	}
}
