package com.midas.app.providers.external.stripe;

import com.midas.app.exceptions.PaymentProviderException;
import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.providers.payment.models.CreateAccount;
import com.midas.app.providers.payment.models.UpdateAccount;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import java.util.UUID;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  public StripePaymentProvider(StripeConfiguration configuration) {
    this.configuration = configuration;
    Stripe.apiKey = configuration.getApiKey();
  }

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) {
    String name = String.format(formatName, details.getFirstName(), details.getLastName());
    String email = details.getEmail();
    UUID userId = details.getUserId();
    CustomerCreateParams params =
        CustomerCreateParams.builder().setEmail(email).setName(name).build();
    try {
      var customer = Customer.create(params);
      logger.info("Creating Strip account for user with email: {}", email);
      return Account.builder()
          .providerId(customer.getId())
          .providerType(ProviderType.STRIPE)
          .email(email)
          .id(userId)
          .firstName(details.getFirstName())
          .lastName(details.getLastName())
          .build();
    } catch (StripeException exception) {
      throw new PaymentProviderException(exception.getUserMessage());
    }
  }

  @Override
  public Account updateAccount(UpdateAccount details) {
    String name = String.format(formatName, details.getFirstName(), details.getLastName());
    String email = details.getEmail();
    UUID userId = details.getUserId();
    String providerId = details.getProviderId();
    CustomerUpdateParams params =
        CustomerUpdateParams.builder().setName(name).setEmail(email).build();
    try {
      var customer = Customer.retrieve(providerId);
      customer.update(params);
      logger.info("Updating Stripe account for user with email: {}", email);
      return Account.builder()
          .providerId(providerId)
          .providerType(ProviderType.STRIPE)
          .email(email)
          .id(userId)
          .firstName(details.getFirstName())
          .lastName(details.getLastName())
          .build();
    } catch (StripeException exception) {
      throw new PaymentProviderException(exception.getUserMessage());
    }
  }
}
