package com.hssa.common.annotation; import java.lang.annotation.*; @Target({ElementType.FIELD, ElementType.PARAMETER}) @Retention(RetentionPolicy.RUNTIME) public @interface TrimString {}
