package com.cos.photogramstart.svc;

import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.domain.subscribe.SubscribeRepo;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiEx;
import com.cos.photogramstart.handler.ex.CustomEx;
import com.cos.photogramstart.handler.ex.CustomValidationApiEx;
import com.cos.photogramstart.web.dto.img.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SubscribeRepo subscribeRepo;

    @Value("${file.path}")
    private String uploadFolder;

    @Transactional
    public User memberProfileImgUpdate(int principalId, MultipartFile profileImageFile){
        UUID uuid = UUID.randomUUID(); // 파일이 중복될때 중복을 감지하기 위함
        String imageFileName = uuid+"_" +profileImageFile.getOriginalFilename(); // 실제 파일명

        Path imgFilePath = Paths.get(uploadFolder + imageFileName);

        // 통신,I/O -> 예외 발생 가능성이 있기때문에 예외처리
        try {
            Files.write(imgFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        User userEntity = userRepository.findById(principalId).orElseThrow(()->{
            throw new CustomApiEx("유저를 찾을수 없습니다.");
        });
        userEntity.setProfileImgUrl(imageFileName);
        
        return userEntity;
    } // 트랜젝션이 걸려있으므로 더티체킹으로 업데이트 함

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

//        프로필사진에서 likes Count 추가
        userEntity.getImages().forEach((image) -> {image.setLikeCount(image.getLikes().size());});
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
