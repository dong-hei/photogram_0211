package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private final long serialVersionUID=1L;

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collector = new ArrayList<>();
        collector.add(() -> { return user.getRole();});
        return collector;
        //권한 가져오는 함수, 왜 컬렉션인가? 권한이 한개가 아닐수도 있기 때문
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //만료 안되었는지 확인
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //계정 잠긴지 확인
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //비밀번호 만기 확인
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 확인
    }
}
