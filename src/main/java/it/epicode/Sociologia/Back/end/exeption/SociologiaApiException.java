package it.epicode.Sociologia.Back.end.exeption;


import org.springframework.http.HttpStatus;

public class SociologiaApiException extends RuntimeException {
  private HttpStatus status;
  private String message;

  public SociologiaApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  public SociologiaApiException(String message, HttpStatus status, String message1) {
    super(message);
    this.status = status;
    this.message = message1;
  }

  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
