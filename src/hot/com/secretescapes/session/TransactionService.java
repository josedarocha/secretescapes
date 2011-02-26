package com.secretescapes.session;

import java.io.Serializable;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.secretescapes.entity.Account;
import com.secretescapes.entity.Transaction;

/**
 * Component to create a new transaction.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Name("transactionService")
@Scope(ScopeType.CONVERSATION)
public class TransactionService implements Serializable {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -2390650343094703174L;

	@In
	private Session hibernateSession;

	@In
	private MailSender mailSender;

	@In(value = "transactionValidator")
	private TransactionValidator validator;

	private Transaction transaction = new Transaction();

	/**
	 * Validate the data inserted and, if everithig is OK, saves the transaction
	 * on DB and sends an email to the payer and the payee.
	 * 
	 * @return Navigation rule.
	 */
	@Transactional
	public String newTransaction() {
		try {
			if (transactionIsValid()) {
				saveTheTransaction();
				showMessage(Severity.INFO, "transactionInserted");
				sendMail();
				transaction = new Transaction();
			}
		} catch (final Exception e) {
			showMessage(Severity.ERROR, "transactionFailed");
		}
		return null;
	}

	/**
	 * Check if the transaction created is valid. If not, show the error
	 * messages and returns <code>FALSE</code>.
	 * 
	 * @return <code>TRUE</code> if the transaction is valid or
	 *         <code>FALSE</code> if not.
	 */
	private boolean transactionIsValid() {
		return validator.validate(transaction);
	}

	/**
	 * Show a message on the screen.
	 * 
	 * @param severity
	 *            Severity of the message.
	 * @param message
	 *            Message to show.
	 */
	private void showMessage(final Severity severity, final String message) {
		StatusMessages.instance().addFromResourceBundle(severity, message);
	}

	/**
	 * Save to DB the data of the transaction and update the balance of the
	 * payer and payee.
	 */
	private void saveTheTransaction() {
		transaction.commit();
		hibernateSession.persist(transaction);
		hibernateSession.flush();
	}

	/**
	 * Send an email to the payer and the payee.
	 */
	private void sendMail() {
		addReceiver(transaction.getPayer());
		addReceiver(transaction.getPayee());
		mailSender.sendMail(buildMailMessage());
	}

	/**
	 * Build the message send at the mail.
	 * 
	 * @return Mail´s message.
	 */
	private String buildMailMessage() {
		String messageToSend = "";
		messageToSend += "A payment of " + transaction.getAmount()
				+ " has been commited from ";
		messageToSend += transaction.getPayer().getName() + " to "
				+ transaction.getPayee().getName();
		return messageToSend;
	}

	/**
	 * Add the name and email of an account to receive the email.
	 * 
	 * @param account
	 *            Account to add to the mail.
	 */
	private void addReceiver(final Account account) {
		mailSender.addReceiver(account.getName(), account.getEmail());
	}

	/** Accesors for mocks. **/
	public void setSession(final Session sesion) {
		hibernateSession = sesion;
	}

	public void setValidator(final TransactionValidator validator) {
		this.validator = validator;
	}

	public void setMailSender(final MailSender sender) {
		mailSender = sender;
	}

	/** Accesors for JSF. **/

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(final Transaction transaction) {
		this.transaction = transaction;
	}
}