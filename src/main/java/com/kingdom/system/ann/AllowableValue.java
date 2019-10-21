package com.kingdom.system.ann;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by gu shuang
 * Description: 校验参数的允许值
 * Date: 2018-05-18
 * Time: 10:49
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AllowableValueValidatorForObject.class})
@ReportAsSingleViolation
public @interface AllowableValue {

    String message() default "参数值超出预期！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] intValues() default {};

    String[] stringValues() default {};
}
