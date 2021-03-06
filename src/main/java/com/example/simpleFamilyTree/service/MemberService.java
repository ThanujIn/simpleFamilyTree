package com.example.simpleFamilyTree.service;

import com.example.simpleFamilyTree.dto.MemberDTO;
import com.example.simpleFamilyTree.util.MemberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private static MemberUtil memberUtil;

    private static List<MemberDTO> loadMembersFromDb(){
        List<MemberDTO> membersList = new ArrayList<>();
        MemberDTO initialMother = MemberUtil.createMember("1", "Ada", "83",null, null, "Female");
        MemberDTO initialFather = MemberUtil.createMember("2", "Albert", "86",null, null,  "Male");
        membersList.add(initialMother);
        membersList.add(initialFather);
        return membersList;
    }

    private List<MemberDTO> members = loadMembersFromDb();

    public Map<String, MemberDTO> loadMembersToMap(){
        Map<String, MemberDTO> membersList = new HashMap<>();
        for(MemberDTO member : members){
            membersList.put(member.getId(), member);
        }
        return membersList;
    }

    List<MemberDTO> getMembers(){
        return members;
    }

    public String addMember(MemberDTO member){
        Map<String, MemberDTO> membersMap = loadMembersToMap();
        if(membersMap.get(member.getId()) == null){
            if(member.getFather() != null && member.getMother() != null && membersMap.get(member.getFather().getId()) != null && membersMap.get(member.getMother().getId()) != null && membersMap.get(member.getFather().getId()).getGender().equalsIgnoreCase("Male") && membersMap.get(member.getMother().getId()).getGender().equalsIgnoreCase("Female")){
                members.add(member);
                updateChildDetails(member);
                return "Member Added Successfully";
            }
            return "Invalid Parent Details";
        }else{
            return "Member ID already exists";
        }
    }

    private void updateChildDetails(MemberDTO member){

        for(MemberDTO members : members){
            if(members.getId().equalsIgnoreCase(member.getMother().getId()) || members.getId().equalsIgnoreCase(member.getFather().getId())){
                if(members.getChildren() == null){
                    List<MemberDTO> children = new ArrayList<>();
                    children.add(member);
                    members.setChildren(children);
                }else {
                    member.getChildren().add(member);
                }

            }
        }
    }

    public MemberDTO getMemberById(String memberId){
        MemberDTO memberDTO = new MemberDTO();
        boolean found = false;
        for(MemberDTO member : members){
            if(member.getId().equals(memberId)){
                memberDTO = member;
                found = true;
            }
        }
        return found ? memberDTO : null;
    }

    public List<MemberDTO> findAll(){
        return members;
    }
}
