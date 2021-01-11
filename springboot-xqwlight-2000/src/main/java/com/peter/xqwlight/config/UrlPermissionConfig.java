package com.peter.xqwlight.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "permission")
public class UrlPermissionConfig {

    private List<String> permitAllUrls = new ArrayList<>();

    private List<String> authenticatedUrls = new ArrayList<>();

}
