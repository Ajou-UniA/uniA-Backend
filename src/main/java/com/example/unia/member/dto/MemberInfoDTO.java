package com.example.unia.member.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class MemberInfoDTO {

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

    @NotBlank(message = "Please enter your nickname")
    private String nickname; // 닉네임
}
