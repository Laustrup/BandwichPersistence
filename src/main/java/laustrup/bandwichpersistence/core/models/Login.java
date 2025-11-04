package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.users.User;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldNameConstants
public class Login implements UserDetails {

  private final String username;

  private final String password;

  private User<? extends User.Id> user;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public Login(
      @JsonProperty String username,
      @JsonProperty String password
  ) {
    this.username = username;
    this.password = password;
  }

  @Override
  //TODO Create service to get authorities
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  public User<? extends User.Id> setUser(User<? extends User.Id> user) {
    if (user != null && user.get_contactInfo() != null && user.get_contactInfo().get_email().equals(username))
      this.user = user;

    return this.user;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
