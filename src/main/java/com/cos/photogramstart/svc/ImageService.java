package com.cos.photogramstart.svc;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.domain.img.ImageRepo;
import com.cos.photogramstart.web.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepo imageRepo;

    @Transactional(readOnly = true)
    public List<Image> popularImgs(){
            return imageRepo.mPopular();
    }

    @Transactional(readOnly = true) // 디폴트: 영속성 컨텍스트 변경 감지 -> 더티체킹 -> flush(반영) But readOnly를 하면 flush()를 안한다.
    public Page<Image> imgStory(int principalId, Pageable pageable) {
        Page<Image> images = imageRepo.mStory(principalId, pageable);

        //imgs에 좋아요 상태 담기
        images.forEach((image) -> {

            image.setLikeCount(image.getLikes().size());

            image.getLikes().forEach((like)->{
                if(like.getUser().getId() == principalId){
                    //해당 이미지에 좋아요 한 사람을 찾아 현제 로그인 사람이 좋아요 한건지 비교
                    image.setLikeState(true);
                }
            });
        });

        return images;
    }

    @Value("${file.path}") // application.yml 설정 경로 가져오기
    //업로드 파일을 프로젝트 외부에 두는 이유는?
    //프로젝트 내부에 두면 정적인 파일 중에 하나인 이미지파일도 deploy(배포를 하게된다.)
    // 브라우저로 파일업로드를하면 deploy하는 속도보다(상대적으로 느림) 브라우저가 파일업로드 요청을 받는(상대적으로 빠름) 시차 때문에 엑박이 뜬다.
    // 그래서 이런 파일업로드는 외부에 두는 것이 좋다.

    private String uploadFolder;

    @Transactional //이미지 서비스에 걸어두는 이유? INSERT, UPDATE, DELETE 활동이 있으면 걸어두는 습관이 좋다.
    public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
        UUID uuid = UUID.randomUUID(); // 파일이 중복될때 중복을 감지하기 위함
        String imageFileName = uuid+"_" +imageUploadDto.getFile().getOriginalFilename(); // 실제 파일명

        Path imgFilePath = Paths.get(uploadFolder + imageFileName);

        // 통신,I/O -> 예외 발생 가능성이 있기때문에 예외처리
        try {
            Files.write(imgFilePath, imageUploadDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image 테이블에 저장
        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
        imageRepo.save(image);

//        @Data와 toString으로 무한참조가 일어나서 충돌이 나게된다.
//        System.out.println("imageEntity = " + imageEntity);
    }

}
