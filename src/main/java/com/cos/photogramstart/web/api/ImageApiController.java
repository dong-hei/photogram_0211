package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.svc.ImageSvc;
import com.cos.photogramstart.svc.LikesSvc;
import com.cos.photogramstart.web.dto.CMResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageApiController {

    private final ImageSvc imgSvc;
    private final LikesSvc likesSvc;

    @GetMapping("/api/image")
    public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PageableDefault(size = 3) Pageable pageable) {
        Page<Image> images = imgSvc.imgStory(principalDetails.getUser().getId(), pageable);
        return new ResponseEntity<>(new CMResDto<>(1, "성공!", images), HttpStatus.OK);
    }

    //어떤 이미지를 좋아요했는지
    @PostMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> likes(@PathVariable int imageId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        likesSvc.like(imageId,principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMResDto<>(1, "좋아요 성공!", null), HttpStatus.CREATED);

    }

    @DeleteMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> unlikes(@PathVariable int imageId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        likesSvc.unLike(imageId,principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMResDto<>(1, "좋아요 해제 성공!", null), HttpStatus.OK);

    }
}
