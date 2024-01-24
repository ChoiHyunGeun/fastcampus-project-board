package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("viewController - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class) // > 매개변수 없이 선언하면 모든 컨트롤러를 가져옴. 그럴 필요 없이 테스트 대상이 되는 컨트롤러만 선언하여 Bean으로 읽어들이기
class ArticleControllerTest {
//    private final MockMvc mvc;
//
//    public ArticleControllerTest(@Autowired MockMvc mvc) {
//        this.mvc = mvc;
//    }
//
//    @DisplayName("view Get 게시판")
//    @Test
//    void boardView() throws Exception {
//        mvc.perform(get("/articles"))
//                .andExpect(status().isOk())
//                /**
//                 * text/html을 예상했지만 text/html charset=UTF-8이 리턴됨
//                 * contentType() -> contentTypeCompatibleWith()로 변경
//                 * 그러면 호환되는 타입까지 전부 맞다고 해줌
//                 */
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                /**
//                 * 화면의 이름을 체크하는 기능인 것 같음
//                 * 다시 한 번 확인하기
//                 */
//                .andExpect(view().name("articles/index"))
//                /**
//                 * 서버에서 데이터를 넘겨줄 때 화면으로 데이터가 넘어오는지 확인
//                 * articles 라는 이름으로 데이터가 넘어오는지 체크
//                 */
//                .andExpect(model().attributeExists("articles"));
//    }
//
//    @DisplayName("view Get 게시글 상세 화면")
//    @Test
//    void boardDetailView() throws Exception {
//        mvc.perform(get("/articles/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("articles/detail"))
//                /**
//                 * 서버에서 데이터를 넘겨줄 때 화면으로 데이터가 넘어오는지 확인
//                 * articles 라는 이름으로 데이터가 넘어오는지 체크
//                 */
//                .andExpect(model().attributeExists("article"))
//                .andExpect(model().attributeExists("articleComments"));
//    }
//
//    @Disabled("구현 중")
//    @DisplayName("view Get 게시글 검색 화면")
//    @Test
//    void boardSearchView() throws Exception {
//        mvc.perform(get("/articles/search"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("articles/search"));
//    }
//
//    @Disabled("구현 중")
//    @DisplayName("view Get 게시글 해시태그 화면")
//    @Test
//    void boardHashtagSearchView() throws Exception {
//        mvc.perform(get("/articles/search-hashtag"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("articles/search-hashtag"));
//    }
}