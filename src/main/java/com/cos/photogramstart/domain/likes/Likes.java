package com.cos.photogramstart.domain.likes;

import com.cos.photogramstart.domain.img.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(
        uniqueConstraints =
        @UniqueConstraint(
                name = "likes_uk",
                columnNames = {"imageId", "userId"}
        ))
public class Likes {
    //Like가 키워드기때문에 테이블이 안만들어짐 그래서 Likes라고 네이밍함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "imageId")
    private Image image; // img 1 : 좋아요 여러개가능

    @JsonIgnoreProperties({"images"})
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; // user 1 : 좋아요 여러개

    private LocalDateTime createDate;



    @PrePersist //DB에 INSERT 되기 직전에 실행
    public void createDate(){
        this.createDate = LocalDateTime.now();
    }

}
