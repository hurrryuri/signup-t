package com.teamname.projectname.Entity.Member;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.teamname.projectname.Constant.Level;
import lombok.*;
import jakarta.persistence.*;
import org.modelmapper.internal.bytebuddy.dynamic.loading.InjectionClassLoader;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //중복되지 않게
    private Integer pid;
    @Column(name="userid", length = 50, nullable = false, unique = true)
    private String userid; //사용자 아이디는 생략불가능, 중복 불가능
    @Column(name="password", nullable = false)
    private String password; //비밀번호
    @Column(name="username", length = 50, nullable = false)
    private String username; //사용자이름
    @Column(name="usertel", length = 30)
    private String usertel; //전화번호
    @Column(name="level", nullable = false)
    private Level userlevel; //사용자 등급




}
