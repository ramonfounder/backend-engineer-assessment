package com.midas.app.account;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.midas.generated.model.UpdateAccountDto;
import java.util.HashMap;
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
        .thenReturn(Account.builder().firstName("Ramon").lastName("Sharif").email("ramon@gamil.com").build());
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
            jsonPath("$.providerType", is(ProviderType.STRIPE.toString().toLowerCase())),
            jsonPath("$.providerId", is("default-provider-customer-id")));
  }

  @Test
  @Transactional
  @DisplayName("Given invalid createAccountDto, When the POST /accounts called, Then failed")
  void testFailureCreateUserAccountForBody() throws Exception {
    var request = post("/accounts").contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request).andDo(print()).andExpectAll(status().isBadRequest());
  }

  @Test
  @Transactional
  @DisplayName(
      "Given updateAccountDto, When the PATCH /accounts called, Then account successfully updated")
  void testUpdateUserAccountSuccessfully() throws Exception {
    var createAccountDto =
        new CreateAccountDto().firstName("Ramon").lastName("Sharif").email("ramon@gmail.com");
    var updateAccountDto =
        new UpdateAccountDto().firstName("Ramon2").lastName("Sharif2").email("ramon2@gmail.com");

    // * Create new account
    var createAccountRequest =
        post("/accounts")
            .content(objectMapper.writeValueAsString(createAccountDto))
            .contentType(MediaType.APPLICATION_JSON);
    var response =
        objectMapper.readValue(
            mvc.perform(createAccountRequest)
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HashMap.class);
    var newAccountId = response.get("id");

    // * Update existed account
    var request =
        patch("/accounts/" + newAccountId)
            .content(objectMapper.writeValueAsString(updateAccountDto))
            .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            jsonPath("$.firstName", is(updateAccountDto.getFirstName())),
            jsonPath("$.lastName", is(updateAccountDto.getLastName())),
            jsonPath("$.email", is(updateAccountDto.getEmail())));
  }
}
