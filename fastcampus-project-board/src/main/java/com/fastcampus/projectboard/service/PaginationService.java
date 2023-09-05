package com.fastcampus.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    private static final int BAR_LENGTH = 5;

    //1~5, 6~10 이런 식으로 5개의 페이지 씩 나눠지도록 구현
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int  totalPages) {
        int startNumber = currentPageNumber >= BAR_LENGTH ? (BAR_LENGTH * (currentPageNumber / BAR_LENGTH)) : 0;
        int endNumber = startNumber + Math.min(BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}

//(5 * (articles.number / 5))+4 > articles.totalPages-1 ? articles.totalPages-1 : (5 * (articles.number / 5))+4)}