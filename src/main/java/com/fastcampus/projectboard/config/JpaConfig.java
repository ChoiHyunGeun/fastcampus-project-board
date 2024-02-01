package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        /*  설명
            인증 기능이 추가되면서 JpaConfig도 수정이 되어야 한다고 함.
            > 이유는 인증 기능이 추가되면서 사용자로부터 사용자 인증 정보를 받을 수 있게 되어서 수정이 되어야 한다고 말함.
            1. SecurityContextHolder 클래스는 인증 정보를 모두 가지고 있는 클래스
            2. SecurityContextHolder 클래스에서 getContext로 security context를 가져옴
            3. SecurityContext에는 Authentication 정보가 있고 그 정보를 getAuthentication로 가져옴
            4. filter로 인증 여부 확인(로그인 했는지 안했는지)
            5. 로그인 정보인 principal을 가져온다.
                5-1) Principal이라는 인터페이스가 있음
                5-2) Principal 인터페이스의 구현체가 바로 BoardPrincipal임
                     > 근데 BoardPrincipal은 Principal 인터페이스를 상속받지 않았던데 왜 구현체라고 호소하는하는건지 알아보기
                     > 4분 25초에는 UserDetail의 구현체라고 설명함 > 이게 맞는 것 같음
                     > 그래서 UserDetail의 구현체인 BoardPrincipal로 캐스팅을 하고 user 정보를 가져오는 순서인듯
                5-3) getPrincipal 안에는 어떤 형태의 인증 정보인지 모르는 인증 정보가 들어있고 그래서 Obejct로 리턴한다.
             6. 5-2에 작성한 내용
             7. user 정보 가져옴
         */
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(BoardPrincipal.class::cast)
                .map(BoardPrincipal::getUsername);
    }
}
