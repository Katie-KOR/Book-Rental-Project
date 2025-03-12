package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller //������ IOC �����̳ʿ� Bean ��ü�� ������
@RequestMapping("/admin") //�̷��� ��������ν� �⺻������ /admin �� ���� ��û�� �� ��Ʈ�ѷ��� ó���ϰ� ��
public class AdminHomeController {
	
	//""�� "/"�� ������ ��� -> "/admin"�� "/admin/" ��û�� ��� home()�� ó���� �� ����
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET) //method ��� ���ص� �Ȱ��� GET�� �⺻�̶�. 
	public String home() { //������ Ȩ�� �����ϱ� ���� URL�� ó���ϴ� �޼���. /admin ����
		System.out.println("[AdminHomeController] home()");
		
		String nextPage = "admin/home";
		
		return nextPage;
		//host:8090/library/admin�� url�� ��û�ϸ� nextPage�� admin/home�� �����.
		//�׷��� servlet-context.xml���� ��õ� �� ������ �ڵ����� admin/home.jsp�� ����ǰ� �׷��� ���� �������� home.jsp�� ��
	}
}
