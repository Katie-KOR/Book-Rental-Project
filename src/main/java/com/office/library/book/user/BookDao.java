package com.office.library.book.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mariadb.jdbc.client.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;

@Component("user.BookDao")
public class BookDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	//도서 검색
	public List<BookVo> selectBookBySearch(BookVo bookVo) {
		System.out.println("[BookDao] selectBookBySearch");
		
		String sql = "SELECT * FROM tbl_book "
					+ "WHERE b_name LIKE ? " // =?을 쓰지 않고 like?를 쓴 이유는 이렇게 해야 그 ? 단어를 포함한 모든 결과물을 가져오기 때문임. =?는 정확한 그 키워드만 불러옴.
					+ "ORDER BY b_no DESC";
		
		List<BookVo> bookVos = null;
		
		try {
			bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
				
				@Override
				public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					BookVo bookVo = new BookVo();
					
					bookVo.setB_no(rs.getInt("b_no"));
					bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					bookVo.setB_name(rs.getString("b_name"));
					bookVo.setB_author(rs.getString("b_author"));
					bookVo.setB_publisher(rs.getString("b_publisher"));
					bookVo.setB_publish_year(rs.getString("b_publish_year"));
					bookVo.setB_isbn(rs.getString("b_isbn"));
					bookVo.setB_call_number(rs.getString("b_call_number"));
					bookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					bookVo.setB_reg_date(rs.getString("b_reg_date"));
					bookVo.setB_mod_date(rs.getString("b_mod_date"));
					
					return bookVo;
				}
			}, "%" + bookVo.getB_name() + "%");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookVos.size() > 0 ? bookVos : null;
	}
	
	//도서 상세보기 페이지를 위한 도서 검색
	public BookVo selectBook(int b_no) {
		System.out.println("[BookDao] selectBook()");
		
		String sql = "SELECT * FROM tbl_book "
				+ "WHERE b_no = ?"; 
	
	List<BookVo> bookVos = null;
	
	try {
		bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
			
			@Override
			public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				BookVo bookVo = new BookVo();
				
				bookVo.setB_no(rs.getInt("b_no"));
				bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
				bookVo.setB_name(rs.getString("b_name"));
				bookVo.setB_author(rs.getString("b_author"));
				bookVo.setB_publisher(rs.getString("b_publisher"));
				bookVo.setB_publish_year(rs.getString("b_publish_year"));
				bookVo.setB_isbn(rs.getString("b_isbn"));
				bookVo.setB_call_number(rs.getString("b_call_number"));
				bookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
				bookVo.setB_reg_date(rs.getString("b_reg_date"));
				bookVo.setB_mod_date(rs.getString("b_mod_date"));
				
				return bookVo;
			}
		}, b_no);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return bookVos.size() > 0 ? bookVos.get(0) : null;
	}
	
	
	//도서 대출하기
	public int insertRentalBook(int b_no, int u_m_no) {
		System.out.println("[BookDao] insertRentalBook()");
		
		String sql = "INSERT INTO tbl_rental_book(b_no, u_m_no, rb_start_date, rb_reg_date, rb_mod_date) "
					+ "VALUES(?,?, NOW(), NOW(), NOW())";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, b_no, u_m_no);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//도서 대출 가능 여부 업데이트
	public void updateRentalBookAble(int b_no) {
		System.out.println("[BookDao] updateRentalBookAble()");
		
		String sql = "UPDATE tbl_book "
					+ "SET b_rantal_able = 0 " //대출 불가능
					+ "WHERE b_no = ?";
		
		try {
			jdbcTemplate.update(sql, b_no);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//대출 중인 도서 검색
	public List<RentalBookVo> selectRentalBooks(int u_m_no) {
		System.out.println("[BookDao] selectRentalBooks()");
		
		String sql = "SELECT * FROM tbl_rental_book rb "
					+ "JOIN tbl_book b "
					+ "ON rb.b_no = b.b_no "
					+ "JOIN tbl_user_member um "
					+ "ON rb.u_m_no = um.u_m_no "
					+ "WHERE rb.u_m_no = ? AND rb.rb_end_date = '1000-01-01'";
		
		List<RentalBookVo> rentalBookVos = new ArrayList<RentalBookVo>();
		
		try {
			rentalBookVos = jdbcTemplate.query(sql, new RowMapper<RentalBookVo>() {
				@Override
				public RentalBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					RentalBookVo rentalBookVo = new RentalBookVo();
					
					rentalBookVo.setRb_no(rs.getInt("rb_no"));
					rentalBookVo.setB_no(rs.getInt("b_no"));
					rentalBookVo.setU_m_no(rs.getInt("u_m_no"));
					rentalBookVo.setRb_start_date(rs.getString("rb_start_date"));
					rentalBookVo.setRb_end_date(rs.getString("rb_end_date"));
					rentalBookVo.setRb_reg_date(rs.getString("rb_reg_date"));
					rentalBookVo.setRb_mod_date(rs.getString("rb_mod_date"));
					
					
					rentalBookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					rentalBookVo.setB_name(rs.getString("b_name"));
					rentalBookVo.setB_author(rs.getString("b_author"));
					rentalBookVo.setB_publisher(rs.getString("b_publisher"));
					rentalBookVo.setB_publish_year(rs.getString("b_publish_year"));
					rentalBookVo.setB_isbn(rs.getString("b_isbn"));
					rentalBookVo.setB_call_number(rs.getString("b_call_number"));
					rentalBookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					rentalBookVo.setB_reg_date(rs.getString("b_reg_date"));
					
					rentalBookVo.setU_m_id(rs.getString("u_m_id"));
					rentalBookVo.setU_m_pw(rs.getString("u_m_pw"));
					rentalBookVo.setU_m_name(rs.getString("u_m_name"));
					rentalBookVo.setU_m_gender(rs.getString("u_m_gender"));
					rentalBookVo.setU_m_mail(rs.getString("u_m_mail"));
					rentalBookVo.setU_m_phone(rs.getString("u_m_phone"));
					rentalBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					rentalBookVo.setU_m_reg_date(rs.getString("u_m_mod_date"));
					
					return rentalBookVo;
				}
			}, u_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rentalBookVos;
	}
	
	
	//도서 대출 이력 조회
	public List<RentalBookVo> selectRentalBookHistory(int u_m_no) {
		System.out.println("[BookDao] selectRentalBookHistory()");
		
		String sql = "SELECT * FROM tbl_rental_book rb "
					+"JOIN tbl_book b "
					+"ON rb.b_no = b.b_no "
					+"JOIN tbl_user_member um "
					+"ON rb.u_m_no = um.u_m_no "
					+"WHERE rb.u_m_no = ? "
					+"ORDER BY rb.rb_reg_date DESC"; //대출 최신 순
		
		List<RentalBookVo> rentalBookVos = new ArrayList<RentalBookVo>();
		
		try {
			rentalBookVos = jdbcTemplate.query(sql, new RowMapper<RentalBookVo>() {
				
				@Override
				public RentalBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
					
					RentalBookVo rentalBookVo = new RentalBookVo();
					
					
					rentalBookVo.setU_m_no(rs.getInt("u_m_no"));
					rentalBookVo.setRb_start_date(rs.getString("rb_start_date"));
					rentalBookVo.setRb_end_date(rs.getString("rb_end_date"));
					rentalBookVo.setRb_reg_date(rs.getString("rb_reg_date"));
					rentalBookVo.setRb_mod_date(rs.getString("rb_mod_date"));
					
					
					rentalBookVo.setB_thumbnail(rs.getString("b_thumbnail"));
					rentalBookVo.setB_name(rs.getString("b_name"));
					rentalBookVo.setB_author(rs.getString("b_author"));
					rentalBookVo.setB_publisher(rs.getString("b_publisher"));
					rentalBookVo.setB_publish_year(rs.getString("b_publish_year"));
					rentalBookVo.setB_isbn(rs.getString("b_isbn"));
					rentalBookVo.setB_call_number(rs.getString("b_call_number"));
					rentalBookVo.setB_rantal_able(rs.getInt("b_rantal_able"));
					rentalBookVo.setB_reg_date(rs.getString("b_reg_date"));
					
					rentalBookVo.setU_m_id(rs.getString("u_m_id"));
					rentalBookVo.setU_m_pw(rs.getString("u_m_pw"));
					rentalBookVo.setU_m_name(rs.getString("u_m_name"));
					rentalBookVo.setU_m_gender(rs.getString("u_m_gender"));
					rentalBookVo.setU_m_mail(rs.getString("u_m_mail"));
					rentalBookVo.setU_m_phone(rs.getString("u_m_phone"));
					rentalBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
					rentalBookVo.setU_m_reg_date(rs.getString("u_m_mod_date"));
					
					return rentalBookVo;
					
				}
				
				
			}, u_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rentalBookVos;
	}
	
	
	//희망도서 추가
	public int insertHopeBook(HopeBookVo hopeBookVo) {
		System.out.println("[BookDao] insertHopeBook()");
		
		String sql = "INSERT INTO tbl_hope_book(u_m_no, hb_name, hb_author, hb_publisher, "
					+"hb_publish_year, hb_reg_date, hb_mod_date, hb_result_last_date) "
					+"VALUES(?,?,?,?,?,NOW(), NOW(), NOW())";
		
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql, hopeBookVo.getU_m_no(), 
												hopeBookVo.getHb_name(),
												hopeBookVo.getHb_author(),
												hopeBookVo.getHb_publisher(),
												hopeBookVo.getHb_publish_year());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//희망도서 요청목록 조회
	public List<HopeBookVo> selectRequestHopeBooks(int u_m_no) {
		
		String sql = "SELECT * FROM tbl_hope_book WHERE u_m_no = ?";
		
		List<HopeBookVo> hopeBookVos = null;
		
		try {
			hopeBookVos = jdbcTemplate.query(sql, new RowMapper<HopeBookVo>() {
				
				@Override
				public HopeBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					HopeBookVo hopeBookVo = new HopeBookVo();
					
					hopeBookVo.setHb_no(rs.getInt("hb_no"));
					hopeBookVo.setU_m_no(rs.getInt("u_m_no"));
					hopeBookVo.setHb_name(rs.getString("hb_name"));
					hopeBookVo.setHb_author(rs.getString("hb_author"));
					hopeBookVo.setHb_publisher(rs.getString("hb_publisher"));
					hopeBookVo.setHb_publish_year(rs.getString("hb_publish_year"));
					hopeBookVo.setHb_reg_date(rs.getString("hb_reg_date"));
					hopeBookVo.setHb_mod_date(rs.getString("hb_mod_date"));
					hopeBookVo.setHb_result(rs.getInt("hb_result"));
					hopeBookVo.setHb_result_last_date(rs.getString("hb_result_last_date"));
				
				
					return hopeBookVo;
				}
			}, u_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hopeBookVos;
	}
}



















