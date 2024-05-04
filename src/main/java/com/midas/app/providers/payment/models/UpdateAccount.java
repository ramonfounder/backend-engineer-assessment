package com.midas.app.providers.payment.models;

import com.midas.app.models.Account;
import java.util.UUID;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UpdateAccount {
  private UUID userId;
  private String firstName;
  private String lastName;
  private String email;
  private String providerId;

  public UpdateAccount(Account account) {
    this.userId = account.getId();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.email = account.getEmail();
    this.providerId = account.getProviderId();
  }
}
