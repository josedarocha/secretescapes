package com.secretescapes.session;

import java.util.List;

import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.HibernateEntityQuery;

import com.secretescapes.entity.Account;
import com.secretescapes.entity.Transaction;

/**
 * Component to search transactions.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Name("transactionSearch")
@Scope(ScopeType.CONVERSATION)
public class TransactionSearch extends HibernateEntityQuery<Transaction> {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 3509494409841921744L;

	private static final String ejbql = "select transaction from Transaction transaction ";

	private Account payer = new Account();
	private Account payee = new Account();

	/**
	 * Constructor. Load de basic query.
	 */
	public TransactionSearch() {
		setEjbql(ejbql + " order by date asc");
	}

	/**
	 * Cleans the restrictions on the transactions.
	 * 
	 * @return Navigation rule.
	 */
	public String cleanRestrictions() {
		payer = new Account();
		payee = new Account();
		return null;
	}

	/**
	 * Cleans the transactions shown and search again on DB.
	 * 
	 * @param evento
	 *            Event that launches this query.
	 */
	public void searchAgain(final ActionEvent evento) {
		Contexts.removeFromAllContexts("transactions");
	}

	/**
	 * Calculate the transactions with a factory, to avoid calculate 1 time for
	 * every control (paginator, number of results, results, ...)
	 * 
	 * @return Transactions found on DB.
	 */
	@Factory(value = "transactions", scope = ScopeType.CONVERSATION, autoCreate = true)
	public List<Transaction> findTransactions() {
		return getResultList();
	}

	/**
	 * Search the transactions on DB.
	 */
	@Override
	public List<Transaction> getResultList() {
		String query = searchByPayer("");
		query = searchByPayee(query);
		setEjbql(ejbql + query);
		setOrderColumn("date");
		return super.getResultList();
	}

	/**
	 * Add a new criteria to the query if the payer is selected.
	 * 
	 * @param query
	 *            Original query.
	 * @return Query with the payer's criteria, if it's selected.
	 */
	private String searchByPayer(String query) {
		if (payer != null && payer.getId() != null) {
			if ("".equals(query)) {
				query += " where ";
			}
			query += " payer.id = " + payer.getId();
		}
		return query;
	}

	/**
	 * Add a new criteria to the query if the payee is selected.
	 * 
	 * @param query
	 *            Original query.
	 * @return Query with the payee's criteria, if it's selected.
	 */
	private String searchByPayee(String query) {
		if (payee != null && payee.getId() != null) {
			if ("".equals(query)) {
				query += " where ";
			} else {
				query += " and ";
			}
			query += " payee.id = " + payee.getId();
		}
		return query;
	}

	/** Accesors for JSF. **/

	public Account getPayer() {
		return payer;
	}

	public void setPayer(final Account payer) {
		this.payer = payer;
	}

	public Account getPayee() {
		return payee;
	}

	public void setPayee(final Account payee) {
		this.payee = payee;
	}
}