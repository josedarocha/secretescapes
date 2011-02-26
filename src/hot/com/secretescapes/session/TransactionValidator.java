package com.secretescapes.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.secretescapes.entity.Transaction;

/**
 * Component to validate entities.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Name("transactionValidator")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class TransactionValidator implements Serializable {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 7366466782497598740L;

	private Transaction transaction;
	private Map<Severity, List<String>> errors = new HashMap<Severity, List<String>>();

	/**
	 * Validate an entity.
	 * 
	 * @param elementToValidate
	 *            Entity that is going to be validated.
	 * @return If the entity is correct <code>TRUE</code> or not,
	 *         <code>FALSE</code>.
	 */
	public boolean validate(final Transaction transaction) {
		this.transaction = transaction;
		validateEntity();
		validateIfThePayerHasEnoughMoney();
		showMessages();
		return errors.isEmpty();
	}

	/**
	 * Validate with hibernate that all the atributes of the transaction and the
	 * account are valid.
	 */
	@SuppressWarnings("unchecked")
	private void validateEntity() {
		final ClassValidator<Transaction> validator = new ClassValidator<Transaction>(
				transaction.tellMeYourClass());
		final InvalidValue[] errors = validator.getInvalidValues(transaction);

		if (errors != null && errors.length > 0) {
			if (!this.errors.containsKey(Severity.ERROR)) {
				this.errors.put(Severity.ERROR, new ArrayList<String>());
			}

			for (final InvalidValue invalidValue : errors) {
				this.errors.get(Severity.ERROR).add(invalidValue.getMessage());
			}
		}
	}

	/**
	 * Validate that there is enough money in the payer's accoun for this
	 * transaction.
	 */
	private void validateIfThePayerHasEnoughMoney() {
		if (!transaction.getPayer().canIExtractThisAmount(
				transaction.getAmount())) {
			if (!errors.containsKey(Severity.ERROR)) {
				errors.put(Severity.ERROR, new ArrayList<String>());
			}
			errors.get(Severity.ERROR).add("#{messages.hasNotEnoughMoney}");
		}
	}

	/**
	 * Show the messages received.
	 * 
	 */
	public void showMessages() {
		if (errors.keySet() != null) {
			for (final Entry<Severity, List<String>> tupla : errors.entrySet()) {
				for (final String mensaje : tupla.getValue()) {
					StatusMessages.instance().add(tupla.getKey(), mensaje);
				}
			}
		}
	}
}