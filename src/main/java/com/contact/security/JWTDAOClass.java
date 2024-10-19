package com.contact.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contact.dao.DAOService;
import com.contact.entity.Register;


@Service
public class JWTDAOClass implements UserDetailsService{

	
	
	@Autowired
	private DAOService daoService;
	
	private PasswordEncoder passwordEncoder;
	
    public JWTDAOClass() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("aaravmahto".equals(username)) {
			UserDetails userDetails = User.builder()
                    .username("aaravmahto")
                    .password(passwordEncoder.encode("@Sahuriya1"))
                    .roles("ADMIN")
                    .build();
			return userDetails;
        } 
		
		Register user_uid = daoService.findUserId(username);
		if(user_uid != null) {
			UserDetails userDetails = User.builder()
					.username(user_uid.getUnique_id())
					.password(user_uid.getPassword())
					.roles(user_uid.getUser_Type())
					.build();
			return userDetails;
		}
		else {
			throw new UsernameNotFoundException(username+" not Found!");
		}
	}
	
	
}
