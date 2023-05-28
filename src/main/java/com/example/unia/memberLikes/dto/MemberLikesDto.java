package com.example.unia.memberLikes.dto;

import lombok.Data;

@Data
public class MemberLikesDto {
    private Long memberLikesId;
    private Long memberId;
    private Long placeId;
}