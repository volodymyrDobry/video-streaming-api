package com.viora.contentservice.domain.helper;

import com.viora.contentservice.domain.exception.DomainValidationException;

import java.util.Objects;

public class DomainCreatorChecker {

    public static void checkDomainValues(Object... objects) {
        for (Object obj : objects) {
            if (Objects.isNull(obj)) {
                throw new DomainValidationException("None of the fields can be of null value");
            }
        }
    }

}
