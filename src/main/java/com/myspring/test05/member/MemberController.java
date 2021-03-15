package com.myspring.test05.member;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 상시대기 상태 유지
@Controller
public class MemberController {

	// Autowired private 기본으로 써야해요!
	// private final MemberDAO
	// Singleton 과 비슷한 느낌(같진 않음!)
	// 제어의 역전에 위배되기에 private final 을 잘 사용하진 않음
	@Autowired
	private MemberDAO dao;
//	MemberDAO dao = new MemberDAO();
	
	@ModelAttribute("cp")
	public String getContextPath(HttpServletRequest request) {
//		String cp = request.getContextPath();
//		model.addAttribute("cp", cp);
		return request.getContextPath();
	}
	
	@RequestMapping(value = "/member/index")
	public String index() {
		System.out.println("==== index() ==== ");
		return "member/index";
	}
	
	@RequestMapping(value = "/member/userMenu")
	public String userMenu() {
		System.out.println("==== userMenu() ==== ");
		return "member/userMenu";
	}
	
	@RequestMapping(value = "/member/joinForm")
	public String joinForm() {
		System.out.println("==== joinForm() ==== ");
		return "member/joinForm";
	}
	
	@RequestMapping(value = "/member/joinPro", method = RequestMethod.POST)
	public String joinPro(Member member) {
		System.out.println("==== joinPro() ==== ");

		dao.memberJoin(member);
		return "redirect:/member/list";
	}

	@RequestMapping(value = "/member/list")
	public String list(Model model) {
		System.out.println("==== list() ==== ");
		ArrayList<Member> memberList = dao.getMemberList();
		
		model.addAttribute("memberList", memberList);
		
		return "member/list";
	}
	
	@RequestMapping(value = "/member/loginForm")
	public String loginForm(Member member, Model model) {
		System.out.println("==== loginForm() ==== ");
		
		return "member/loginForm";
	}
	
	@RequestMapping(value = "/member/loginPro", method = RequestMethod.POST)
	public String loginPro(Member member, Model model) {
		System.out.println("==== loginPro() ==== ");
		
		int check = dao.checkMember(member);
		
		model.addAttribute("id", member.getId());
		model.addAttribute("check", check);
		
		return "member/loginPro";
	}
	
	@RequestMapping(value = "/member/logout")
	public String logout(HttpServletRequest request, HttpSession session) {
		System.out.println("==== logout() ==== ");
		
		session.removeAttribute("log");
		
		return "/member/logoutPro";
	}
	
	@RequestMapping(value = "/member/modifyForm")
	public String modifyForm(HttpServletRequest request, Model model, HttpSession session) {
		
		System.out.println("==== modifyForm() ==== ");
		
		if(session.getAttribute("log") != null) {
			String id = (String)session.getAttribute("log");
			
			Member member = dao.getOneMember(id);
			
			model.addAttribute("member", member);
		}
		
		return "member/modifyForm";
	}
	
	@RequestMapping(value = "/member/modifyPro", method = RequestMethod.POST)
	public String modifyPro(Member member, HttpServletRequest request, HttpSession session) {
		System.out.println("==== modifyPro() ==== ");
		
		if(session.getAttribute("log") != null) {
			String id = (String)session.getAttribute("log");
			
			Member bean = dao.getOneMember(id);
			bean.setPw(member.getPw());
			bean.setEmail(member.getEmail());
			
			dao.updateMember(bean);
		}
		
		return "redirect:/member/userMenu";
	}
	
}
