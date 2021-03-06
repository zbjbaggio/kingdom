package com.kingdom.system.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A {@link Filter} that adds cross-origin resource sharing (CORS) headers to the response.
 * <p>
 * Created by matthias.wendt on 19.05.15.
 */
@Component
public class CORSFilter implements Filter {

    /**
     * Adds CORS headers to the response. Then calls {@link FilterChain#doFilter} on the rest of the chain.
     *
     * @param request  the servlet request.
     * @param response the servlet response.
     * @param chain    the filter chain.
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        servletResponse.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
        servletResponse.setHeader("Access-Control-Max-Age", "3600");
        servletResponse.setHeader("Access-Control-Allow-Headers", "Origin, x-requested-with, Content-Type, Accept, token, key");
        chain.doFilter(request, response);
    }

    /**
     * No effect.
     *
     * @param config the filter config.
     */
    public void init(FilterConfig config) {// make sonar happy
    }

    /**
     * No effect.
     */
    public void destroy() {        // make sonar happy
    }

}
