package com.example.unia.memberLikes.repository;

import com.example.unia.memberLikes.entity.MemberLikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLikesRepository extends JpaRepository<MemberLikesEntity, Long> {
    MemberLikesEntity findByMemberIdAndPlaceId(Long memberId, Long id);
}
