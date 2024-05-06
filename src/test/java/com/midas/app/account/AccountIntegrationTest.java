package com.midas.app.account;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.shared.AbstractTest;
import com.midas.generated.model.CreateAccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

public class AccountIntegrationTest extends AbstractTest {
  @MockBean private StripePaymentProvider stripePaymentProvider;

  @Autowired private AccountRepository accountRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // * Reset
    accountRepository.deleteAll();

    // * Implement default external dependencies actions (providers, etc)
    when(stripePaymentProvider.createAccount(
            any(com.midas.app.providers.payment.models.CreateAccount.class)))
        .thenReturn(
            Account.builder()
                .firstName("Ramon")
                .lastName("Sharif")
                .providerType(ProviderType.STRIPE)
                .providerId("default-provider-customer-id")
                .email("ramon@gmail.com")
                .build());
  }

  @Test
  @Transactional
  @DisplayName("Given createAccountDto, When the POST /accounts called, Then new valid account")
  void testCreateUserAccountSuccessfully() throws Exception {
    var createAccountDto =
        new CreateAccountDto().firstName("Ramon").lastName("Sharif").email("ramon@gmail.com");

    var request =
        post("/accounts")
            .content(objectMapper.writeValueAsString(createAccountDto))
            .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            jsonPath("$.firstName", is(createAccountDto.getFirstName())),
            jsonPath("$.lastName", is(createAccountDto.getLastName())),
            jsonPath("$.email", is(createAccountDto.getEmail())),
            jsonPath("$.providerType", is(ProviderType.STRIPE.toString().toLowerCase())));
  }

  @Test
  @Transactional
  @DisplayName("Given invalid createAccountDto, When the POST /accounts called, Then failed")
  void testFailureCreateUserAccountForBody() throws Exception {
    var request = post("/accounts").contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request).andDo(print()).andExpectAll(status().isBadRequest());
  }
}
