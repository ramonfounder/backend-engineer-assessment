package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.services.AccountService;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@ActivityImpl
@Component
@RequiredArgsConstructor
public class AccountActivityImpl implements AccountActivity {

  private final AccountService accountService;
  private final PaymentProvider paymentProvider;

  @Override
  public Account saveAccount(Account account) {
    return this.accountService.saveAccount(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    //    if (Objects.requireNonNull(account.getProviderType()) == ProviderType.STRIPE) {
    //      return paymentProvider.createAccount(new CreateAccount(account));
    //    }
    return account;
    // add more providers
    //        throw new IllegalArgumentException("Unsupported provider type: " +
    // account.getProviderType());
  }

  @Override
  public void deleteAccount(Account account) {
    this.accountService.deleteAccount(String.valueOf(account.getId()));
  }
}
