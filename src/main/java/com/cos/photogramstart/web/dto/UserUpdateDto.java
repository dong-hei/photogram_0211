package com.cos.photogramstart.web.dto;

import com.cos.photogramstart.domain.user.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class UserUpdateDto {

    @NotBlank
    private String name; //필수

    @NotBlank
    private String password; //필수
    private String website;
    private String bio;
    private String phoneNum;
    private String gender;
    private String email;

    //빌더 패턴, 필수가 아닌 값들은 엔티티로 만들면 위험하다.
    public User toEntity(){
        return User.builder()
                .name(name) // 이름을 기재 안하는것도 당연히 문제
                .password(password) // 패스워드를 기재하지 않으면 공백이 들어가므로 문제가 된다 -> 당연히 Validation 체크해야됨
                .website(website)
                .bio(bio)
                .phoneNum(phoneNum)
                .gender(gender)
                .email(email)
                .build();
    }

}
