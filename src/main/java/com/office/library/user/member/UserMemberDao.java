package com.office.library.user.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component //스프링 IoC 컨테이너에 Bean으로 등록
public class UserMemberDao {

	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	//아이디 중복 확인
	public boolean isUserMember(String u_m_id) { 
		System.out.println("[UserMemberDao] isUserMember()");
		
		String sql = "SELECT COUNT(*) FROM tbl_user_member "
					+"WHERE u_m_id = ?";
		
		int result = jdbcTemplate.queryForObject(sql, Integer.class, u_m_id);
		
		return result > 0 ? true : false;
	}
	
	//회원가입 
	public int insertUserAccount(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberDao] inserUserAccount()");
		
		String sql = "INSERT INTO tbl_user_member(u_m_id, "
					+ "u_m_pw, " + "u_m_name, " + "u_m_gender, "
					 + "u_m_mail, " + "u_m_phone, " + "u_m_reg_date, "
					 + "u_m_mod_date) VALUES(?,?,?,?,?,?, NOW(), NOW())";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql,
					userMemberVo.getU_m_id(),
					passwordEncoder.encode(userMemberVo.getU_m_pw()),
					userMemberVo.getU_m_name(), userMemberVo.getU_m_gender(),
					userMemberVo.getU_m_mail(), userMemberVo.getU_m_phone());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	
	//회원 로그인
	public UserMemberVo selectUser(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberDao] selectUser()"); 
		
		String sql = "SELECT * FROM tbl_user_member "
					+"WHERE u_m_id = ?";
		
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		
		try {
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					
					return userMemberVo;
					
				}
			}, userMemberVo.getU_m_id());
			
			if (!passwordEncoder.matches(userMemberVo.getU_m_pw(), userMemberVos.get(0).getU_m_pw()))
				userMemberVos.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMemberVos.size() > 0 ? userMemberVos.get(0) :null;
	}
	
	
	//회원 정보 업데이트
	public int updateUserAccount(UserMemberVo userMemberVo) {
		System.out.println("[UserMemberDao] updateUserAccount()"); 
		
		String sql = "UPDATE tbl_user_member SET "
					 + "u_m_name = ?, " + "u_m_gender = ?, " + "u_m_mail = ?, "
					 + "u_m_phone = ?, " + "u_m_mod_date = NOW() "
					 + "WHERE u_m_no = ?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql,
					userMemberVo.getU_m_name(), userMemberVo.getU_m_gender(),
					userMemberVo.getU_m_mail(),userMemberVo.getU_m_phone(),
					userMemberVo.getU_m_no());
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//최근 로그인한 정보 불러오기
	public UserMemberVo selectUser(int u_m_no) {
		System.out.println("[UserMemberDao] selectUser()"); 
		
		String sql = "SELECT * FROM tbl_user_member "
					+ "WHERE u_m_no = ?";
		
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		
		try {
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_mod_date"));
					
					return userMemberVo;
					
				}
			}, u_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
	}
	
	//새 비밀번호 업데이트를 위해 입력 정보와 일치하는 회원을 조회
	public UserMemberVo selectUser(String u_m_id, String u_m_name, String u_m_mail) {
		System.out.println("[UserMemberDao] selectUser()"); 
		
		String sql = "SELECT * FROM tbl_user_member "
				+  "WHERE u_m_id = ? AND u_m_name = ? AND u_m_mail = ?";
		
		List<UserMemberVo> userMemberVos = new ArrayList<UserMemberVo>();
		
		try {
			userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
				
				@Override
				public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserMemberVo userMemberVo = new UserMemberVo();
					
					userMemberVo.setU_m_no(rs.getInt("u_m_no"));
					userMemberVo.setU_m_id(rs.getString("u_m_id"));
					userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
					userMemberVo.setU_m_name(rs.getString("u_m_name"));
					userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
					userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
					userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
					userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));
					
					return userMemberVo;
				}
			}, u_m_id, u_m_name, u_m_mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
	}
	
	//새 비밀번호 업데이트
	public int updatePassword(String u_m_id, String newPassword) {
		System.out.println("[UserMemberDao] updatePassword()");
		
		String sql = "UPDATE tbl_user_member SET "
					+ "u_m_pw = ?, u_m_mod_date = NOW() "
					+ "WHERE u_m_id = ?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), u_m_id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
					
	}
}


















