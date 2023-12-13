package com.sparta.todoapp.global.aop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "로그")
@Aspect
@Component
public class LoggingAop {

    @Pointcut("execution(* com.sparta.todoapp.domain.*.controller..*.*(..))")
    private void allController() {
    }

    @Around("allController()")
    public Object loggingTest(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        log.info("요청 시각 = {}", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일(E) HH시 mm분 ss초 SSS (a)")));

        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("Join log = {}", joinPoint.getSignature());
            log.info("응답 시각 = {}", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일(E) HH시 mm분 ss초 SSS (a)")));
            log.info("걸린 시간 = {}ms", timeMs);
        }
    }
}
