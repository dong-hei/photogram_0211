package com.cos.photogramstart.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CMResDto<T> {
    
    private int code; //1은 성공 -1은 실패
    private String message;
    private T data; // 전역으로 사용할거라 제네릭 리턴타입



}
