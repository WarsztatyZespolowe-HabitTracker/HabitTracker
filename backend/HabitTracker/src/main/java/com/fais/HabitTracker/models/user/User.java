package com.fais.HabitTracker.models.user;


import com.fais.HabitTracker.enums.Role;
import io.micrometer.common.util.StringUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private Role role = Role.USER;
    private boolean active = true;
    private Instant createdAt = Instant.now();
    private Instant deletedAt;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public static User createNewUser(String username, String encodedPassword) {
        User user = new User();
        user.username = username;
        user.password = encodedPassword;
        return user;
    }

    public void updateUserDetails(String username, String encodedPassword) {
        if (StringUtils.isNotBlank(username)) {
            this.username = username;
        }

        if (StringUtils.isNotBlank(encodedPassword)) {
            this.password = encodedPassword;
        }
    }

    public void deactivate() {
        this.active = false;
        this.deletedAt = Instant.now();
    }
}
