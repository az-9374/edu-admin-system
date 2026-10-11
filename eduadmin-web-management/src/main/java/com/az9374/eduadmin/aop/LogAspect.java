package com.az9374.eduadmin.aop;

import com.az9374.eduadmin.mapper.OperateLogMapper;
import com.az9374.eduadmin.pojo.OperateLog;
import com.az9374.eduadmin.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * 环绕通知：记录操作日志
     * 切入点：com.az9374.eduadmin.controller 包下所有带有 @Log 注解的方法
     */
    @Around("@annotation(com.az9374.eduadmin.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 记录方法执行开始时间
        long begin = System.currentTimeMillis();

        // 2. 执行目标方法
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            // 3. 无论方法是否异常，都记录日志
            try {
                // 获取方法签名信息
                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getName();


                // 获取方法参数
                String methodParams = Arrays.toString(joinPoint.getArgs());

                // 获取返回值
                String returnValue = result.toString();

                // 计算执行耗时
                long costTime = System.currentTimeMillis() - begin;

                // 获取当前请求中的操作人ID（实际项目中从Session/Token中获取）
                Integer operateEmpId = getOperateEmpId();

                // 4. 封装日志对象
                OperateLog operateLog = new OperateLog();
                operateLog.setOperateEmpId(operateEmpId);
                operateLog.setOperateTime(LocalDateTime.now());
                operateLog.setClassName(className);
                operateLog.setMethodName(methodName);
                operateLog.setMethodParams(methodParams);
                operateLog.setReturnValue(returnValue);
                operateLog.setCostTime(costTime);

                // 5. 调用Mapper持久化日志
                operateLogMapper.insert(operateLog);
                log.info("操作日志记录成功: {}#{}", className, methodName);
            } catch (Exception e) {
                // 日志记录失败不应影响主业务流程，仅打印错误日志
                log.error("操作日志记录失败", e);
            } finally {
                CurrentHolder.remove();
            }


        }
    }

    /**
     * 从请求上下文中获取当前操作人ID
     * 实际项目中应替换为真实的用户认证获取逻辑
     */
    private Integer getOperateEmpId() {
        return CurrentHolder.getCurrentId(); //
    }
}