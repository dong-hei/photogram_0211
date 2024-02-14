package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiEx;
import com.cos.photogramstart.svc.SubscribeService;
import com.cos.photogramstart.svc.UserService;
import com.cos.photogramstart.web.dto.CMResDto;
import com.cos.photogramstart.web.dto.UserUpdateDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final SubscribeService subscribeSvc;

    //MultipartFile 파람 : dto가 아닌 ajax통신으로 img를 받을때 필요함, 변수명이 중요한데 jsp에 name과 정확하게 매칭해야 반영됨
    @PutMapping("/api/user/{principalId}/profileImgUrl")
    public ResponseEntity<?> profileImgUrlUpdate(@PathVariable int principalId,
                                                 MultipartFile profileImageFile,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){
        User userEntity = userService.memberProfileImgUpdate(principalId, profileImageFile);
        principalDetails.setUser(userEntity);
        return new ResponseEntity<>(new CMResDto<>(1, "프로필사진 변경 성공", null), HttpStatus.OK);
    }

    @GetMapping("/api/user/{pageUserId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable int pageUserId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        List<SubscribeDto> dto = subscribeSvc.subscribeList(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMResDto<>(1, "구독자 정보 리스트 조회 성공", dto), HttpStatus.OK);

    }

    @PutMapping("/api/user/{id}")
    public CMResDto<?> update(
            @PathVariable int id,
            @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult, // 반드시 @Valid가 적혀있는 다음 파람에 적어야한다.
            @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(bindingResult.hasErrors()){
            Map<Object, Object> errorMap = new HashMap<>();

            for (FieldError e : bindingResult.getFieldErrors()) {
                errorMap.put(e.getField(), e.getDefaultMessage());
//                System.out.println("에러메세지");
//                System.out.println(e.getDefaultMessage());
            }
            throw new CustomValidationApiEx("유효성 검사 실패!", errorMap);
        }else {

            System.out.println("userUpdateDto = " + userUpdateDto);
            User userEntity = userService.memberEdit(id, userUpdateDto.toEntity());
            principalDetails.setUser(userEntity); //세션정보 변경

            return new CMResDto<>(1, "회원수정 완료", userEntity);
            //userEntity이 응답시 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답.
            //user내에 vo 객체, 그중에 있는 Image -> Image내에 vo 객체, 그 중에 있는 User -> user내에 vo 객체, 그중에 있는 Image
            //무한 참조
        }
    }

}
