package com.midas.app.providers.payment.models;

import com.midas.app.models.Account;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CreateAccount {
  private String userId;
  private String firstName;
  private String lastName;
  private String email;

  public CreateAccount(Account account) {
    this.userId = String.valueOf(account.getId());
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.email = account.getEmail();
  }
}
