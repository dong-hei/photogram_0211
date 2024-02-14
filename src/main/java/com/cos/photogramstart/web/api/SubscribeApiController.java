package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.svc.SubscribeService;
import com.cos.photogramstart.web.dto.CMResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscribeApiController {

    private final SubscribeService subscribeSvc;

    @PostMapping("/api/subscribe/{toUserId}") //누굴 구독하겠다.
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @PathVariable int toUserId){
        subscribeSvc.subscribe(principalDetails.getUser().getId(), toUserId);
        return new ResponseEntity<>(new CMResDto<>(1,"구독 성공" , null), HttpStatus.OK);
    }

    @DeleteMapping("/api/subscribe/{toUserId}") //누굴 구독취소하겠다.
    public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @PathVariable int toUserId){
        subscribeSvc.unSubscribe(principalDetails.getUser().getId(), toUserId);
        return new ResponseEntity<>(new CMResDto<>(1,"구독 취소 성공" , null), HttpStatus.OK);
    }
}
