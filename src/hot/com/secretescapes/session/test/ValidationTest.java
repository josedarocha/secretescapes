package com.secretescapes.session.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.secretescapes.entity.Account;
import com.secretescapes.entity.Transaction;
import com.secretescapes.session.TransactionValidator;

/**
 * Test for the validation of the transaction's atrributes.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
public class ValidationTest {

	private TransactionValidator transactionValidator = new TransactionValidator();
	private final Account payer = new Account("Joe", "joe@secretescapes.com");
	private final Account payee = new Account("Patty",
			"patty@secretescapes.com");

	@Test
	public void errorIfThePayerAndThePayeeIsTheSame() {
		final Transaction transaction = new Transaction(payer, payer, 1D);
		assertEquals(transactionValidator.validate(transaction), false);
	}

	@Test
	public void errorIfTheAmountIsNull() {
		try {
			final Transaction transaction = new Transaction(payer, payee, null);
			assertEquals(transactionValidator.validate(transaction), false);
			fail("Exception expected.");
		} catch (final Exception e) {
			// OK
		}
	}

	@Test
	public void errorIfTheAmountIsZero() {
		final Transaction transaction = new Transaction(payer, payee, 0D);
		assertEquals(transactionValidator.validate(transaction), false);
	}

	@Test
	public void errorIfThereIsNoPayer() {
		try {
			final Transaction transaction = new Transaction(null, payee, 1D);
			assertEquals(transactionValidator.validate(transaction), false);
			fail("Exception expected.");
		} catch (final Exception e) {
			// OK
		}
	}

	@Test
	public void errorIfThereIsNoPayee() {
		try {
			final Transaction transaction = new Transaction(payer, null, 1D);
			assertEquals(transactionValidator.validate(transaction), false);
			fail("Exception expected.");
		} catch (final Exception e) {
			// OK
		}
	}

	@Test
	public void errorIfThePayerHasNotEnoughMoney() {
		final Transaction transaction = new Transaction(payer, payee, 1D);
		final Double balance = transaction.getPayer().getBalance();
		transaction.setAmount(balance + 10D);
		assertEquals(transactionValidator.validate(transaction), false);
	}

	@Test
	public void successIfThePayerHasEnoughMoney() {
		final Transaction transaction = new Transaction(payer, payee, 1D);
		final Double balance = transaction.getPayer().getBalance();
		transaction.setAmount(balance - 10D);
		transaction.getPayer().setId(1L);
		transaction.getPayee().setId(2L);
		assertEquals(transactionValidator.validate(transaction), true);
	}

}