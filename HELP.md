[보안로그인]
1. build.gradle에 보안 라이브러리를 추가
   implementation 'org.springframework.boot:spring-boot-starter-security'
   implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'

2. 보안 설정 후 서버를 실행하시면 임의 아이디와 비밀번호를 생성해서 제공
   아이디 : user, 비밀번호 : 콘솔에 임시 비밀번호

3. Config에 권한부여 설정(SecurityConfig.java)
   1) 비밀번호 암호화
      public PasswordEncoder passwordEncoder(){
   2) 맵핑 권한
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      초기 설정시 모두 사용을 허용해서 작업 진행하고, 중간에 제거
      auth.requestMatchers("/**").permitAll(); //모든 매핑 허용
      /** : 모든 매핑
      permitAll() : 모든 이용자
      authenticated() : 인증된 이용자(로그인한 사용자)
      hasRole() : 해당 권한자만 이용 가능(하나만 지정가능)
      hasAnyRole() : 지정된 권한자들만 이용가능(여러개 지정가능)
   3) 사용자 로그인 설정
      .defaultSuccessUrl() : 로그인 성공시 이동할 페이지
      .loginPage() : 사용자가 작성한 로그인 폼(맵핑명)
      .usernameParameter() : username으로 사용할 변수명(HTML)
      .successHandler() : 로그인 성공시 처리할 클래스
      ※ 로그인 성공 시 기본 아이디, 권한만 서버가 제공한다.
         사용자가 임의 내용을 제공하고자 할 때는 successHandler로 구현
   
   4) csrf 설정 : HTML의 변조방지
   5) 사용자 로그아웃 설정
      .logoutUrl() : 로그아웃 맵핑명 
      .logoutSuccessUrl() : 로그아웃 성공시 맵핑명

4. 사용자 핸들러를 통해 정보 수정
   1) 로그인 성공시 정보 수정
      public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
      @Override
      public void onAuthenticationSuccess(HttpServletRequest request,
               HttpServletResponse response,
               Authentication authentication)
      throws IOException, SecurityException {
      나중에 userdetails에서 제공할 정보를 수정할 필요가 있다.

5. Constant에 권한 등급 등록(열거형)
   키(설명), 키(설명), 키(설명);

6. Entity를 이용해서 테이블 설계

7. DTO를 작성 : Enum(열거형)의 키와 내용을 처리할 수 있도록 추가적인 변수 및 함수를 작성
   private String userlevelDescription; //열거형 설명을 저장할 변수

   public void setUserlevel(Level userlevel) {
      this.userlevel = userlevel; //키값 저장
      this.userlevelDescription =
       userlevel != null?userlevel.getDescription():null;
   } 차후에 상품분류, 고객 회원 등급 등에서 활용

8. Repository에서 사용자 아이디로 조회할 수 있는 쿼리를 작성
9. Service에 UserDetails를 상속받아서 사용자용 로그인처리 함수를 작성
10. DTO에 UserDetails를 상속 받아서 함수를 Override 처리하면 DTO내에 모든 변수도 동시에 전달이 가능
    (사용자 아이디, 비밀번호, 등급만 제공하는 기능을 사용자가 확장해서 사용이 가능)
11. Service에서 회원 삽입, 수정, 삭제 구현, (임시비밀번호 발급을 추가)
12. Controller 맵핑
13. HTML(로그인, 회원가입, 수정, 비밀번호 재발급, 탈퇴)
14. 각 메뉴 및 페이지에 권한 부여를 통해서 등급별 사용제한

라이브러리 : 기능 제공 -> 기능을 이용해서 응용프로그램을 개발
프레임워크=엔진
API : Application Interface, 응용프로그램 연동
      기존에 개발된 프로그램을 연동해서 새로운 프로그램으로 활용
      지도API, 우편번호API, 주식API