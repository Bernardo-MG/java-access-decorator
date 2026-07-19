
package com.bernardomg.framework.security.access.test.interceptor.unit;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bernardomg.framework.security.access.annotation.RequireResourceAuthorization;
import com.bernardomg.framework.security.access.interceptor.RequireResourceAuthorizationInterceptor;
import com.bernardomg.framework.security.access.interceptor.ResourceAccessValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequireResourceAuthorizationInterceptor")
class TestRequireResourceAuthorizationInterceptor {

    static class TestService {

        @RequireResourceAuthorization(resource = "data", action = "read")
        public void annotatedMethod() {}

    }

    private RequireResourceAuthorizationInterceptor interceptor;

    @Mock
    private JoinPoint                               jp;

    @Mock
    private ResourceAccessValidator                 validator;

    @BeforeEach
    public final void initializeInterceptor() {
        interceptor = new RequireResourceAuthorizationInterceptor(validator,
            () -> new RuntimeException("Missing authentication"));
    }

    @Test
    @DisplayName("When the method is authorized, the interceptor accepts the call")
    void allowsExecution_WhenAuthorized() throws Exception {
        final Method           method;
        final ThrowingCallable exec;

        // GIVEN
        method = TestService.class.getMethod("annotatedMethod");

        // WHEN
        given(validator.isAuthorized("data", "read")).willReturn(true);

        exec = () -> interceptor.before(jp, method.getAnnotation(RequireResourceAuthorization.class));
        assertThatCode(exec).doesNotThrowAnyException();

        // THEN
        verify(validator).isAuthorized("data", "read");
    }

    @Test
    @DisplayName("When the method is authorized, the interceptor throws the supplied exception")
    void deniesExecution_WhenNotAuthorized() throws Exception {
        final Method           method;
        final ThrowingCallable exec;

        // GIVEN
        method = TestService.class.getMethod("annotatedMethod");

        // WHEN
        given(validator.isAuthorized("data", "read")).willReturn(false);

        // THEN
        exec = () -> interceptor.before(jp, method.getAnnotation(RequireResourceAuthorization.class));
        assertThatThrownBy(exec).isInstanceOf(RuntimeException.class);
    }

}
