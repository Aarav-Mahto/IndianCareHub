package com.contact.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.contact.dao.DAOService;
import com.contact.dao.DAOServiceImplementation;
import com.contact.entity.Contact;
import com.contact.entity.QuestionAnchor;
import com.contact.entity.Register;
import com.contact.hepler.Helper;
import com.contact.hepler.HelperImplement;
import com.contact.security.AuthenticationDone;

@Configuration
public class DeclareBean {
	
	@Bean
	public Contact getContact() {
		return new Contact();
	}
	
	@Bean
	public QuestionAnchor getQuestionAnchor(){
		return new QuestionAnchor();
	}
	
	@Bean
	public Register getRegister() {
		return new Register();
	}
	
	@Bean
	public Helper getHelper() {
		return new HelperImplement();
	}
	
	@Bean
	public DAOService getDaoService() {
		return new DAOServiceImplementation();
	}
	
	@Bean
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public AuthenticationDone gAuthenticationDone(){
		return new AuthenticationDone();
	}
	
}
