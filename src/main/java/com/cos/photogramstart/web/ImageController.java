package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.handler.ex.CustomValidationEx;
import com.cos.photogramstart.svc.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageSvc;

    @GetMapping({"/", "image/story"})
    public String story() {
        return "image/story";
    }

    @GetMapping("image/popular")
    public String popular(Model model) {

        List<Image> images = imageSvc.popularImgs();
        //인기 페이지
        model.addAttribute("images",images);
        return "image/popular";
    }

    @GetMapping("/image/upload")
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
