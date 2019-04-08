package com.capgemini.spring.configuration;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;
import  com.capgemini.bankapp.dao.BankAccountDao;
import  com.capgemini.bankapp.service.*;
import  com.capgemini.bankapp.dao.impl.BankAccountDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;
import java.sql.Connection;
import  com.capgemini.bankapp.util.DbUtil;

@Configuration
public class AnnotationConfiguration
{

@Bean
public BankAccountDao dao(){
BankAccountDao bank=new  BankAccountDaoImpl();
return bank;
	}

@Bean
public BankAccountServiceImpl bank(){

	BankAccountServiceImpl account=new BankAccountServiceImpl();
	account.setDaoProvider(dao());
	return account;
	}

@Bean
public Connection connect()
{

Connection connection=DbUtil.getConnection();
return connection;
}
}