package org.hongxi.spring.boot.web.filter;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hongxi.spring.boot.web.constants.WebConstants.WEB_CRYPTO_FILTER_ORDER;

/**
 * 有时出于安全考虑，请求、响应数据需要加密，则在这个 filter 里解密 request、加密 response
 *
 * Created by shenhongxi on 2020/10/23.
 */
public class RequestResponseWrapperFilter extends OncePerRequestFilter implements OrderedFilter {

    private int order = REQUEST_WRAPPER_FILTER_MAX_ORDER + WEB_CRYPTO_FILTER_ORDER;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        byte[] requestBody = IOUtils.toByteArray(request.getInputStream());
        // 如果客户端对请求数据进行了加密，这里需要对 requestBody 进行解密
        OpenHttpServletRequestWrapper wrapperRequest =
                new OpenHttpServletRequestWrapper(request, requestBody);
        OpenHttpServletResponseWrapper wrapperResponse =
                new OpenHttpServletResponseWrapper(response, new ResponseBodyHandler());
        filterChain.doFilter(wrapperRequest, wrapperResponse);
        wrapperResponse.copyBodyToResponse();
    }
}
