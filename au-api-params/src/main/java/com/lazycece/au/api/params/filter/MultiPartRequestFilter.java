package com.lazycece.au.api.params.filter;

import com.lazycece.au.context.RequestContext;
import com.lazycece.au.filter.AuFilter;
import com.lazycece.au.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author lazycece
 */
public class MultiPartRequestFilter implements AuFilter {

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String REQUEST_FIELD = "request";

    @Override
    public boolean preHandle() throws Exception {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType)
                && contentType.contains(MULTIPART_FORM_DATA)
                && request instanceof HttpServletRequestWrapper) {
            HttpServletRequestWrapper requestWrapper = (HttpServletRequestWrapper) request;
            Field requestFiled = FieldUtils.getField(HttpServletRequestWrapper.class, REQUEST_FIELD, true);
            MultiPartRequestWrapper multiPartRequestWrapper = new MultiPartRequestWrapper((HttpServletRequest) requestFiled.get(requestWrapper));
            FieldUtils.writeField(requestFiled, requestWrapper, multiPartRequestWrapper, true);
        }
        return true;
    }

    static class MultiPartRequestWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {

        private Collection<Part> parts = null;

        MultiPartRequestWrapper(HttpServletRequest request) {
            super(request);
            parseMultipartFormData(request);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<Part> getParts() {
            if (this.parts == null) {
                return Collections.EMPTY_LIST;
            }
            return this.parts;
        }

        @Override
        public Part getPart(String name) {
            for (Part part : getParts()) {
                if (name.equals(part.getName())) {
                    return part;
                }
            }
            return null;
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        private void parseMultipartFormData(HttpServletRequest request) {
            try {
                this.parts = request.getParts();
                Map<String, String[]> parameters = request.getParameterMap();
                if (parameters != null) {
                    Map<String, List<String>> paramsMap = new HashMap<>(16);
                    parameters.forEach((name, values) -> {
                        if (values != null) {
                            paramsMap.put(name, Arrays.asList(values));
                        } else {
                            paramsMap.put(name, new ArrayList<>());
                        }
                    });
                    RequestContext.getCurrentContext().setRequestQueryParams(paramsMap);
                }
            } catch (IOException | ServletException e) {
                throw new IllegalStateException("parse request error: {}", e);
            }
        }
    }
}
