package kr.notice.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.controller.Action;
import kr.notice.dao.NoticeDAO;
import kr.notice.vo.NoticeVO;
import kr.util.FileUtil;

public class DeleteNoticeAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//로그인 여부 체크
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num == null) { //로그인 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		int notice_num = Integer.parseInt(request.getParameter("notice_num"));
		NoticeDAO dao = NoticeDAO.getInstance();
		NoticeVO db_notice = dao.getNotice(notice_num);
		
		//로그인 한 회원번호와 작성자 회원번호 일치 여부 체크
		if(user_num != db_notice.getMem_num()) { //불일치
			return "/WEB-INF/views/common/notice.jsp";
		}
		//일치
		dao.deleteNotice(notice_num); //글 삭제
		//파일 삭제
		FileUtil.removeFile(request, db_notice.getNotice_filename());
		
		return "redirect:/notice/listNotice.do";
	}

}
