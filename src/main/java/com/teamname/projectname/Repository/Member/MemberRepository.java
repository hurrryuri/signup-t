package com.teamname.projectname.Repository.Member;

import com.teamname.projectname.Entity.Member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    //개별조회는 Optional, 다중조회는 List, Page
    //아이디 조회(개별조회)
    Optional<MemberEntity> findByUserid(String userid);

    //아이디와 비밀번호 조회(개별조회)
    Optional<MemberEntity> findByUseridAndPassword(String userid, String password);

    //회원목록 조회(다중조회) - 입력받는 변수가 많은 경우 쿼리로 작성해서 변수의 수를 줄여서 사용
    @Query("SELECT u FROM MemberEntity u WHERE u.userid like %:keyword% or u.username like %:keyword%")
    Page<MemberEntity> search(String keyword, Pageable pageable);

}
