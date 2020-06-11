/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Leon
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAllExceptionMethod(Exception ex,WebRequest requset,HttpServletResponse res) {
 

            return new ResponseEntity<Object>("An Error Has Occured", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);          
        } 

//    @ExceptionHandler(value 
//      = { IllegalArgumentException.class, IllegalStateException.class })
//    protected ResponseEntity<Object> handleConflict(
//      RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "An Error Has Occured";
//        return handleExceptionInternal(ex, bodyOfResponse, 
//          new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
}
