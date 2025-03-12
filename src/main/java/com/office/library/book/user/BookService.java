package com.office.library.book.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;

@Service("user.BookService")
public class BookService {

	
	@Autowired
	BookDao bookDao;
	
	
	//도서 검색 목록 가져오기
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBookBySearch(bookVo);
	}
	
	
	//도서 상세보기
	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBook(b_no);
	}
	
	
	//도서 대출하기
	public int rentalBookConfirm(int b_no, int u_m_no) {
		System.out.println("[BookService] rentalBookConfirm()");
		
		int result = bookDao.insertRentalBook(b_no, u_m_no);
		
		if (result >= 0)
			bookDao.updateRentalBookAble(b_no);
		
		return result;
	}
	
	//나의 책장 들어가기
	public List<RentalBookVo> enterBookshelf(int u_m_no) {
		System.out.println("[BookService] enterBookshelf()");
		
		return bookDao.selectRentalBooks(u_m_no);
	}
	
	
	//도서 대출 이력 조회
	public List<RentalBookVo> listupRentalBookHistory(int u_m_no) {
		System.out.println("[BookService] listupRentalBookHistory()");
		
		return bookDao.selectRentalBookHistory(u_m_no);
	}
	
	//희망 도서 신청하기
	public int requestHopeBookConfirm(HopeBookVo hopeBookVo) {
		System.out.println("[BookService] requestHopBookConfirm()");
		
		return bookDao.insertHopeBook(hopeBookVo);
	}
	
	//희망도서 요청목록 가져오기
	public List<HopeBookVo>listupRequestHopeBook(int u_m_no) {
		System.out.println("[BookService] listupRequestHopeBook()");
		
		return bookDao.selectRequestHopeBooks(u_m_no);
	}
}













