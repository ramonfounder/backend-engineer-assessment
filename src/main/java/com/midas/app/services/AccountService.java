package com.midas.app.services;

import com.midas.app.models.Account;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details);

  Account updateAccount(UUID id, Account details);

  /**
   * saveAccount saves a new account in the system.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account saveAccount(Account details);

  /**
   * deleteAccount deletes ths account in the system by id.
   *
   * @param id is the id of the account.
   */
  void deleteAccount(UUID id);

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  /**
   * getAccount returns an account by id.
   *
   * @return Account
   */
  Account getAccountById(UUID id);
}
