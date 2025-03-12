package com.office.library.book.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;

@Service("admin.BookService")
public class BookService {
	@Autowired
	BookDao bookDao;
	
	final static public int BOOK_ISBN_ALREADY_EXIST = 0; //이미 등록된 도서
	final static public int BOOK_REGISTER_SUCCESS = 1; //신규 도서 등록 성공
	final static public int BOOK_REGISTER_FAIL = -1; //신규 도서 등록 실패

	//DB에 신규 도서 추가
	public int registerBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] registerBookConfirm()");
		
		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());
		
		if (!isISBN) {
			int result = bookDao.insertBook(bookVo);
			
			if (result > 0) 
				return BOOK_REGISTER_SUCCESS;
			else 
				return BOOK_REGISTER_FAIL;
			
		} else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}
	
	//도서 검색 기능
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBookBySearch(bookVo);
	}
	
	//도서 상세 보기
	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] bookDetail()");
		
		return bookDao.selectBook(b_no);
	}
	
	//도서 수정 화면 불러오기
	public BookVo modifyBookForm(int b_no) {
		System.out.println("[BookService] modifyBookForm()");
		
		return bookDao.selectBook(b_no);
	}
	
	//도서 수정하기
	public int modifyBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] modifyBookConfirm()");
		
		return bookDao.updateBook(bookVo);
	}
	
	//도서 삭제하기
	public int deleteBookConfirm(int b_no) {
		System.out.println("[BookService] deleteBookConfirm()");
		
		return bookDao.deleteBook(b_no);
	}
	
	//대출 도서 목록 조회
	public List<RentalBookVo> getRentalBooks() {
		System.out.println("[BookService] getRentalBooks()");
		
		return bookDao.selectRentalBooks();
	}
	
	
	//도서 반납 확인
	public int returnBookConfirm(int b_no, int rb_no) {
		System.out.println("[BookService] returnBookConfirm()");
		
		int result = bookDao.updateRentalBook(rb_no); //대출도서 반납처리
		
		if (result > 0)
			result = bookDao.updateBook(b_no); //도서 상태를 대출 가능으로 변경
		
		return result;
	}
	
	//희망도서 목록 조회하기
	public List<HopeBookVo> getHopeBooks() {
		System.out.println("[BookService] getHopeBooks()");
		
		return bookDao.selectHopeBooks();
	}
	
	
	//희망도서 입고 처리 확인
	public int registerHopeBookConfirm(BookVo bookVo, int hb_no) {
		System.out.println("[BookService] registerHopeBookConfirm()");
		
		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());
		
		if (!isISBN) {
			int result = bookDao.insertBook(bookVo);
			
			if (result > 0) {
				bookDao.updateHopeBookResult(hb_no);
				
				return BOOK_REGISTER_SUCCESS;
			} else {
				return BOOK_REGISTER_FAIL;
			}
		} else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}
	
	
	
	//전체 도서 목록 보기
	public List<BookVo> getAllBooks() {
		System.out.println("[BookService] getAllBooks()");
		
		return bookDao.selectAllBooks();
	}
}





















