package com.office.library.admin.member;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service //AdminMemberController에서 @Autowired로 받아 쓸것임.
public class AdminMemberService {
	
	
	final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;
	final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;
	final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1; //DB에 Insert 실패
	
	
	@Autowired
	AdminMemberDao adminMemberDao; //db와 통신하는 dao의 객제를 받아와야 수행할 수 있음
	
	@Autowired
	JavaMailSenderImpl javaMailSenderImpl;
	
	
	public int createAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] createAccountConfirm()");
		
		boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());
		
		if (!isMember) {
			int result = adminMemberDao.insertAdminAccount(adminMemberVo);
			
			if (result > 0)
				return ADMIN_ACCOUNT_CREATE_SUCCESS;
			else 
				return ADMIN_ACCOUNT_CREATE_FAIL;
		} else {
			return ADMIN_ACCOUNT_ALREADY_EXIST;
		}

	}
	
	
	public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("AdminMemberService] loginConfirm()");
		
		AdminMemberVo loginedAdminMemberVo =
				adminMemberDao.selectAdmin(adminMemberVo);
		
		//정상이라면 loginedAdminMemberVo에 회원정보가 담김
		if (loginedAdminMemberVo != null)
			System.out.println("[AdminMemberService] Admin Member Login SUCCESS !!");
		else
			System.out.println("[AdminMemberService] Admin Member Login FAIL !!");
		
		return loginedAdminMemberVo;
	}
	
	
	//관리자 목록 가져와줌
	public List<AdminMemberVo> listupAdmin() {
		System.out.println("[AdminMemberService] listupAdmin()");
		
		return adminMemberDao.selectAdmins();
	}
	
	
	//관리자가 승인하면 db의 a_m_approval을 0에서 1로 바꿈
	public void setAdminApproval(int a_m_no) {
		System.out.println("[AdminMemberService] setAdminApproval()");
		
		int result = adminMemberDao.updateAdminAccount(a_m_no);
		
	}
	
	
	//관리자 정보 업데이트
	public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] modifyAccountConfirm");
		
		return adminMemberDao.updateAdminAccount(adminMemberVo);
	}
	
	
	//가장 최근의 관리자 정보 가져오기
	public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
		System.out.println("[AdminMemberService] getLoginedAdminMemberVo()");
		
		return adminMemberDao.selectAdmin(a_m_no);
	}
	
	
	//새로운 비밀번호 생성
	public int findPasswordConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberService] findPasswordConfirm()");
		
		AdminMemberVo selectedAdminMemberVo = 
				adminMemberDao.selectAdmin(adminMemberVo.getA_m_id(), //관리자 인증
				adminMemberVo.getA_m_name(), adminMemberVo.getA_m_mail());
		
		int result=0;
		
		if (selectedAdminMemberVo !=null) {
			String newPassword = createNewPassword(); //난수 이용해 새로운 비번 생성 (아래 코드에 있음)
			result = adminMemberDao.updatePassword(adminMemberVo.getA_m_id(), newPassword); //새 비번 업데이트
			
			if (result > 0)
				sendNewPasswordByMail(adminMemberVo.getA_m_mail(), newPassword); //관리자 메일로 새 비번 발송 (아래 코드 있음)
			
		}
		return result;
	}
	
	
	//난수를 이용해서 새로운 비밀번호 생성 (교재 p.430 참고)
	private String createNewPassword() {
		System.out.println("[AdminMemberService] createNewPassword()");
		
		char[] chars = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
				'u', 'v', 'w', 'x', 'y', 'z' };
		
		StringBuffer stringBuffer = new StringBuffer();
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.setSeed(new Date().getTime());
		
		int index = 0;
		int length = chars.length;
		for (int i = 0; i < 8; i++) { //난수 8번 생성
			index = secureRandom.nextInt(length); //난수 8개를 index에 저장
			
			if (index % 2 == 0)
				stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
			else
				stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
		}
		
		System.out.println("[AdminMemberService] NEW PASSWORD:" + stringBuffer.toString());
		
			return stringBuffer.toString();
				
		}
	
	//관리자 메일로 새 비밀번호 발송
	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		System.out.println("[AdminMemberService] sendNewPasswordByMail");
		
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo(toMailAddr); //받는 메일 주소 -> toMailAddr 로 해놔야 함 평소에는!! OR 특정 메일
				mimeMessageHelper.setSubject("[한국도서관] 새 비밀번호 안내입니다."); //제목
				mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true); //내용
			}
		};
		javaMailSenderImpl.send(mimeMessagePreparator);
}
	
}
















