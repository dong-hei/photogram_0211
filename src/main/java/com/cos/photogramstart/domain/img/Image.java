package com.cos.photogramstart.domain.img;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String caption; // 내용
    private String postImgUrl; // 사진을 전송받아 그 사진을 서버 특정 폴더에 저장 - DB에 저장된 경로를 insert


    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER) // 이미지를 select하면 조인해서 User 정보를 같이 들고옴
    private User user;

    //이미지 좋아요,댓글
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likes;
    private LocalDateTime createDate;

    @Transient // DB에 컬럼이 생성되지 않는다.
    private boolean likeState;

    //댓글
    @OrderBy("id DESC")
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Comment> comments;

    @Transient // DB에 컬럼이 생성되지 않는다.
    private int likeCount;
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
