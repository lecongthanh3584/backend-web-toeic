package com.backend.spring.exception;

import com.backend.spring.constants.MessageConstant;
import com.backend.spring.enums.EStatusCode;
import com.backend.spring.payload.response.main.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRunTimeException(RuntimeException e) {
        return new ResponseEntity<>(new ResponseData<>(EStatusCode.EXCEPTION_OCCURRED.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>(new ResponseData<>(EStatusCode.EXCEPTION_OCCURRED.getValue(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<?> handleRefreshTokenException(RefreshTokenException e) {
        return new ResponseEntity<>(new ResponseData<>(e.getCode(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
    public ResponseEntity<?> handleOAuth2AuthenException(OAuth2AuthenticationProcessingException e) {
        return new ResponseEntity<>(new ResponseData<>(e.getCode(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(new ResponseData<>(EStatusCode.DATA_INVALID.getValue(), MessageConstant.INVALID_DATA, errors),
                HttpStatus.BAD_REQUEST);
    }

/*    Tất cả các exception extend từ runtimeException và Exception sẽ đều chạy vào hàm xử lý tương
      ứng ở trên, nếu muốn xử lý cho từng ngoại lệ thì sẽ tạo ra một @ExceptionHandler tương ứng với
      ngoại lệ đó
*/

}
