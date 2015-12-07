package br.com.mwdesenvolvimento.mylibrary.server;

/**
 * Created by mindware on 25/03/15.
 */
public class JsonServerException extends Exception {
  public JsonServerException() {
    super("Server side problem");
  }
  public JsonServerException(String message) {
    super(message);
  }
}
