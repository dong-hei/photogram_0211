package com.cos.photogramstart.handler;


import com.cos.photogramstart.handler.ex.CustomEx;
import com.cos.photogramstart.handler.ex.CustomValidationApiEx;
import com.cos.photogramstart.handler.ex.CustomValidationEx;
import com.cos.photogramstart.utills.Script;
import com.cos.photogramstart.web.dto.CMResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 컨트롤러 익셉션 받는 용도
@RestController
public class ControllerExHandler {


    @ExceptionHandler(CustomValidationEx.class)
    public String validationEx(CustomValidationEx e){

        if (e.getErrorMap() == null) {
            return Script.back(e.getMessage());
        }else{
            return Script.back(e.getErrorMap().toString());
        }
        //CMResDto.Script 비교
        //클라에 응답 -> Script 좋다
        //Ajax 통신 -> CMResDto가 좋다
        //Android 통신 -> CMresDto가 좋다
//        System.out.println("에러맵 메세지" + e.getErrorMap());
//        return new CMResDto(-1,e.getMessage(), e.getErrorMap());
//                return new CMResDto(-1,e.getMessage(), "회원가입에 실패했습니다.");
    }

    @ExceptionHandler(CustomValidationApiEx.class)
    public ResponseEntity<?> validationApiEx(CustomValidationApiEx e){
        System.out.println("에러맵 메세지" + e.getErrorMap());
        return new ResponseEntity<CMResDto<?>>(new CMResDto<>(-1,e.getMessage(),e.getErrorMap()),HttpStatus.BAD_REQUEST);
//        return new CMResDto(-1,e.getMessage(), e.getErrorMap());
    }

    @ExceptionHandler(CustomEx.class)
    public String ex(CustomValidationEx e){
        return Script.back(e.getMessage());
    }
}
