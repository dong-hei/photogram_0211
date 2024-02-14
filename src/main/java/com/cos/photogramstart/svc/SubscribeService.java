package com.cos.photogramstart.svc;

import com.cos.photogramstart.domain.subscribe.SubscribeRepo;
import com.cos.photogramstart.handler.ex.CustomApiEx;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepo subscribeRepo;
    private final EntityManager em; //Repository는 EntityManager를 구현해서 만들어져 있는 구현체


    @Transactional(readOnly = true)
    public List<SubscribeDto> subscribeList(int principalId, int pageUserId){

        //쿼리 준비
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT u.id, u.username, u.profileImgUrl,");
        sb.append("if ((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
        sb.append("if ((?=u.id), 1, 0 ) equalUserState ");
        sb.append("FROM user u INNER JOIN subscribe s ");
        sb.append("ON u.id = s.toUserId ");
        sb.append("WHERE s.fromUserId = ?");

        // 첫번째 물음표 : Principal
        // if (?=u.id) : Principal
        // 마지막 물음표 : pageUserId

        //쿼리 완성
        Query query = em.createNativeQuery(sb.toString())
                        .setParameter(1,principalId)
                        .setParameter(2,principalId)
                        .setParameter(3,pageUserId);

        //쿼리 실행 - QLRM, DB에서 result된 결과를 자바 클래스에 매핑해주는 라이브러리
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDto> SubscribeDtos = result.list(query, SubscribeDto.class);
        return SubscribeDtos;
    }

    public void subscribe(int fromUserId, int toUserId){
        try {
            subscribeRepo.mSubscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiEx("이미 구독을 했습니다.");
        }
    }

    public void unSubscribe(int fromUserId, int toUserId){
        subscribeRepo.mUnSubscribe(fromUserId, toUserId);
    }
}
