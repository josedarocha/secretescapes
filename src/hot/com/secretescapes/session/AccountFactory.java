package com.secretescapes.session;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.secretescapes.entity.Account;

/**
 * Factory to search accounts on DB.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Name("accountFactory")
public class AccountFactory {

	@In
	private Session hibernateSession;

	/**
	 * Factory to search accounts on DB.
	 * 
	 * @return Accoun's recorded on DB.
	 */
	@SuppressWarnings("unchecked")
	@Factory(value = "accounts", scope = ScopeType.CONVERSATION, autoCreate = true)
	public List<Account> getAccounts() {
		final Criteria criteria = hibernateSession
				.createCriteria(Account.class);
		return criteria.list();
	}
}