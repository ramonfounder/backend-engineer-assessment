package com.midas.app.activities;

import com.midas.app.models.Account;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AccountActivity {
  /**
   * saveAccount saves an account in the data store.
   *
   * @param account is the account to be saved
   * @return Account
   */
  @ActivityMethod
  Account saveAccount(Account account);

  /**
   * createPaymentAccount creates a payment account in the provider.
   *
   * @param account is the account to be created
   * @return Account
   */
  @ActivityMethod
  Account createPaymentAccount(Account account);

  /**
   * updatePaymentAccount updates the payment account in the provider.
   *
   * @param account is the account to be created
   * @return Account
   */
  @ActivityMethod
  Account updatePaymentAccount(Account account);

  /**
   * saveAccount deletes the account in the data store.
   *
   * @param account is the account to be deleted
   */
  @ActivityMethod
  void deleteAccount(Account account);
}
