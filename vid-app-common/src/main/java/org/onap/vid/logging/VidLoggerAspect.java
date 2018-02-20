package org.onap.vid.logging;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.onap.portalsdk.core.logging.aspect.EELFLoggerAdvice;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.att.eelf.configuration.Configuration.MDC_SERVER_FQDN;


@Aspect
@org.springframework.context.annotation.Configuration
public class VidLoggerAspect {

    private String canonicalHostName;
    @Autowired
    EELFLoggerAdvice advice;

    public VidLoggerAspect() {
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            canonicalHostName = localHost.getCanonicalHostName();
        } catch (UnknownHostException e) {
            // YOLO
            canonicalHostName = null;
        }
    }

    @Pointcut("execution(public * org.onap.vid.controller.*Controller.*(..))")
    public void vidControllers() {}

    @Around("vidControllers() && (" +
            "  @within(org.onap.portalsdk.core.logging.aspect.AuditLog)" +
            "  || @annotation(org.onap.portalsdk.core.logging.aspect.AuditLog)" +
            "  || @annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            ")")
    public Object logAuditMethodClassAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAroundMethod(joinPoint, SystemProperties.SecurityEventTypeEnum.INCOMING_REST_MESSAGE);
    }

    private Object logAroundMethod(ProceedingJoinPoint joinPoint, SystemProperties.SecurityEventTypeEnum securityEventType) throws Throwable {
        //Before
        Object[] passOnArgs = new Object[] {joinPoint.getSignature().getDeclaringType().getName(),joinPoint.getSignature().getName()};
        Object[] returnArgs = advice.before(securityEventType, joinPoint.getArgs(), passOnArgs);

        fixServerFqdnInMDC();

        //Execute the actual method
        Object result;
        String restStatus = "COMPLETE";
        try {
            result = joinPoint.proceed();
        } catch(Exception e) {
            restStatus = "ERROR";
            throw e;
        } finally {
            fixStatusCodeInMDC(restStatus);
            advice.after(securityEventType, restStatus, joinPoint.getArgs(), returnArgs, passOnArgs);
        }

        return result;
    }

    // Set the status code into MDC *before* the metrics log is written by advice.after()
    private void fixStatusCodeInMDC(String restStatus) {
        EELFLoggerDelegate.mdcPut(SystemProperties.STATUS_CODE, restStatus);
    }

    // Override the non-canonical hostname set by EELFLoggerDelegate::setGlobalLoggingContext()
    // that was invoked by advice.before() (and some other SDK cases)
    private void fixServerFqdnInMDC() {
        if (!StringUtils.isBlank(canonicalHostName)) {
            EELFLoggerDelegate.mdcPut(MDC_SERVER_FQDN, canonicalHostName);
        }
    }

}
