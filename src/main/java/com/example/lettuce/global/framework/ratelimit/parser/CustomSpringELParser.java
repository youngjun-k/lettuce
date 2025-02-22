package com.example.lettuce.global.framework.ratelimit.parser;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

import org.springframework.expression.EvaluationContext;

public class CustomSpringELParser {

    /**
     * 메서드 파라미터에서 SpEL 표현식을 이용해 특정 값을 추출하는 메서드.
     *
     * @param parameterNames 메서드 파라미터 이름 배열
     * @param args           메서드 실행 시 전달된 인자 값 배열
     * @param key            SpEL 표현식 (예: "#userId")
     * @return 추출된 값
     */
    public static String getDynamicValue(Method method, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        // StandardEvaluationContext context = new StandardEvaluationContext();

        ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

        EvaluationContext context = (EvaluationContext) new MethodBasedEvaluationContext(
                null, method, args, paramNameDiscoverer);

        return parser.parseExpression(key).getValue(context, String.class);
    }
}