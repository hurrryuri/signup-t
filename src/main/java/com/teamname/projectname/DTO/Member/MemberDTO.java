package com.teamname.projectname.DTO.Member;

import com.teamname.projectname.Constant.Level;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO{

    private Integer pid;
    private String userid; //사용자 아이디는 생략불가능, 중복 불가능
    private String password; //비밀번호
    private String username; //사용자이름
    private String usertel; //전화번호
    private Level userlevel; //사용자 등급

    //열거형 키(설명)
    private String userlevelDescription; //열거형 설명을 저장할 변수

    //해당키와 설명을 저장하는 사용자 함수를 선언
    public void setUserlevel(Level userlevel) {
        this.userlevel = userlevel; //키값 저장
        this.userlevelDescription =
                userlevel != null ? userlevel.getDescription() : null;
    }



}
