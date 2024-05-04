package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.providers.payment.models.CreateAccount;
import com.midas.app.providers.payment.models.UpdateAccount;
import com.midas.app.services.AccountService;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@ActivityImpl
@Component
@RequiredArgsConstructor
public class AccountActivityImpl implements AccountActivity {

  private final AccountService accountService;
  private final StripePaymentProvider stripePaymentProvider;

  /**
   * saveAccount saves an account in the data store.
   *
   * @param account is the account to be saved
   * @return Account
   */
  @Override
  public Account saveAccount(Account account) {
    return this.accountService.saveAccount(account);
  }

  /**
   * createPaymentAccount creates a payment account in the provider.
   *
   * @param account is the account to be created
   * @return Account
   */
  @Override
  public Account createPaymentAccount(Account account) {
    return this.getPaymentProvider(account.getProviderType())
        .createAccount(new CreateAccount(account));
  }

  /**
   * updatePaymentAccount updates the payment account in the provider.
   *
   * @param account is the account to be created
   * @return Account
   */
  @Override
  public Account updatePaymentAccount(Account account) {
    return this.getPaymentProvider(account.getProviderType())
        .updateAccount(new UpdateAccount(account));
  }

  /**
   * saveAccount deletes the account in the data store.
   *
   * @param account is the account to be deleted
   */
  @Override
  public void deleteAccount(Account account) {
    this.accountService.deleteAccount(account.getId());
  }

  /**
   * getPaymentProvider gets the paymentProvider.
   *
   * @param providerType is the providerType
   */
  private PaymentProvider getPaymentProvider(ProviderType providerType) {
    return switch (providerType) {
      case STRIPE -> stripePaymentProvider;
      case PAYPAL -> throw new UnsupportedOperationException("Paypal not implemented");
    };
  }
}
