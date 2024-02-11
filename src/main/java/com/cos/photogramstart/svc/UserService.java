package com.cos.photogramstart.svc;

import com.cos.photogramstart.domain.subscribe.SubscribeRepo;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomEx;
import com.cos.photogramstart.handler.ex.CustomValidationApiEx;
import com.cos.photogramstart.web.dto.img.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SubscribeRepo subscribeRepo;

    @Transactional(readOnly = true) //읽기전용이라 변경감지 연산을 안하기떄문에 성능 최적화
    public UserProfileDto memberProfile(int pageUserId,int principalId){
        UserProfileDto dto = new UserProfileDto();

        //SELECT * FROM image WHERE userid = :userId;
        User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
            throw new CustomEx("해당 프로필 페이지는 없는 페이지 입니다.");
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId); // 1은 페이지 주인, -1은 주인은 아님
        dto.setImgCount(userEntity.getImages().size());

        int subscribeState = subscribeRepo.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepo.mSubscribeCount(pageUserId);

        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);

        return dto;
    }

    @Transactional
    public User memberEdit(int id, User user) {
//        1. 영속화
          User userEntity = userRepository.findById(id).orElseThrow(() -> new CustomValidationApiEx("찾을 수 없는 아이디 입니다.")); // 1. 무조건 찾는다 - get() 2. 못찾았으면 ex - orElseThrow()

//        2. 영속화된 오브젝트 수정 - 더티체킹 (업뎃 완료)
          userEntity.setName(user.getName());

          String rawPassword = user.getPassword();
          String encPassword = bCryptPasswordEncoder.encode(rawPassword);
          userEntity.setPassword(encPassword);
          userEntity.setBio(user.getBio());
          userEntity.setWebsite(user.getWebsite());
          userEntity.setPhoneNum(user.getPhoneNum());
          userEntity.setGender(user.getGender());
          userEntity.setEmail(user.getEmail());
//        2. 영속화된 오브젝트 수정 - 더티체킹 (업뎃 완료)
        
        return userEntity;
        // 더티체킹이 일어나 업데이트 완료
    }
    
}
