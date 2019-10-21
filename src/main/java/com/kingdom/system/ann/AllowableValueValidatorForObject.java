package com.kingdom.system.ann;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllowableValueValidatorForObject implements ConstraintValidator<AllowableValue, Object> {

    private int[] intValues;

    private String[] stringValues;

    @Override
    public void initialize(AllowableValue constraintAnnotation) {
        intValues = constraintAnnotation.intValues();
        stringValues = constraintAnnotation.stringValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        //同时校验两种类型，或者两种都不校验还使用此注解，则校验失败
        if (!(intValues.length == 0 ^ stringValues.length == 0)) {
            return false;
        }

        //值为空，校验不为空，则校验失败
        if (value == null) {
            return false;
        }

        //值不为空且校验不为空
        if (value instanceof Integer) {
            int val = Integer.parseInt(value.toString());
            for (int intValue : intValues) {
                if (val == intValue) {
                    return true;
                }
            }
            return false;
        } else if (value instanceof String) {
            String val = value.toString();
            List<String> arrayList = Arrays.asList(stringValues);
            return arrayList.contains(val);
        }
        return false;
    }
}
