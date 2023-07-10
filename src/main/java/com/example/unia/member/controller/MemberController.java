package com.example.unia.member.controller;

import com.example.unia.member.dto.MemberDTO;
import com.example.unia.member.dto.MemberInfoDTO;
import com.example.unia.member.dto.MemberUpdateDTO;
import com.example.unia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;


    /**
     * Login 실패 (test)
     * [GET] /api/v1/member/login/fail
     *
     * @return ResponseEntity
     */
    @GetMapping("/login/fail")
    public ResponseEntity test1() {
        Map<String, Object> map = new HashMap<>();
        map.put("result", 0);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * Login 성공 (test)
     * [GET] /api/v1/member/login/success
     *
     * @return ResponseEntity
     */
    @GetMapping("/login/success")
    public ResponseEntity test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        return new ResponseEntity(map, HttpStatus.OK);
    }


    /**
     * 마이페이지 조회
     * [GET] /api/v1/member/{memberId}
     *
     * @param memberId 학번
     * @return ResponseEntity body(memberInfoDTO)
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> findByMemberId(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.findById(memberId);
        if (memberDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member does not exist");
        }

        MemberInfoDTO memberInfoDTO = memberDTO.toMemberInfoDTO();
        return ResponseEntity.status(HttpStatus.OK).body(memberInfoDTO);
    }

    /**
     * 개인정보 변경
     * [PATCH] /api/v1/member/update/{memberId}
     *
     * @param memberId
     * @param Dto
     * @return ResponseEntity<MemberDTO>
     */
    @PatchMapping("/update/{memberId}")
    public ResponseEntity<MemberDTO> updateMemberDto(@PathVariable Long memberId, @RequestBody MemberUpdateDTO Dto) {
        MemberDTO findMember = memberService.findById(memberId);
        if (findMember == null || Dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        MemberDTO updateinfo = memberService.updateinfo(Dto, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(updateinfo);
    }

    /**
     * 비밀번호 변경
     * [PATCH] /api/v1/member/{memberId}
     *
     * @param memberId 학번
     * @param password 비밀번호
     * @return ResponseEntity body(existMember)
     */
    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberDTO> updatePassword(@PathVariable Long memberId, @RequestParam("newPassword") String password) {


        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        MemberDTO existMember = memberService.findById(memberId);

        if (existMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        existMember.setMemberPassword(passwordEncoder.encode(password));

        memberService.update(existMember);
        return ResponseEntity.status(HttpStatus.OK).body(existMember);

    }


    /**
     * 회원 탈퇴
     * [DELETE] /api/v1/member/{memberId}
     *
     * @param memberId 학번
     * @return ResponseEntity body("Delete Success")
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteById(@PathVariable Long memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete Success");
    }

    /**
     * 닉네임 변경
     * [PATCH] /api/v1/member/{memberId}/nickname
     *
     * @param memberId 학번
     * @return ResponseEntity body(updatedMember)
     */
    @PatchMapping("/{memberId}/nickname")
    public ResponseEntity<MemberDTO> updateNickname(@PathVariable Long memberId, @RequestParam("newNickname") String newNickname) {

        if (newNickname == null || newNickname.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        MemberDTO updatedMember = memberService.updateNickname(memberId, newNickname);


        if (updatedMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);

    }
}