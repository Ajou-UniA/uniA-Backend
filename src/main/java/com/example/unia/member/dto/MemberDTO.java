package com.example.unia.member.dto;

import com.example.unia.member.entity.MemberEntity;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Objects;

@Data
public class MemberDTO {

    @NotBlank(message = "Please enter your first name")
    private String firstName; // 이름

    @NotBlank(message = "Please enter your last name")
    private String lastName; // 성

    @NotNull(message = "Please enter your student ID")
    private Long memberId; // 학번

    @NotBlank(message = "Please enter your major")
    private String memberMajor; // 학과

    @NotBlank(message = "Please enter your email")
    @Pattern(regexp = "[a-zA-Z0-9._%+-]+@ajou.ac.kr$", message = "Email format is incorrect")
    private String memberEmail; // 이메일

    @NotBlank(message = "Please enter password")
    @Size(min = 8, max = 12, message = "Password must be between 8 and 12 characters")
    private String memberPassword; // 비밀번호

    @NotBlank(message = "Please confirm your password")
    private String memberConfirmPassword; // 비밀번호 확인

    @NotBlank(message = "Please enter your nickname")
    private String nickname; // 닉네임

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setLastName(memberEntity.getLastName());
        memberDTO.setFirstName(memberEntity.getFirstName());
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberMajor(memberEntity.getMemberMajor());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setNickname(memberEntity.getNickname()); // 닉네임 추가
        return memberDTO;
    }

    public MemberInfoDTO toMemberInfoDTO() {
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
        memberInfoDTO.setFirstName(this.firstName);
        memberInfoDTO.setLastName(this.lastName);
        memberInfoDTO.setMemberId(this.memberId);
        memberInfoDTO.setMemberMajor(this.memberMajor);
        memberInfoDTO.setMemberEmail(this.memberEmail);
        memberInfoDTO.setNickname(this.nickname); // 닉네임 추가
        return memberInfoDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO memberDTO = (MemberDTO) o;
        return memberPassword.equals(memberDTO.memberPassword) && memberConfirmPassword.equals(memberDTO.memberConfirmPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberPassword, memberConfirmPassword);
    }
}

