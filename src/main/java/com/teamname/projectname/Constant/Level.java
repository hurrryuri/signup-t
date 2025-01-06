package com.teamname.projectname.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    ADMIN("관리자"), OPERATOR("운영자"), USER("사용자");

    private final String description;
}

//ADMIN, OPERATOR, USER 데이터베이스에 저장되는 값
//"관리자", "운영자", "사용자"는 화면(HTML)에 출력할 내용
