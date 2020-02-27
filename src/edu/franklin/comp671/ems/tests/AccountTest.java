package edu.franklin.comp671.ems.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import edu.franklin.comp671.ems.models.Account;
import edu.franklin.comp671.ems.service.AccountManager;

@TestMethodOrder(OrderAnnotation.class)
class AccountTest {
	static AccountManager accountManager;

	@BeforeAll
	static void setup() {

		accountManager = new AccountManager();
	}

	@Test
	@Tag("Account_Test")
	@Order(1)
	@DisplayName("Create an account with valid field values")
	void create_account_valid_field_values() {
		Account account = new Account();
		account.setEmail("comp1@franklin.edu");
		account.setFirstName("COMP");
		account.setLastName("C671");
		account.setPassword("password123");
		Assertions.assertTrue(accountManager.createAccount(account));
	}

	@Test
	@Order(2)
	@DisplayName("Create an account with invalid data")
	void create_account_invalid_field_values() {
		Account account = new Account();
		Assertions.assertFalse(accountManager.createAccount(account));
	}

	@Test
	@Order(3)
	@DisplayName("Validate whether Account data is saved")
	void create_account_data_saved() {
		Account account = accountManager.getAccount("comp1@franklin.edu");

		Assertions.assertAll("account", () -> assertEquals("COMP", account.getFirstName()),
				() -> assertEquals("C671", account.getLastName()));
	}

	@Test
	@Order(4)
	@DisplayName("Assume account object is created and assert that the values are saved in memory")
	void assume_create_account_object_is_valid() {
		Account tempAccount = new Account();
		tempAccount.setEmail("comp2@franklin.edu");
		tempAccount.setFirstName("COMP");
		tempAccount.setLastName("C671-2");
		tempAccount.setPassword("password1234");
		accountManager.createAccount(tempAccount);
		Assumptions.assumeTrue(accountManager.getAccount("comp2@franklin.edu") != null);
		Account account = accountManager.getAccount("comp2@franklin.edu");
		Assertions.assertAll("account", () -> assertEquals("COMP", account.getFirstName()),
				() -> assertEquals("C671-2", account.getLastName()));
	}
	@Test
	@Order(5)
	@DisplayName("Assume account object is created and assert that the values are saved in memory")
	void assume_false_create_account_object_is_valid() {
		Account tempAccount = new Account();
		tempAccount.setEmail("comp3@franklin.edu");
		tempAccount.setFirstName("COMP");
		tempAccount.setLastName("C671-2");
		tempAccount.setPassword("password1234");
		accountManager.createAccount(tempAccount);
		Assumptions.assumeFalse(accountManager.getAccount("comp3@franklin.edu") != null);
		Account account = accountManager.getAccount("comp3@franklin.edu");
		Assertions.assertAll("account", () -> assertEquals("COMP", account.getFirstName()),
				() -> assertEquals("C671-2", account.getLastName()));
	}

}
