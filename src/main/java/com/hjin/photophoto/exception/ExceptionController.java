//package com.hjin.photophoto.exception;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Map;
//import java.util.Objects;
//
//@Controller
//@Slf4j
//public class ExceptionController implements ErrorController {
//
//    @Override
//    public String getErrorPath() {
//        return null;
//    }
//
//    @RequestMapping(value = "/error")
//    public ResponseEntity<Object> handleNoHandlerFoundException(HttpServletResponse response,
//                                                                HttpServletRequest request) {
//
//
////        System.out.println(response.getStatus());  //오류 상태
////        System.out.println(request.getRequestURI());        //요청 주소
//
////        {
////            "timestamp": "2019-02-15T21:48:44.447+0000",
////                "status": 404,
////                "error": "Not Found",
////                "message": "No message available",
////                "path": "/123"
////        }
//        if(Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
//            Map<String, Object> body = Map.of(
//                    "timestamp", System.currentTimeMillis(),
//                    "error", "Not Found",
//                    "message", "",
//                    "path", request.getRequestURI()
//            );
//
//            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
//    }
//}
