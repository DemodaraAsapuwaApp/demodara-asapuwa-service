package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.dao.MemberEntity;
import com.example.demodaraasapuwaservice.dto.MemberDto;
import com.example.demodaraasapuwaservice.mapper.MemberMapper;
import com.example.demodaraasapuwaservice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper mapper) {
        this.memberRepository = memberRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<List<MemberDto>> getMembers() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        if (memberEntityList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.mapLDaoToLDto(memberEntityList));
    }

    public ResponseEntity<MemberDto> getMember(Integer id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.mapDaoToDto(byId.get()));
    }

    public ResponseEntity modifyMember(Integer id, MemberDto resource) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        MemberEntity memberEntity = mapper.modDtoToDao(resource, byId.get());
        memberRepository.save(memberEntity);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity addMember(MemberDto resource) {
        //is there already a member with same name?
        if (memberRepository.findByName(resource.getName()).isPresent()) {
            List<String> errors = new ArrayList<>();
            errors.add("Adding Request Rejected. Member with name: " + resource.getName() + " already available on system.");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        MemberEntity memberEntity = mapper.mapDtoToDao(resource);
        memberRepository.save(memberEntity);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteMember(Integer id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (!byId.isPresent()) {
            List<String> errors = new ArrayList<>();
            errors.add("Delete Request Rejected. Member with id: " + id + " not available on system.");
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
        memberRepository.delete(byId.get());
        return ResponseEntity.ok().build();
    }
}