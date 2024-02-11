package com.cos.photogramstart.domain.img;

import com.cos.photogramstart.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption; // 내용
    private String postImgUrl; // 사진을 전송받아 그 사진을 서버 특정 폴더에 저장 - DB에 저장된 경로를 insert


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    
    //이미지 좋아요,댓글

    private LocalDateTime createDate;


    @PrePersist //DB에 INSERT 되기 직전에 실행
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }

//    오브젝트가 콘솔에 출력될 때 문제가 될수 있어 User 부분을 출력되지 않게 한다.
//    @Override
//    public String toString() {
//        return "Image{" +
//                "id=" + id +
//                ", caption='" + caption + '\'' +
//                ", postImgUrl='" + postImgUrl + '\'' +
//                ", user=" + user +
//                ", createDate=" + createDate +
//                '}';
//    }

}
