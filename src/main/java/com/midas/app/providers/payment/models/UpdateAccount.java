package com.midas.app.providers.payment.models;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UpdateAccount {
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String providerId;
}
