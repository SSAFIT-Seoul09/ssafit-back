package com.ssafy.ssafit.review.service;

import com.ssafy.ssafit.review.domain.ReviewDao;
import com.ssafy.ssafit.review.domain.repository.ReviewDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "ReviewServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl {

    private final com.ssafy.ssafit.review.domain.repository.ReviewDao reviewDao;
}
