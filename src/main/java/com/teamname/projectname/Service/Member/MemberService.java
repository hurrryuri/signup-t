package com.teamname.projectname.Service.Member;

import com.teamname.projectname.Constant.Level;
import com.teamname.projectname.DTO.Member.LoginDTO;
import com.teamname.projectname.DTO.Member.MemberDTO;
import com.teamname.projectname.Entity.Member.MemberEntity;
import com.teamname.projectname.Repository.Member.MemberRepository;
import com.teamname.projectname.Service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.lang.reflect.Member;
import java.security.SecureRandom;
import java.util.Optional;

//Gmail에서 이메일 전송하도록 설정
// 이메일 비밀키 발급
//임시 비밀번호를 이메일로 발송

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService { //사용자가 로그인정보 변경
    private final MemberRepository memberRepository; //SQL처리
    private final ModelMapper modelMapper; //변환
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화
    private final EmailService emailService; // 이메일로 임시번호 발급
    //private EmailService emailservice = new emailservice


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException { //입력받은 아이디가 없으면 예외처리
        //사용자 아이디로 조회해서
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);

        if (memberEntity.isPresent()) { //아이디가 존재하면
            LoginDTO loginDTO = modelMapper.map(memberEntity, LoginDTO.class);

            return loginDTO;

        } else { //존재하지 않으면 예외처리
            throw new UsernameNotFoundException(userid);


        }

        //조회한 결과를 보안에 전달하면 보안에 인증확인
    }

    //회원가입
    public void saveMember(MemberDTO memberDTO) {
        try { // 서버가 멈추는 것을 예방하기 위해서 try~catch문 사용
            Long totalCount = memberRepository.count(); // 저장된 회원수를 읽어온다.
            Optional<MemberEntity> read = memberRepository.findByUserid(memberDTO.getUserid()); //아이디 조회(아이디 중복)

            if(read.isPresent()) { //이미 가입된 아이디이면
                throw new IllegalStateException("이미 가입된 회원입니다.");
            }

            String password = passwordEncoder.encode(memberDTO.getPassword()); //비밀번호 암호화처리
            MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class); //DTO->Entity변환
            memberEntity.setPassword(password); //암호화한 비밀번호를 다시 저장

            if(totalCount < 1) { //가입한 회원이 존재하지 않으면
                memberEntity.setUserlevel(Level.ADMIN); //첫번째 회원을 관리자로 등록
            }else { //두번째 회원 가입자 부터는 사용자로 등록
                memberEntity.setUserlevel(Level.USER);
            }
            memberRepository.save(memberEntity); //저장
        } catch(IllegalStateException e) { //상태오류(데이터베이스 처리 실패시)
            //e.getmessage()는 오류메시지
            System.out.println("회원 가입을 실패하였습니다." + e.getMessage());
            throw e; //호출한 곳으로 돌아간다.(return)
        } catch (Exception e) { //비정상적인 처리(오류발생)
            System.out.println("예기치 않은 문제가 발생하였습니다." + e.getMessage());
            throw new RuntimeException("가입 중 오류가 발생하였습니다."); // 사용자가 오류처리
        }
    }

    //개별조회(회원수정시 -> 회원정보 읽기)-관리자가 회원정보를 수정
    public MemberDTO readMember1(String userId) {
        try { // 서버가 멈추는 것을 예방하기 위해서 try~catch문 사용
            Optional<MemberEntity> read = memberRepository.findByUserid(userId);
            if(read.isPresent()) { //검색한 회원이 존재하면
                MemberDTO memberDTO = modelMapper.map(read, MemberDTO.class);
            }
            return null;
        } catch(IllegalStateException e) { //상태오류(데이터베이스 처리 실패시)
            //e.getmessage()는 오류메시지
            System.out.println("회원 조회를 실패하였습니다." + e.getMessage());
            throw e; //호출한 곳으로 돌아간다.(return)
        } catch (Exception e) { //비정상적인 처리(오류발생)
            System.out.println("예기치 않은 문제가 발생하였습니다." + e.getMessage());
            throw new RuntimeException("조회 중 오류가 발생하였습니다."); // 사용자가 오류처리
        }
    }
    //회원정보 수정
    //수정(비밀번호 입력, 이름, 전화번호, 주소, 새비밀번호)
    public void modifyMember(MemberDTO memberDTO) {
        try { // 서버가 멈추는 것을 예방하기 위해서 try~catch문 사용
            Optional<MemberEntity> read = memberRepository.findByUserid(memberDTO.getUserid()); //존재여부 검색
            /* if(read.isPresent()) {//회원이 존재하면 수정
                //비밀번호 확인을 통해 2차 검증
                String password = passwordEncoder.encode(memberDTO.getPassword());
                if(passwordEncoder.matches(password, read.get().getPassword())){ //비밀번호가 일치하면
                    MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
                    memberEntity.setPassword(password);
                    memberRepository.save(memberEntity);
                }
            }else { //존재하지 않으면 오류처리
                throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
            } */
            //if문이 많으면 적은부분으로 코딩한다.
            if(!read.isPresent()) { //회원이 존재하면 수정
                throw new IllegalStateException("일치하는 회원이 없습니다.");
            }
            String password = passwordEncoder.encode(memberDTO.getPassword());
            if(!passwordEncoder.matches(password, read.get().getPassword())) {
                throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
            }
            MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
            memberEntity.setPassword(password);
            memberRepository.save(memberEntity);

        } catch (IllegalStateException e) { //상태오류(데이터베이스 처리 실패시)
            //e.getmessage()는 오류메시지
            System.out.println("회원 수정을 실패하였습니다." + e.getMessage());
            throw e; //호출한 곳으로 돌아간다.(return)
        } catch (Exception e) { //비정상적인 처리(오류발생)
            System.out.println("예기치 않은 문제가 발생하였습니다." + e.getMessage());
            throw new RuntimeException("수정 중 오류가 발생하였습니다."); // 사용자가 오류처리
        }
    }

    //임시 비밀번호 발급(아이디, 회원명, 전화번호를 입력받아서 일치하면 해당 메일(아이디)로 임시비밀번호를 전송)
    public void passwordSend(MemberDTO memberDTO) {
        try { // 서버가 멈추는 것을 예방하기 위해서 try~catch문 사용
            Optional<MemberEntity> read = memberRepository.findByUserid(memberDTO.getUserid()); //조회
            if(!read.isPresent()){ //일치하는 회원이 없으면
                throw new IllegalStateException("일치하는 회원이 존재하지 않습니다.");
            }
            if(!read.get().getUsername().equals(memberDTO.getUsername())) { //이름이 일치하지 않으면
                throw new IllegalStateException("회원이름이 일치하지 않습니다.");
            }
            if(!read.get().getUsertel().equals(memberDTO.getUsertel())) { //전화번호가 일치하지 않으면
                throw new IllegalStateException("전화번호가 일치하지 않습니다.");
            }
            String tempPassword = generateTempPassword(8); //임시비밀번호 생성
            read.get().setPassword(passwordEncoder.encode(tempPassword)); //임시 비밀번호를 저장
            memberRepository.save(read.get()); //데이터베이스에 저장

            //임시비밀번호를 회원 이메일(아이디)로 전달
            String emailSubject = "임시비밀번호 발급"; //메일제목
            String emailText = "안녕하세요"+read.get().getUsername()+"님.\n"+"요청하신 임시 비밀번호는 다음과 같습니다.\n"+
                    tempPassword+"\n"+"로그인 후 반드시 비밀번호를 변경해 주십시요."; //본문내용


            emailService.sendEmail(read.get().getUserid(), emailSubject, emailText); //메일전송
            System.out.println("전송완료");

        } catch(IllegalStateException e) { //상태오류(데이터베이스 처리 실패시)
            //e.getmessage()는 오류메시지
            System.out.println("회원 가입을 실패하였습니다." + e.getMessage());
            throw e; //호출한 곳으로 돌아간다.(return)
        } catch (Exception e) { //비정상적인 처리(오류발생)
            System.out.println("예기치 않은 문제가 발생하였습니다." + e.getMessage());
            throw new RuntimeException("가입 중 오류가 발생하였습니다."); // 사용자가 오류처리
        }
    }

    //비밀번호 생성기(입력한 자리수만큼 임시비밃번호를 생성)
    private String generateTempPassword(int length){
        //비밀번호에 사용할 문자열
        final String chars = "ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        try {
            for(int i = 0; i < length; i++) {
                int index = random.nextInt(chars.length()); //0~문자열 개수 사이의 정수형 난수 발생
                sb.append(chars.charAt(index)); //해당위치의 문자열을 추가
            }
            return sb.toString(); //생성한 비밀번호 전달
        } catch (Exception e) {
            System.err.println("임시 비밀번호 생성 실패!!"); //콘솔에 오류메세지 출력
            throw new RuntimeException("임시 비밀번호 생성을 실패하였습니다."); //호출한 메소드에 전달한 오류
        }
    }
}
