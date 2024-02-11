package com.cos.photogramstart.web.dto.img;

import com.cos.photogramstart.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
    private boolean pageOwnerState; //페이지가 누구한테 접속되어있는지 확인하는 상태
    private int imgCount; //이미지 개수
    private boolean subscribeState; // 구독상태
    private int subscribeCount; // 구독 개수
    private User user;
}
