package com.imthath.food_street.user_service.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Constraint(validatedBy = ValidPhoneNumber.PhoneNumberValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Phone number must be exactly 10 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
        @Override
        public void initialize(ValidPhoneNumber constraintAnnotation) {
        }

        @Override
        public boolean isValid(String phone, ConstraintValidatorContext context) {
            return phone != null && phone.matches("\\d{10}");
        }
    }
}