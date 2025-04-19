package com.ducbao.service_be.config.security;

import com.ducbao.service_be.model.entity.UserModel;
import com.ducbao.service_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username).get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.addAll(userModel.getAuthorities());
        User user = new User(userModel.getUsername(), userModel.getPassword(), grantedAuthorities);
        return user;
    }

    public UserDetails loadUserById(String id) {
        UserModel userModel = userRepository.findById(id).get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.addAll(userModel.getAuthorities());
        if(userModel.getPassword()== null){
            UserDetailsCustoms userDetailsCustoms = new UserDetailsCustoms(
                    userModel.getUsername(), grantedAuthorities
            );
            return userDetailsCustoms;
        }
        User user = new User(userModel.getUsername(), userModel.getPassword(), grantedAuthorities);
        return user;
    }
}
