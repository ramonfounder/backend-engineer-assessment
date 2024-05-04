package com.midas.app.providers.payment;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.models.CreateAccount;
import com.midas.app.providers.payment.models.UpdateAccount;

public interface PaymentProvider {

  String formatName = "%s %s";

  /** providerName is the name of the payment provider */
  String providerName();

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(CreateAccount details);

  /**
   * updateAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  public Account updateAccount(UpdateAccount details);
}
