package com.james.common.netcore.networking.http.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author elson, email:liuqi2elson@163.com
 * @date 2020/7/10
 * @Desc 在解析数据时，是否需要使用 HiConverterFactory，默认需要。
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface WithHiResponse {
    boolean value() default true;
}
