package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
    }

    public Set<String> parseHashtagNames(String content) {
        if (content == null) {
            return Set.of(); // 비어있는 Set 리턴
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");    //해시태그 정규식 검사
        Matcher matcher = pattern.matcher(content.strip());   //앞 뒤 공백 제거
        Set<String> result = new HashSet<>();

        while (matcher.find()) {
            result.add(matcher.group().replace("#", ""));
        }

        return Set.copyOf(result); // 수정 불가능한 Set을 리턴. 내부적으로 copy가 일어나는 듯
    }

    public void deleteHashtagWithoutArticles(Long hashtagId) {
        Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);
        /**
         * 1. ID로 해시태그 가져옴
         * 2. 해당 해시태그를 갖고 있는 게시글 리스트 가져옴.
         * 3. 비었다면 > 해당 해시태그를 갖고 있는 게시글이 없다면 삭제
         */
        if (hashtag.getArticles().isEmpty()) {
            hashtagRepository.delete(hashtag);
        }
    }
}
