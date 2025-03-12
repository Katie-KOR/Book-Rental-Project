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
	
	final static public int BOOK_ISBN_ALREADY_EXIST = 0; //�̹� ��ϵ� ����
	final static public int BOOK_REGISTER_SUCCESS = 1; //�ű� ���� ��� ����
	final static public int BOOK_REGISTER_FAIL = -1; //�ű� ���� ��� ����

	//DB�� �ű� ���� �߰�
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
	
	//���� �˻� ���
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBookBySearch(bookVo);
	}
	
	//���� �� ����
	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] bookDetail()");
		
		return bookDao.selectBook(b_no);
	}
	
	//���� ���� ȭ�� �ҷ�����
	public BookVo modifyBookForm(int b_no) {
		System.out.println("[BookService] modifyBookForm()");
		
		return bookDao.selectBook(b_no);
	}
	
	//���� �����ϱ�
	public int modifyBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] modifyBookConfirm()");
		
		return bookDao.updateBook(bookVo);
	}
	
	//���� �����ϱ�
	public int deleteBookConfirm(int b_no) {
		System.out.println("[BookService] deleteBookConfirm()");
		
		return bookDao.deleteBook(b_no);
	}
	
	//���� ���� ��� ��ȸ
	public List<RentalBookVo> getRentalBooks() {
		System.out.println("[BookService] getRentalBooks()");
		
		return bookDao.selectRentalBooks();
	}
	
	
	//���� �ݳ� Ȯ��
	public int returnBookConfirm(int b_no, int rb_no) {
		System.out.println("[BookService] returnBookConfirm()");
		
		int result = bookDao.updateRentalBook(rb_no); //���⵵�� �ݳ�ó��
		
		if (result > 0)
			result = bookDao.updateBook(b_no); //���� ���¸� ���� �������� ����
		
		return result;
	}
	
	//������� ��� ��ȸ�ϱ�
	public List<HopeBookVo> getHopeBooks() {
		System.out.println("[BookService] getHopeBooks()");
		
		return bookDao.selectHopeBooks();
	}
	
	
	//������� �԰� ó�� Ȯ��
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
	
	
	
	//��ü ���� ��� ����
	public List<BookVo> getAllBooks() {
		System.out.println("[BookService] getAllBooks()");
		
		return bookDao.selectAllBooks();
	}
}





















