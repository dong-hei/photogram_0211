package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationEx;
import com.cos.photogramstart.svc.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor // final 필드 DI
public class AuthController {

    @Autowired
    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    //시큐리티가 POST를 막아서 진행되지 않음 (CSRF 토큰을 시큐리티가 검사해봤더니 없어서.)
    @PostMapping("/auth/signup")
    public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            Map<Object, Object> errorMap = new HashMap<>();

            for (FieldError e : bindingResult.getFieldErrors()) {
                errorMap.put(e.getField(), e.getDefaultMessage());
                System.out.println("에러메세지");
                System.out.println(e.getDefaultMessage());
            }
            throw new CustomValidationEx("유효성 검사 실패!", errorMap);
        } else {
            // user -> signupDto
            User user = signupDto.toEntity();
            User userEntity = authService.signin(user);
            System.out.println("userEntity = " + userEntity);
            log.info(user.toString());
            return "auth/signin";
        }
        //회원가입 validation 전처리


    }
}
