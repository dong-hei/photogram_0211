package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.handler.ex.CustomValidationEx;
import com.cos.photogramstart.svc.ImageSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageSvc imageSvc;

    @GetMapping({"/", "image/story"})
    public String story() {
        return "image/story";
    }

    @GetMapping("image/popular")
    public String popular() {
        return "image/popular";
    }

    @GetMapping("image/upload")
    public String upload() {
        return "image/upload";
    }

    @PostMapping("/image")
    public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        if (imageUploadDto.getFile().isEmpty()) {
            throw new CustomValidationEx("이미지가 첨부되지 않았습니다",null);
        }
       
        //Svc 호출
        imageSvc.imageUpload(imageUploadDto,principalDetails);

        return "redirect:/user/" + principalDetails.getUser().getId();
    }
}
