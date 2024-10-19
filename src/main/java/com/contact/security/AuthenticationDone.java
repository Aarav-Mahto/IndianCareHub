package com.contact.security;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contact.entity.Register;

import com.contact.dao.DAOService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationDone implements AuthenticationSuccessHandler {

    @Autowired
    private DAOService daoService;

    Map<String, String> userCredential = Collections.emptyMap();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String userRole = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst().orElse("USER");
                            
        Register user_uid = daoService.findUserId(username);
        HttpSession session = request.getSession();
        if (user_uid != null) {
            // Set user information in session
            session.setAttribute("fullName", user_uid.getFullName());
            session.setAttribute("emailId", user_uid.getEmail_id());
            session.setAttribute("userType", user_uid.getUser_Type());
            setUserInfoSession(user_uid.getFullName(), user_uid.getEmail_id(), user_uid.getUser_Type());
        }
        else if("aaravmahto".equals(username)){
            session.setAttribute("fullName", "Aarav Mahto");
            session.setAttribute("emailId", "aaravmahto");
            session.setAttribute("userType", "ADMIN");
            setUserInfoSession("Aarav Mahto", "aaravmahto", "ADMIN");
        }
        else {
            // session.setAttribute("username", username);
            // session.setAttribute("role", userRole);
            // System.out.println("username"+username);
            // System.out.println("role"+userRole);
            //response.sendRedirect("/login?error=true");
            session.setAttribute("fullName", "Not Found");
            session.setAttribute("emailId", "Not Found");
            session.setAttribute("userType", "Not Found");
            setUserInfoSession("Error", "Error", "Error");
        }
        response.sendRedirect("/dashboard");
    }
    public void setUserInfoSession(String name, String email, String accessType) {
		userCredential =new HashMap<>();
		userCredential.put("name", name);
		userCredential.put("email", email);
		userCredential.put("accessType", accessType);
	}
	public Map<String, String> getUserInfoSession() {
		return userCredential;
	}
    
}
