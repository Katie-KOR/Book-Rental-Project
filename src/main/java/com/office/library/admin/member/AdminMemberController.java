package com.office.library.admin.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	@Autowired
	AdminMemberService adminMemberService;
	
	//회원가입
	@GetMapping("/createAccountForm") //아래 방식과 동일한데 간단한 버전. get메소드로 매핑할 때의 방법임.
		//@RequestMapping(value = "/createAccountForm", method=RequestMethod.GET)
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		String nextPage = "admin/member/create_account_form";
		
		return nextPage;
	}
	
	//회원가입 확인
	@PostMapping("/createAccountConfirm") //아래 방식과 동일한데 간단한 버전. post메소드로 매핑할 때의 방법임.
		//@RequestMapping(value = "/createAccountConfirm", method=RequestMethod.POST) //민감한 회원 정보가 담긴 데이터이므로 GET 말고 POST 명시
	public String creatAccountConfirm(AdminMemberVo adminMemberVo) { //AdminMemberVo에 있는 정보를 받아옴
		System.out.println("[AdminMemberController] creatAccountConfirm()");
		
		String nextPage = "/admin/member/create_account_ok";
		
		int result = adminMemberService.createAccountConfirm(adminMemberVo);
		
		if (result <= 0)
			nextPage = "admin/member/create_account_ng";
		
		return nextPage;
	}
	
	
	//로그인
	@GetMapping("/loginForm")
	public String loginform() {
		System.out.println("[AdminMemberController] loginForm()");
		
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	
	//로그인 확인
	//세션에 저장하는 기능 추가
	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = 
				adminMemberService.loginConfirm(adminMemberVo);
		
		if (loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		} else {
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60 * 30); //브라우저가 30분 동안 아무것도 하지 않으면 세션 종료
		}
		
		return nextPage;
		
	}
	
	
	//로그아웃 확인
	@RequestMapping(value = "/logoutConfirm", method = RequestMethod.GET)
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		
		String nextPage = "redirect:/admin";
		
		session.invalidate(); //세션 무효화
		
		return nextPage;
	}

	
	
	//관리자 목록
	//Model 은 서버의 데이터를 뷰에 전달하는 역할을 하는 인터페이스
	//AdminMemberService로부터 받은 모든 관리자 정보(adminMemberVos)를 리스트 타입으로 받았는데, 그걸 Model 안에 넣어서 클라이언트인 jsp에 전달함
	@RequestMapping(value = "/listupAdmin", method = RequestMethod.GET)
	public String listupAdmin(Model model) {
		System.out.println("[AdminMemberController] listupAdmin()");
		
		String nextPage = "/admin/member/listup_admins";
		
		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		
		model.addAttribute("adminMemberVos", adminMemberVos); //Model 안에 데이터 추가
		
		return nextPage;
	}
	
	
	//관리자 승인
	@RequestMapping(value = "/setAdminApproval", method = RequestMethod.GET)
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		System.out.println("[AdminMemberController] setAdminApproval()");
		
		String nextPage = "redirect:/admin/member/listupAdmin";
		
		adminMemberService.setAdminApproval(a_m_no);
		
		return nextPage;
	}
	
	
	
	//회원 정보 수정
	@GetMapping("/modifyAccountForm")
	public String modifyAccountForm(HttpSession session) {
		System.out.println("[AdminMemberController] modifyAccountForm()");
		
		String nextPage = "admin/member/modify_account_form";
		
		AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
		
		if (loginedAdminMemberVo == null)
			nextPage = "redirect:/admin/member/loginForm";
		
		return nextPage;
	}
	
	
	//회원 정보 수정 확인
	@PostMapping("/modifyAccountConfirm")
	public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "admin/member/modify_account_ok";
		
		int result = adminMemberService.modifyAccountConfirm(adminMemberVo); //관리자 정보 업데이트
		
		if (result > 0) {
			AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no()); //가장 최근의 관리자 정보 가져오기
			
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
			session.setMaxInactiveInterval(60 * 30);
		} else {
			nextPage = "admin/member/modify_account_ng";
					
		}
		
		return nextPage;
	}
	
	
	//비밀번호 찾기
	@GetMapping("/findPasswordForm")
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordForm()");
		
		String nextPage="admin/member/find_password_form";
		
		return nextPage;
	}
	
	//비밀번호 찾기 확인
	@PostMapping("/findPasswordConfirm")
	public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberController] findPasswordConfirm()");
		
		String nextPage="admin/member/find_password_ok";
		
		int result = adminMemberService.findPasswordConfirm(adminMemberVo);
		
		if (result <= 0)
			nextPage = "admin/member/find_password_ng";
		
		return nextPage;
	}
}













