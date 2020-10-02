package com.azure.backend;

import org.springframework.data.annotation.Version;

public class BaseDto {

    @Version
    private String _etag;

}