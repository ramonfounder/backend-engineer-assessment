package com.midas.app.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class PaymentProviderException extends ApiException {

  static final String MESSAGE = "Payment provider exception";

  public PaymentProviderException() {
    super(HttpStatus.FORBIDDEN, MESSAGE);
  }

  public PaymentProviderException(String message) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
  }

  public PaymentProviderException(String message, List<String> errors) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
    super.setErrors(errors);
  }

  public PaymentProviderException(String message, String error) {
    super(HttpStatus.FORBIDDEN, MESSAGE);

    super.setMessage(message);
    super.setErrors(error);
  }
}
