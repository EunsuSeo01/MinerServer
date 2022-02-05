package com.umc.miner.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

/* 페이징 처리를 위한 클래스 생성 */
public class Pagination {
    private boolean hasPrev;   // 이전 페이지 여부.
    private boolean hasNext;   // 다음 페이지 여부.
    private int currentPage;   // 현재 페이지 번호.
    private int startPage;  // 각 페이지 범위의 시작 번호.
    private int endPage;    // 각 페이지 범위의 끝 번호.
    private int totalMapNum;    // 전체 맵의 개수.
    private int totalPage;    // 전체 페이지 개수.
    private int dataPerPage;   // 페이지의 크기. 한 페이지에 보여질 맵의 개수.

    public Pagination(int pageNo, int pageSize) {
        this.currentPage = pageNo;
        this.dataPerPage = pageSize;
    }

    public void pageInfo(int totalMapNum) {
        this.totalMapNum = totalMapNum;

        if (totalPage != 0) {
            // 전체 페이지 수
            this.totalPage = (int)Math.ceil((double)totalMapNum / dataPerPage);
        } else {
            this.totalPage = 1;
        }

        // 시작 페이지
        this.startPage = 1;

        // 끝 페이지
        this.endPage = totalPage;

        // 이전 버튼 상태
        this.hasPrev = (currentPage <= startPage) ? false : true;

        // 다음 버튼 상태
        this.hasNext = (currentPage >= endPage) ? false : true;
    }
}
