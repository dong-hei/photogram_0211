package com.cos.photogramstart.web;


import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.svc.UserService;
import com.cos.photogramstart.web.dto.img.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{pageUserId}")
    public String profile(@PathVariable int pageUserId,
                          Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails){
        UserProfileDto dto = userService.memberProfile(pageUserId, principalDetails.getUser().getId());
        model.addAttribute("dto", dto);
        return "user/profile";
    }

    //@AuthenticationPrincipal SpringSecurity 어딘가에 있는 나의 세션 정보를 찾고자할때샤용
    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails){

//        무한 루프를 돌기때문에 주석처리
//        System.out.println("세션정보 = " + principalDetails.getUser()); //세션정보를 불러올때 추천하는 방법
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //어딘가에 있는 나의 세션 정보를 찾고자할때샤용 (어노테이션에 비하면 조금 더 복잡)
//        Object mPrincipalDetails = auth.getPrincipal();
//        System.out.println("auth = " + auth);

//        JSP파일 헤더에 이렇게 넣으면 model에 안넘겨도 된다.
//        model.addAttribute("principal", principalDetails.getUser());

//        <!-- 세션에 접속하는 방법, JSP의 모든페이지에 ${principal} 이라고 적으면 principal detail에 접근 가능 -->
//<sec:authorize access="isAuthenticated()">
//    <sec:authentication property="principal" var="principal"/>

        return "user/update";
    }
}
