package com.my.worldwave.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecutionTimeChecker {

    @Pointcut("@annotation(com.my.worldwave.util.ExecutionTimeCheckPoint)")
    public void checkExecutionTime() {}

    @Around("checkExecutionTime()")
    public Object checkExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String targetName = signature.getDeclaringTypeName() + "." + signature.getName();
        log.info("Execution Time of [{}] : {} ms", targetName, totalTimeMillis);
        return proceed;
    }

}
