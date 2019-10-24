/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.logging;

import static com.att.eelf.configuration.Configuration.MDC_SERVER_FQDN;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.onap.portalsdk.core.logging.aspect.EELFLoggerAdvice;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.AppService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.onap.vid.controller.ControllersUtils;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@org.springframework.context.annotation.Configuration
public class VidLoggerAspect {

    private String canonicalHostName;
    private final ControllersUtils controllersUtils;
    private final String appName;

    private final EELFLoggerAdvice advice;

    @Autowired
    public VidLoggerAspect(EELFLoggerAdvice advice, SystemPropertiesWrapper systemPropertiesWrapper,
        AppService appService) {
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            canonicalHostName = localHost.getCanonicalHostName();
        } catch (UnknownHostException e) {
            // YOLO
            canonicalHostName = null;
        }
        this.advice = advice;
        this.controllersUtils = new ControllersUtils(systemPropertiesWrapper);

        this.appName = defaultIfEmpty(appService.getDefaultAppName(), SystemProperties.SDK_NAME);
    }

    @Pointcut("execution(public * org.onap.vid.controller..*Controller.*(..))")
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
        Object[] returnArgs = advice.before(securityEventType, fabricateArgsWithNull(), passOnArgs);

        HttpServletRequest httpServletRequest = httpServletRequestOrNull(joinPoint);
        fixSetRequestBasedDefaultsIntoGlobalLoggingContext(httpServletRequest,
            joinPoint.getSignature().getDeclaringType().getName());

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

    /**
     * Returns an array with a single entry with a null value. This will stop org.onap.portalsdk.core.logging.aspect.EELFLoggerAdvice.before
     * from throwing on ArrayIndexOutOfBound, and also prevent SessionExpiredException.
     */
    private Object[] fabricateArgsWithNull() {
        return new Object[]{null};
    }

    /**
     * Finds the first joinPoint's param which is an HttpServletRequest. If not found, use Spring's RequestContextHolder
     * to retrieve it.
     *
     * @return null or the current httpServletRequest
     */
    private HttpServletRequest httpServletRequestOrNull(ProceedingJoinPoint joinPoint) {
        final Object httpServletRequest = Arrays.stream(joinPoint.getArgs())
            .filter(param -> param instanceof HttpServletRequest)
            .findFirst()
            .orElseGet(() -> {
                try {
                    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                } catch (Exception e) { // ClassCast, IllegalState, etc.
                    return null;
                }
            });

        return (HttpServletRequest) httpServletRequest;
    }

    /**
     * Mimics a part from org.onap.portalsdk.core.logging.aspect.EELFLoggerAdvice.before, but with much more carefulness
     * of exceptions and defaults. Main difference is that if no session, function does not throw. It just fallback to
     * an empty loginId.
     */
    private void fixSetRequestBasedDefaultsIntoGlobalLoggingContext(HttpServletRequest httpServletRequest, String className) {
        if (httpServletRequest != null) {

            EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(className);
            String requestId = UserUtils.getRequestId(httpServletRequest);
            String loginId = controllersUtils.extractUserId(httpServletRequest);

            logger.setRequestBasedDefaultsIntoGlobalLoggingContext(httpServletRequest, appName, requestId, loginId);
        }
    }

    // Override the non-canonical hostname set by EELFLoggerDelegate::setGlobalLoggingContext()
    // that was invoked by advice.before() (and some other SDK cases)
    private void fixServerFqdnInMDC() {
        if (!StringUtils.isBlank(canonicalHostName)) {
            EELFLoggerDelegate.mdcPut(MDC_SERVER_FQDN, canonicalHostName);
        }
    }

}
