package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller //스프링 IOC 컨테이너에 Bean 객체로 생성됨
@RequestMapping("/admin") //이렇게 명시함으로써 기본적으로 /admin 에 대한 요청을 이 컨트롤러가 처리하게 됨
public class AdminHomeController {
	
	//""와 "/"를 매핑한 결과 -> "/admin"과 "/admin/" 요청을 모두 home()이 처리할 수 있음
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET) //method 명시 안해도 똑같음 GET이 기본이라서. 
	public String home() { //관리자 홈에 접속하기 위해 URL을 처리하는 메서드. /admin 매핑
		System.out.println("[AdminHomeController] home()");
		
		String nextPage = "admin/home";
		
		return nextPage;
		//host:8090/library/admin인 url을 요청하면 nextPage인 admin/home이 연결됨.
		//그런데 servlet-context.xml에서 명시된 것 때문에 자동으로 admin/home.jsp가 연결되고 그렇게 응답 페이지인 home.jsp가 뜸
	}
}
