package uk.avocado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

// A decorator to add Avocado functions to HttpServletRequest
public class AvocadoHttpServletRequest extends HttpServletRequestWrapper {

  private final HttpServletRequest servletRequest;

  public AvocadoHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.servletRequest = request;
  }

  public String getUsername() {
    return servletRequest.getHeader("AV-User");
  }
}
