package com.ray.dormitory.util.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author : Ray
 * @date : 2019.12.03 16:04
 */
//@Aspect
//@Component
//@EnableAspectJAutoProxy
public class ResponseAspect {
    @Pointcut("execution(* com.ray.dormitory.controller.*.*(..))")
    public void cut() {

    }

    @AfterReturning("cut()")
    public void afterReturn() {
        System.out.println("response over");
    }
}
