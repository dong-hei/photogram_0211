package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service // IoC
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //POST 로그인 요청 {httpbody = username,password} => IoC컨테이너에 UserDetailsService가 낚아챈다 -> 로그인 진행
    //그러나 PrincipalDetailsService가 UserDetailsService를 대체한다.

    // pw는 알아서 체킹 -> 리턴이 잘되면 자동으로 UserDetails 세션으로 만든다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);

        User userEntity = userRepository.findByUsername(username);

        System.out.println("userEntity = " + userEntity);
        if (userEntity == null) {
            return null;
        }else{
            return new PrincipalDetails(userEntity); // 세션에 저장될때 userObject를 활용 가능
        }

    }
}
