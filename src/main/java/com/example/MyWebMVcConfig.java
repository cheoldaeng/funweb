package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.MemberLoginCheckInterceptor;
import com.example.interceptor.MemberStayLoggedInInterceptor;

@Configuration
public class MyWebMVcConfig implements WebMvcConfigurer{
	
	@Autowired
	private MemberLoginCheckInterceptor memberLoginCheckInterceptor;
	@Autowired
	private MemberStayLoggedInInterceptor memberStayLoggedInInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		InterceptorRegistration reg = registry.addInterceptor(memberLoginCheckInterceptor);
		reg.addPathPatterns("/board/file*")
		.excludePathPatterns("/board/fileList"); //제외될 부분이 있을 때 사용 exclude
		
		registry.addInterceptor(memberStayLoggedInInterceptor)
		.addPathPatterns("/*"); //쿠키는 항시 체크해야하니 *
		//.excludePathPatterns("/css/**", "/fonts/**", "/images/**", "/script/**");
		
		
	}
}
