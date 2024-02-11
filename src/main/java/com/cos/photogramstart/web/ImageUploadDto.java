package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ImageUploadDto {
    private MultipartFile File;
    private String caption;

    public Image toEntity(String postImageUrl, User user){
        return Image.builder()
                .caption(caption)
                .postImgUrl(postImageUrl)
                .user(user)
                .build();
    }
}
