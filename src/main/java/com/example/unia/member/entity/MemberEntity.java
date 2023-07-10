package com.example.unia.member.entity;


import com.example.unia.assignment.entity.AssignmentEntity;
import com.example.unia.member.dto.MemberDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_table")
public class MemberEntity {

    @Column(nullable = false)
    private String firstName; // 이름

    @Column(nullable = false)
    private String lastName; // 성

    @Id
    private Long memberId; // 학번

    @Column(nullable = false)
    private String memberMajor; // 학과

    @Column(nullable = false, unique = true)
    private String memberEmail; // 이메일

    @Column(nullable = false)
    private String memberPassword; // 비밀번호

    @Column(nullable = false)
    private String nickname; // 닉네임

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<AssignmentEntity> assignmentList = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setFirstName(memberDTO.getFirstName());
        memberEntity.setLastName(memberDTO.getLastName());
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberMajor(memberDTO.getMemberMajor());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setNickname(memberDTO.getNickname()); // 닉네임 추가
        return memberEntity;
    }

}