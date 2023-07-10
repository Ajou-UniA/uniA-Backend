package com.example.unia.member.service;

import com.example.unia.member.config.UserCustom;
import com.example.unia.member.dto.MemberUpdateDTO;
import com.example.unia.member.entity.Role;
import com.example.unia.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import com.example.unia.member.entity.MemberEntity;
import com.example.unia.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Transactional
    public void create(MemberDTO memberDTO) {
        //시큐리티 비교에 암호화된 비밀번호가 필요하여 회원가입시 암호화하여 저장(보안+기능구현을 위함)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }
    public boolean checkEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        return optionalMemberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public MemberDTO findByMemberEmail(String memberEmail){
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(memberEmail);
        return memberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    @Transactional
    public MemberDTO updateinfo(MemberUpdateDTO updateDTO,Long id) {
        // update라는 메소드가 따로 없기 때문에, save 메소드 사용
        Optional<MemberEntity> byMemberId = memberRepository.findById(id);
        updateDTO.toMemberDTO(byMemberId.get(),updateDTO);
        MemberDTO memberDTO = MemberDTO.toMemberDTO(byMemberId.get());
        return memberDTO;
    }
    @Transactional
    public void update(MemberDTO memberDTO) {
        // update라는 메소드가 따로 없기 때문에, save 메소드 사용
        memberRepository.save(MemberEntity.toMemberEntity(memberDTO));
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional
    public MemberDTO updateNickname(Long memberId, String newNickName){
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(memberId);

        if (memberEntityOptional.isEmpty()) {
            return null;
        }

        MemberEntity memberEntity = memberEntityOptional.get();
        memberEntity.setNickname(newNickName);
        return MemberDTO.toMemberDTO(memberRepository.save(memberEntity));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> findName = memberRepository.findByMemberEmail(username);
        if(findName == null){
            throw new UsernameNotFoundException(username);
        }
        MemberEntity member = findName.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));

        UserCustom userCustom = new UserCustom(member.getMemberEmail(),member.getMemberPassword(), authorities, member.getMemberId());

        return userCustom;

    }

}
