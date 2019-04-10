package com.lab.meet.Controller;

import com.lab.meet.Model.ResponseBody;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * Basic Controller which is called for unhandled errors
 */
@Controller
@ApiIgnore
@Api("全局统一错误处理")
public class AppErrorController implements ErrorController {

    private ErrorAttributes errorAttributes;
    private final ServerProperties serverProperties;


    private final static String ERROR_PATH = "/error";

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        this.errorAttributes = errorAttributes;
        this.serverProperties = serverProperties;
    }


    @RequestMapping(value = ERROR_PATH)
    @org.springframework.web.bind.annotation.ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseBody<?> error(WebRequest request) {
        Map<String, Object> map = getErrorAttributes(request,
                isIncludeStackTrace(request));
        int status = (int) map.get("status");
        String message = (String) map.get("msg");
        return ResponseBody.failedRep(message, status);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private boolean isIncludeStackTrace(WebRequest request) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        return include == ErrorProperties.IncludeStacktrace.ALWAYS || include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM && getTraceParameter(request);
    }


    private Map<String, Object> getErrorAttributes(WebRequest request,
                                                   boolean includeStackTrace) {
        return this.errorAttributes.getErrorAttributes(request,
                includeStackTrace);
    }

    private boolean getTraceParameter(WebRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

}