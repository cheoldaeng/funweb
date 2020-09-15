package com.example;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;

public class test {
	
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/jspdb?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul";
	private static final String USER = "jspuser";
	private static final String PASSWORD = "jsppass";
	
	@Test
	public void testConnection() throws Exception{
		Class.forName(DRIVER);
		try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
			System.out.println(connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
