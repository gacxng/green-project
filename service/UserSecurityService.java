package com.greenfinal.service;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserinfoDao dao;
    private final PasswordEncoder passwordEncoder;

    public UserinfoDto create(UserinfoDto dto){
    	UserinfoDto user = new UserinfoDto();
        user.setUser_id(dto.getUser_id());
        user.setUser_pw(passwordEncoder.encode(dto.getUser_pw()));
        user.setUser_name(dto.getUser_name());
        user.setUser_nickname(dto.getUser_nickname());
        user.setTel(dto.getTel());
        user.setEmail(dto.getEmail());
        user.setUser_img(dto.getUser_img());
        user.setImg_path(dto.getImg_path());
        this.dao.insertUser(user);

        return user;
    }

    // 아이디 찾기
    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException{
    	UserinfoDto user = this.dao.getByUserId(user_id);
        if(user == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(user_id)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }
        else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(user.getUser_id(), user.getUser_pw(), authorities);
    }

    // 회원 정보
    public UserinfoDto viewUserInfo(String user_id){
        return dao.viewUserInfo(user_id);
    }

    public String selectPw(String user_id){
        return dao.selectPw(user_id);
    }

    // 아이디 중복확인
    public boolean isUserIdDuplicate(String user_id) {
        return dao.countByUserId(user_id) == 0;
    }

    public boolean isNickDuplicate(String user_nickname){
        return dao.countByNickName(user_nickname) == 0;
    }

    // 이메일 중복확인
    public boolean isEmailDuplicate(String email){
        return dao.countByEmail(email) == 0;
    }
    
}
