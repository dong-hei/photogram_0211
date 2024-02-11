package com.cos.photogramstart.web.dto.auth;

import com.cos.photogramstart.domain.user.User;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data //Getter, Setter
public class SignupDto {

    //어차피 앞단에서 막히는데 굳이 제약사항을 거는 이유는? -> PostMan으로 오는 요청 막으려고

    @Size(min = 2, max = 20) //20자 이하
    @NotBlank
    private String username;

    @NotBlank //스페이스바,Null 불가
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    //빌더 패턴
    public User toEntity(){
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .build();
    }

}
