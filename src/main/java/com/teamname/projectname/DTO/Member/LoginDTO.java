package com.teamname.projectname.DTO.Member;

import com.teamname.projectname.Constant.Level;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO implements UserDetails {

    private Integer pid;
    private String userid; //사용자 아이디는 생략불가능, 중복 불가능
    private String password; //비밀번호
    private String username; //사용자이름 - 보안에서 아이디로 사용
    private String usertel; //전화번호
    private Level userlevel; //사용자 등급(ADMIN 열거값)

    //열거형 키(설명)
    private String userlevelDescription; //열거형 설명을 저장할 변수

    //해당키와 설명을 저장하는 사용자 함수를 선언
    public void setUserlevel(Level userlevel) {
        this.userlevel = userlevel; //키값 저장
        this.userlevelDescription =
                userlevel != null?userlevel.getDescription():null;
    }

    //사용자 이름을 출력하는 메소드
    public String getDisplayUsername(){
        return username;
    }

    //UserDetails를 사용자 커스텀으로 변경
    //사용자 아이디 오버라이딩
    @Override
    public String getUsername() {
        return userid;
    }

    //비밀번호 오버라이딩
    @Override
    public String getPassword() {
        return password;
    }

    //권한 오버라이딩
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userlevel != null) { //등급이 존재하면
            switch (userlevel) { // constant에 있는 내용으로
                case ADMIN:
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
                case OPERATOR:
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_OPERATOR"));
                case USER:
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
        return Collections.emptyList();
    }
    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정 차단 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //자격 증명 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
