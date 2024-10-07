package com.ducbao.service_be.model.entity;

import com.ducbao.common.model.entity.UserBaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@SuperBuilder
@NoArgsConstructor
public class UserModel extends UserBaseModel implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
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
