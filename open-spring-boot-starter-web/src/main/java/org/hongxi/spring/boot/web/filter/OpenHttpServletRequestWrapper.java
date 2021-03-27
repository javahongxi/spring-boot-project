package org.hongxi.spring.boot.web.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by shenhongxi on 2021/3/27.
 */
public class OpenHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] jsonBytes;

    public OpenHttpServletRequestWrapper(HttpServletRequest request, byte[] jsonBytes) {
        super(request);
        this.jsonBytes = jsonBytes;
    }

    @Override
    public String getContentType() {
        if (super.getContentType() == null) {
            return MediaType.APPLICATION_JSON_VALUE;
        }
        if (super.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            return MediaType.APPLICATION_JSON_VALUE;
        }
        return super.getContentType();
    }

    @Override
    public String getHeader(String name) {
        if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
            return getContentType();
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
            return Collections.enumeration(Arrays.asList(getContentType()));
        }
        return super.getHeaders(name);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(jsonBytes), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BodyServletInputStream(jsonBytes);
    }
}
