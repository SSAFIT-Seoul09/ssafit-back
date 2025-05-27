package com.ssafy.ssafit.usageTimeTracker;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.UserContext;
import com.ssafy.ssafit.usageTimeTracker.domain.model.UsageTimeTracker;
import com.ssafy.ssafit.usageTimeTracker.domain.repository.UsageTimeTrackerDao;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j(topic = "UsageTimeTrackerAOP")
@Aspect
@Component
@RequiredArgsConstructor
public class UsageTimeTrackerAOP {

    private final UserDao userDao;
    private final UsageTimeTrackerDao usageTimeTrackerDao;

    @Around("execution(* com.ssafy.ssafit..controller..*(..))")
    public Object trackUsageTime(ProceedingJoinPoint joinPoint) throws Throwable {

        // 요청 시작 시간 기록
        long startTime = System.currentTimeMillis();

        try {
            // 핵심기능 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료시간 - 시작 시간
            long runTime = endTime - startTime;

            // 로그인한 회원의 정보 가져오기.
            AuthenticatedUser auth = UserContext.getUser();
            // 만약 로그인 했다면 수행시간 기록
            if (auth != null) {
                // 존재하는 회원인지 검증
                User user = userDao.findUserById(auth.getUserId());
                if (user == null) {
                    throw UserNotFoundException.ofUserId(auth.getUserId());
                }

                // API 사용시간 및 DB에 기록
                UsageTimeTracker usageTimeTracker = usageTimeTrackerDao.findByUserId(user.getId());
                if (usageTimeTracker == null) {
                    // 로그인 된 유저의 기록이 없으면(첫 요청일 경우)
                    usageTimeTracker = UsageTimeTracker.of(user.getId(), runTime);
                    usageTimeTrackerDao.insertTime(usageTimeTracker);
                } else {
//                    usageTimeTracker.addRunTime(runTime);
                    usageTimeTrackerDao.updateTime(user.getId(), runTime);
                }

                log.info("이번 요청 runTime : {} ms", runTime);
                log.info("[API Use Time] Username: " + user.getNickname() + ", Total Time: " + usageTimeTracker.getTotalTime() + " ms");
            }
        }
    }
}
