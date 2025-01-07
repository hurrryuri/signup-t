package com.teamname.projectname.Controller.Member;


import com.teamname.projectname.DTO.Member.MemberDTO;
import com.teamname.projectname.Service.Member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Member;

//회원관리
//로그인, 로그아웃, 회원가입, 회원수정, 비밀번호재발급
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService; //서비스 연동
    //추가 Util 연동

    //회원가입
    @GetMapping("/register")
    public String showRegisterPage() {
        return "member/register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute MemberDTO memberDTO){
        System.out.println("ddddddddddddddddddddd");
        System.out.println(memberDTO.getUserid());
        System.out.println(memberDTO.getUsername());
        memberService.saveMember(memberDTO);

        return "redirect:/login";
    }

    //회원수정
    @GetMapping("/modify")
    public String showModifyPage(){
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modifyMember(MemberDTO memberDTO){
        return "redirect:/logout";
    }

    //임시비밀번호 발급
    @GetMapping("/password")
    public String showPasswordPage() {
        return "member/password";
    }
    @PostMapping("/password")
    public String modifyPassword(@ModelAttribute MemberDTO memberDTO) {
        memberService.passwordSend(memberDTO);
        //스크립트로 출력할 메세지를 전달

        return "redirect:/login";
    }

    //로그인
    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login";
    }

    //로그아웃
    @GetMapping("/logout")
    public String showLogoutPage(HttpSession session){
        session.invalidate(); //해당 접속 컴퓨터의 섹션정보를 삭제
        return "redirect:/"; //메일 또는 로그인
    }
}
