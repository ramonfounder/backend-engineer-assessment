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

  @Override
  public Account saveAccount(Account account) {
    return this.accountService.saveAccount(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    return this.getPaymentProvider(account.getProviderType())
        .createAccount(new CreateAccount(account));
  }

  @Override
  public Account updatePaymentAccount(Account account) {
    return this.getPaymentProvider(account.getProviderType())
        .updateAccount(new UpdateAccount(account));
  }

  @Override
  public void deleteAccount(Account account) {
    this.accountService.deleteAccount(account.getId());
  }

  private PaymentProvider getPaymentProvider(ProviderType providerType) {
    return switch (providerType) {
      case STRIPE -> stripePaymentProvider;
      case PAYPAL -> throw new UnsupportedOperationException("Paypal not implemented");
    };
  }
}
