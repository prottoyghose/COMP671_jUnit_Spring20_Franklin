package edu.franklin.comp671.ems.service;

import java.util.HashMap;
import java.util.Map;

import edu.franklin.comp671.ems.models.Account;

public class AccountManager {

	Map<String, Account> accounts = new HashMap<String, Account>();

	public boolean createAccount(Account account) {
		boolean valid = isValidAccount(account);
		if (valid) {
			accounts.put(account.getEmail(), account);

		}
		return valid;
	}

	private boolean isValidAccount(Account account) {
		boolean valid = true;
		if (account.getFirstName() == null || account.getLastName() == null || account.getEmail() == null
				|| account.getPassword() == null) {
			valid = false;
		}
		return valid;
	}

	public Account getAccount(String email) {

		return accounts.get(email);
	}

}
