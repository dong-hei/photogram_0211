package com.cos.photogramstart.svc;

import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.likes.LikesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesSvc {

    private final LikesRepo likesRepo;

    @Transactional
    public void like(int imageId, int principalId){
        likesRepo.mLikes(imageId, principalId);
    }

    @Transactional
    public void unLike(int imageId, int principalId){
        likesRepo.mUnLikes(imageId, principalId);

    }

}
