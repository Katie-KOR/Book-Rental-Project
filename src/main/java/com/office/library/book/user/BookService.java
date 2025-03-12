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
	
	
	//���� �˻� ��� ��������
	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBookBySearch(bookVo);
	}
	
	
	//���� �󼼺���
	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] searchBookConfirm()");
		
		return bookDao.selectBook(b_no);
	}
	
	
	//���� �����ϱ�
	public int rentalBookConfirm(int b_no, int u_m_no) {
		System.out.println("[BookService] rentalBookConfirm()");
		
		int result = bookDao.insertRentalBook(b_no, u_m_no);
		
		if (result >= 0)
			bookDao.updateRentalBookAble(b_no);
		
		return result;
	}
	
	//���� å�� ����
	public List<RentalBookVo> enterBookshelf(int u_m_no) {
		System.out.println("[BookService] enterBookshelf()");
		
		return bookDao.selectRentalBooks(u_m_no);
	}
	
	
	//���� ���� �̷� ��ȸ
	public List<RentalBookVo> listupRentalBookHistory(int u_m_no) {
		System.out.println("[BookService] listupRentalBookHistory()");
		
		return bookDao.selectRentalBookHistory(u_m_no);
	}
	
	//��� ���� ��û�ϱ�
	public int requestHopeBookConfirm(HopeBookVo hopeBookVo) {
		System.out.println("[BookService] requestHopBookConfirm()");
		
		return bookDao.insertHopeBook(hopeBookVo);
	}
	
	//������� ��û��� ��������
	public List<HopeBookVo>listupRequestHopeBook(int u_m_no) {
		System.out.println("[BookService] listupRequestHopeBook()");
		
		return bookDao.selectRequestHopeBooks(u_m_no);
	}
}













