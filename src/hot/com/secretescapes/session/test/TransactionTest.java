package com.secretescapes.session.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.mockito.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.secretescapes.entity.Transaction;
import com.secretescapes.session.MailSender;
import com.secretescapes.session.TransactionService;
import com.secretescapes.session.TransactionValidator;

/**
 * Test for the validation of the transaction registration.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
public class TransactionTest {

	private TransactionService transactionService;
	private TransactionValidator validator;
	private MailSender mailSender;
	private Session hibernateSession;

	private Transaction transaction;

	@BeforeMethod
	public void setup() {
		transactionService = new TransactionService();

		hibernateSession = mock(Session.class);
		transactionService.setSession(hibernateSession);

		validator = mock(TransactionValidator.class);
		transactionService.setValidator(validator);

		mailSender = mock(MailSender.class);
		transactionService.setMailSender(mailSender);
	}

	@Test
	public void checkThatIfTheTransactionIsValidItIsRegistered()
			throws Exception {
		transaction = new Transaction();
		when(validator.validate(transaction)).thenReturn(true);

		transactionService.newTransaction();
		verify(hibernateSession, times(1)).persist(
				Matchers.any(Transaction.class));
	}

	@Test
	public void checkThatIfTheTransactionIsNotValidItIsNotRegistered()
			throws Exception {
		transaction = new Transaction();
		when(validator.validate(transaction)).thenReturn(false);

		transactionService.newTransaction();
		verify(hibernateSession, times(0)).persist(
				Matchers.any(Transaction.class));
	}
}