package com.jmgits.sample.twilio.controller;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by javi.more.garc on 11/05/16.
 */
@RestController
@RequestMapping("/error")
public class SimpleErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @Autowired
    public SimpleErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public Map<String, Object> error(HttpServletRequest request, HttpServletResponse response) {

        //
        // should we have an uncontrolled exception (like SqlException), the
        // ErrorPageFilter would leave a 200 when running in a real tomcat

        forceToInternalServerErrorIfOk(response);

        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable error = this.errorAttributes.getError(requestAttributes);

        if (error != null) {
            ResponseStatus annotation = AnnotationUtils.getAnnotation(error.getClass(), ResponseStatus.class);

            if (annotation != null) {
                request.setAttribute("javax.servlet.error.status_code", annotation.value().value());
                response.setStatus(annotation.value().value());
            }
        }

        return getErrorAttributes(request, getTraceParameter(request));
    }

    //
    // private methods

    private void forceToInternalServerErrorIfOk(HttpServletResponse response) {

        ResponseFacade responseFacade = getResponseFacade(response);

        // if there is a no response facade or it contains an error status
        if (responseFacade == null || responseFacade.getStatus() >= 300) {
            // nothing to do
            return;
        }

        // force the status to internal server error
        responseFacade.setStatus(INTERNAL_SERVER_ERROR.value());

    }

    private ResponseFacade getResponseFacade(HttpServletResponse response) {

        if (!(response instanceof HttpServletResponseWrapper) && !(response instanceof ResponseFacade)) {
            return null;
        }

        if (response instanceof HttpServletResponseWrapper) {

            HttpServletResponse wrapped = (HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();

            return getResponseFacade(wrapped);
        }

        return (ResponseFacade) response;

    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);

        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);

        return new TreeMap<>(errorAttributes);
    }

    private static boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equalsIgnoreCase(parameter);
    }

}