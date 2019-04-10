package com.lab.meet.Config;

import com.lab.meet.Model.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public ResponseBody<?> processException(HttpServletRequest req, Exception e) {
        return GlobalExceptionHandler.getResponse(e);
    }


    private static ResponseBody<?> getResponse(Exception e) {
        return ResponseBody.failedRep(e.getMessage(), ResponseBody.ERROR);
    }

}
