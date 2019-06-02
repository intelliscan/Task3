package securestore.service;

import java.io.IOException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PMUserDetailsService implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    securestore.service.UserDatabase database = null;
    try {
      database = new securestore.service.UserDatabase();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    if (!database.contains(username))
      return null;

    return User.withUsername(username).password(database.getPassword(username)).roles("USER").build();
  }
}
