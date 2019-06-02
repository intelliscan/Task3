package securestore.service;

import java.io.Serializable;

public class LoginData implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public LoginData() {
  }

  LoginData(String website, String name, String password) {
    this.website = website;
    this.name = name;
    this.password = password;
  }

  private String website;
  private String name;
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }
}