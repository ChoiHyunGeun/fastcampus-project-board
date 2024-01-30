package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.dto.security.KakaoOAuth2Response;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import com.fastcampus.projectboard.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.nio.file.attribute.UserPrincipal;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Autowired
    private HandlerMappingIntrospector handlerMappingIntrospector;

    /**
     * 스프링 시큐리티의 관리 하에 두고 인증과 권한 체크를 하는 부분
     * @param http
     * @return
     * @throws Exception
     */
    @Bean //TODO : 최초로 pc 켜고, 서버를 실행 시키고, 로그인을 하면 error 화면이 나옴. 딱 이 조건일 때만 최초로 발생함. 확인하고 수정하기
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    //.anyRequest().permitAll()
                    //.antMatchers(), mvcMatchers() > 강의에선 이걸 사용했지만 버전업이 됨에 따라 requestMatchers()를 사용
                    //순서도 중요한 것 같음. 가장 먼저 제외하고 싶은건 정적 리소스니까 가장 먼저 제외시켜주기
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/")).permitAll()
                    .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/articles")).permitAll()
                    .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector, "/articles/search-hashtag")).permitAll()
                    //.permitAll() // > 위에 정의한 경로는 누구나 들어올 수 있게 설정한거임
                    .anyRequest().authenticated() // > 위에서 정의한 경로, Request 말고는 모두 권한 체크가 필요함
                ).formLogin(withDefaults()) //아무 일도 안하는 기본 값으로 동작하길 원한다면 withDefaults()를 사용
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(oAuth -> oAuth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)))
                .build();
    }

    /**
     * 스프링 시큐리티 검사에서 제외한다
     * ex) 정적 리소스 css, html, js 등
     *
     * 주석처리 한 이유
     * 이렇게 정적 리소스에 대하여 ignore해버리면 CSRF같은 보안 공격 기능이나 방어 기능, 다른 어떤 보안 공격에 대해서
     * 취약해질 수 밖에 없음. 그래서 이렇게 아예 검사에서 제외시키지는 말고, 위에 선언된 securityFilterChain에 해당 내용을
     * 그대로 넣어줘서 Spring security 관리 하에 리소스들이 관리될 수 있도록 구현하는 것을 추천한다고 함.
     * 여기서 배운점은 서버 구동될 때 warning 로그가 보이면 한번씩 읽어보는 것도 좋은 것 같음.
     * 정말 자세하게 어떤 방법을 추천해주는지 나오니까 로그 보는 습관 들이기
     * @return
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        /*
//            검사에서 제외될 리스트는 PathRequest.toStaticResources().atCommonLocations() 이런식으로 스프링에서 제공을 해주는데
//            atCommonLocations를 타고 StaticResourceLocation 클래스를 보면 제외시킬 정적 리소스의 경로가 정의되어 있음.
//            이런건 어떻게 알고 사용하는지 공부해봐야 할 것 같음
//         */
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    /**
     * 인증 정보, 사용자 정보를 가져온다.
     * UserDetailsService 인터페이스를 통해서 실제 인증 데이터를 가져오는 서비스 로직 구현
     * @param userAccountService
     * @return UserDetailsService의 추상 메서드인 loadUserByUsername가 구현된 UserDetailsService 객체의 인스턴스를 리턴
     */
    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService){
        /**
         * 여기서 return문 안에 정의된 내용들은
         * UserDetailsService 인터페이스에 단 하나의 추상 메서드만 있기 때문에 가능한 형식이며
         * 단 하나의 추상 메서드인 loadUserByUsername가 구현된 것이다.
         *
         * return문에서 username은 loadUserByUsername(String username) <- 여기서 받은 변수이며
         * 변수로 받은 username을 통해 userAccountRepository로 사용자 정보를 조회하는 내용임.
         */
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username : " + username));
    }

    /**
     * 카카오 인증을 위해 추가되는 함수인데
     * 왜 이 인터페이스가 사용되고 어떻게 구현되는지 이해가 안됨
     * 어디를 참고해서 개발을 해야할 지 전혀 모르겠음.
     * TODO : 반드시 설정하는 과정 혼자서 다시 한번 구현해보기
     * @return
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());

            /**
             * application.yaml에서 정의한
             *   security:
             *     oauth2:
             *       client:
             *         registration:
             *           kakao: <- 이게 registrationId임
             */
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}dummy" + UUID.randomUUID());


            /**
             * user정보를 조회하고 있으면 그대로 mapping하여 진행
             * 정보가 없으면 회원가입 진행
             */
            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() ->
                        BoardPrincipal.from(
                                userAccountService.saveUser(
                                        username,
                                        dummyPassword,
                                        kakaoResponse.email(),
                                        kakaoResponse.nickname(),
                                        null
                                )
                        )
                    );
        };
    }

    /**
     * 사용자의 평문 비밀번호를 안전하게 해싱하여 저장함.
     * 인증 과정에서 사용자가 입력한 비밀번호를 저장된 해시와 비교
     * 해시 알고리즘을 사용하여 비밀번호를 보호함으로써, 데이터베이스가 노출되더라도 비밀번호가 쉽게 노출되지 않음
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
