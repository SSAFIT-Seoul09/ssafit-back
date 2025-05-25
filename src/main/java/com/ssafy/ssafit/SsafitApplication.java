package com.ssafy.ssafit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(
		basePackages = {"com.ssafy.ssafit.user.domain.repository"
				, "com.ssafy.ssafit.video.domain.repository"
				, "com.ssafy.ssafit.favorite.domain.repository"
				, "com.ssafy.ssafit.review.domain.repository"
				, "com.ssafy.ssafit.comment.domain.repository"
				, "com.ssafy.ssafit.usageTimeTracker.domain.repository"
		})
public class SsafitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsafitApplication.class, args);
	}

}
