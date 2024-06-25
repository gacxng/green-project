package com.greenfinal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserSecurityService userSecurityService;
    private final UserinfoService userinfoService;
    private final GameService gameService;
    private final CharacterService charService;
    private static final String REPOSITORY_DIR = "D:\\javaStudy\\green\\src\\main\\resources\\static\\img\\myplant\\";
    private static final String REPOSITORY_DIREC = "C:\\Users\\admin\\Desktop\\강호성\\src\\main\\resources\\static\\img\\user\\";
    private final PasswordEncoder passwordEncoder;

    // 회원가입창 이동
    @GetMapping("/login/regist.do")
    public ModelAndView putUser(){
        ModelAndView mav = new ModelAndView("login/regist");
        return mav;
    }

    @PostMapping("/login/regist.do")
    public String putUser(@RequestParam("user_id") String user_id, @RequestParam("tel1") String tel1, @RequestParam("tel2") String tel2, @RequestParam("tel3") String tel3, UserinfoDto userDto, HttpSession session) {
        String tel = combineTEl(tel1, tel2, tel3);
        userDto.setTel(tel);
/*        try {
            if (file.isEmpty()) {
                String noImgName = "profile_basic.jpg";
                userDto.setUser_id(user_id);
                userDto.setUser_img(noImgName);
                userSecurityService.create(userDto);
            }
            else  {
                String imgName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                file.transferTo(new File(REPOSITORY_DIR + imgName));
                userDto.setUser_img(imgName);
                userDto.setImg_path(REPOSITORY_DIR);

            }*/
            session.setAttribute("user_id",user_id);
            userSecurityService.create(userDto);
            // 새로운 사용자를 생성하고 초기 게임 데이터를 설정
            GameDto newGame = new GameDto();
            newGame.setUser_id(userDto.getUser_id());
            newGame.setCharacter_no("0");
            newGame.setExp(0);
            newGame.setG_level(1);
            newGame.setPushTime(0);
            newGame.setWatercnt(0);
            newGame.setLovecnt(0);
            newGame.setSuncnt(0);
            newGame.setPoocnt(0);
            newGame.setBugcnt(0);
            newGame.setPotcnt(0);
            newGame.setMusiccnt(0);
            newGame.setNutritioncnt(0);

            //새로운 사용자를 게임 테이블에 추가
            gameService.addUser(newGame);

/*        }
        catch(IOException e) {
            e.printStackTrace();
        }*/
        return "redirect:/login/selectChar.do";
        //return "redirect:/";
    }

    // 로그인 창
    @GetMapping("/login/login.do")
    public String login() {
        return "login/login";
    }

    @GetMapping("/main.do")
    public ModelAndView main(@AuthenticationPrincipal User user){
        String user_id = user.getUsername();
        ModelAndView mav = new ModelAndView("main");
        if(user == null) {
           mav = new ModelAndView("login/login");
        }
        else {
           mav.addObject("nickname", userinfoService.findNickName(user_id));
           mav.addObject("user_img", userinfoService.findUserImg(user_id));
        }
        return mav;
    }
    
    /*
    @GetMapping("/mypage/user_home")
    public ModelAndView getMenu() {
        ModelAndView mav = new ModelAndView("/mypage/user_home");
        
        // 현재 인증된 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 역할 가져오기
        String userRole = authentication.getAuthorities().stream()
                              .map(GrantedAuthority::getAuthority)
                              .findFirst()
                              .orElse("ROLE_USER"); // 기본값은 'ROLE_USER'로 설정
        
        // 모델에 사용자 역할 추가
        mav.addObject("userRole", userRole);
        return mav;
    } */

    // 아이디 중복 체크
    @GetMapping("/login/checkDuplicate")
    public ResponseEntity<String> checkDuplicatedUserId(@RequestParam("user_id") String user_id){
        boolean isDuplicate = userSecurityService.isUserIdDuplicate(user_id);
        return ResponseEntity.ok(Boolean.toString(isDuplicate));
    }

    // 닉네임 중복 체크
    @GetMapping("/login/checkDuplicateNick")
    public ResponseEntity<String> duplicateNick(@RequestParam("user_nickname") String user_nickname){
        boolean isNickDuplicate = userSecurityService.isNickDuplicate(user_nickname);
        return ResponseEntity.ok(Boolean.toString(isNickDuplicate));
    }
    
    // 이메일 중복확인
    @GetMapping("/login/checkDuplicateEmail")
    public ResponseEntity<String> duplicateEmail(@RequestParam("email") String email) {
        boolean isEmailDuplicate = userSecurityService.isEmailDuplicate(email);
        return ResponseEntity.ok(Boolean.toString(isEmailDuplicate));
    }


    // 아이디찾기 이동
    @GetMapping("/login/find_id.do")
    public String findId(){
        return "login/find_id";
    }

    // 아이디찾기
    @PostMapping("/login/find_id.do")
    public ModelAndView findId(@RequestParam("user_name")String name, @RequestParam("email") String email){
        String userId = userinfoService.findUserId(name, email);
        ModelAndView mav = new ModelAndView("login/find_id_result");
        mav.addObject("userId", userId);
        return mav;
    }

    // 비밀번호찾기 이동
    @GetMapping("/login/find_pw.do")
    public String findPw(){
        return "login/find_pw";
    }
    
    @PostMapping("/login/find_pw.do")
    public String resetPassword(@RequestParam("email")String email, @RequestParam("user_name")String name, @RequestParam("user_id")String userId, Model model) {
        try {
            boolean isUserValid = userinfoService.validateUser(email, name, userId);
            if (isUserValid) {
                String newPassword = userinfoService.generateAndSendNewPassword(email);
                System.out.println("newPassword : "+newPassword);
                System.out.println("userId : "+userId);
                userinfoService.updateNewPassword(userId, newPassword);
                model.addAttribute("message","새 비밀번호가 이메일로 전송되었습니다.");
                return "/login/find_pw_result";
            } else {
                model.addAttribute("message", "일치하는 사용자 정보를 찾을 수 없습니다.");
                return "/login/find_pw_result";
            }
        } catch (Exception e) {
            model.addAttribute("message", "비밀번호 찾기 중 오류가 발생했습니다.");
            return "/login/find_pw_result";
        }
    }
/*
    // 비밀번호 찾기(유저정보확인)
    @PostMapping("/login/find_pw.do")
    public ModelAndView findPw(@RequestParam("user_name") String user_name, @RequestParam("user_id") String user_id, @RequestParam("email") String email){
        boolean findPw = userinfoService.findUserPw(user_name, user_id, email);
        ModelAndView mav = new ModelAndView("login/find_pw_result");
        mav.addObject("findPw", findPw);
        mav.addObject("user_id", user_id);
        return mav;
    }

    // 비밀번호 찾기(비밀번호 변경)
    @PostMapping("/login/chgPw.do")
    public String chgPw(@RequestParam("nextPw") String nextPw, @RequestParam("newPwChk") String nextPwChk, @RequestParam("user_id") String user_id, Model model){
    	UserinfoDto userDto = new UserinfoDto();
        boolean findPw = true;

        if(nextPw != null && !nextPw.isEmpty()) {
            if (!nextPw.equals(nextPwChk)) {
                model.addAttribute("findPw", findPw);
                model.addAttribute("errorMessage", "비밀번호확인이 일치하지 않습니다.");
                return "login/find_pw_result";
            }
            userDto.setUser_id(user_id);
            userDto.setNextPw(passwordEncoder.encode(nextPw));
        }
        try{
        	userinfoService.updatePassword(userDto);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "login/find_pw_result";
        }
        return "login/login";
    }
*/
    // 회원정보수정 페이지
    @GetMapping("/mypage/editUserInfo.do")
    public String viewInfo(Principal principal, Model model){
        String user_id = principal.getName();
        model.addAttribute("dto",userSecurityService.viewUserInfo(user_id));
        model.addAttribute("nickname", userinfoService.findNickName(user_id));
        return "mypage/edit_userinfo";
    }

    // 회원정보수정
    @PostMapping("/mypage/editUserInfo.do")
    public ModelAndView editUserInfo(Principal principal, UserinfoDto userDto, @RequestParam("tel1") String tel1, @RequestParam("tel2") String tel2, @RequestParam("tel3") String tel3){
        String user_id = principal.getName();
        String tel = combineTEl(tel1, tel2, tel3);

        ModelAndView mav = new ModelAndView("redirect:/mypage/userPostList.do");

/*        try{
            if(!file.isEmpty()) {
                String imgName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                file.transferTo(new File(REPOSITORY_DIR + imgName));
                userDto.setUser_img(imgName);
                userDto.setImg_path(REPOSITORY_DIR);
                System.out.println(userDto.getUser_img());
            }
            else {
                // 프로필 사진이 변경되지 않았을 때 기존 사진을 유지
                userDto.setUser_img(userinfoService.getUserImg(user_id));
                userDto.setImg_path(userinfoService.getImgpath(user_id));
            }
            userDto.setUser_id(user_id);
            userDto.setTel(tel);
            userinfoService.updateUserInfo(userDto);
            System.out.println(userDto);
        }
        catch(Exception e){
            e.printStackTrace();
        }*/
        userDto.setUser_id(user_id);
        userDto.setTel(tel);
        userinfoService.updateUserInfo(userDto);
        return mav;
    }

    @GetMapping("/fragments/header")
    public ModelAndView getUserInfo(Principal principal) {
        String user_id = principal.getName();
        ModelAndView mav = new ModelAndView("fragments/header");
        mav.addObject("nickname",userinfoService.findNickName(user_id));
        mav.addObject("user_img",REPOSITORY_DIR + userinfoService.findUserImg(user_id));
        System.out.println(mav);
        return mav;
    }

    // 비밀번호변경 창
    @RequestMapping("/mypage/chgPw.do")
    public String chgPwPop(){
        return "mypage/chgPwForm";
    }

    // 비밀번호 변경
    @PostMapping("/mypage/changePw.do")
    public String changePw(Principal principal,@RequestParam("prePw") String prePw,@RequestParam("nextPw") String nextPw,@RequestParam("nextPwChk") String nextPwChk, Model model){
        String user_id = principal.getName();
        UserinfoDto userDto = new UserinfoDto();

        // 현재 비밀번호 확인
        if(!passwordEncoder.matches(prePw, userSecurityService.selectPw(user_id))){
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "mypage/chgPwForm";

        }

        // 새 비밀번호와 비밀번호 확인 일치 여부 확인
        if(nextPw != null && !nextPw.isEmpty()){
            if(!nextPw.equals(nextPwChk)){
                model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
                return "mypage/chgPwForm";
            }
            userDto.setUser_id(user_id);
            userDto.setNextPw(passwordEncoder.encode(nextPw));
        }
        try{
        	userinfoService.updatePassword(userDto);
        }
        catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "mypage/chgPwForm";
        }
        return "mypage/closePopup";
    }

    // 회원탈퇴 이동
    @GetMapping("/mypage/deleteUser.do")
    public String deleteUser(){
        return "mypage/deleteUser";
    }

    // 회원탈퇴
    @PostMapping("/mypage/delUser.do")
    public String exitUser(Principal principal,@RequestParam("user_id") String user_id, @RequestParam("password") String password,Model model){
    	UserinfoDto userDto = new UserinfoDto();
        String dbUser_id = principal.getName();
        if (!dbUser_id.equals(user_id) || !passwordEncoder.matches(password, userSecurityService.selectPw(dbUser_id))){
            model.addAttribute("errorMessage", "아이디혹은 비밀번호가 일치하지 않습니다.");
            return "mypage/deleteUser";
        }

        try {
        	userinfoService.dropUser(user_id);
            SecurityContextHolder.clearContext();
        }
        catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "mypage/delUser";
        }
        return "redirect:/logout.do";
    }
    
    @GetMapping("/login/selectChar.do")
    public ModelAndView selectChar(@SessionAttribute("user_id") String user_id) {
        ModelAndView mav = new ModelAndView("registSelect/choose");
        List<CharacterDto> charList = charService.getAllChar();
        mav.addObject("user_id", user_id);
        mav.addObject("charList", charList);
        return mav;
    }

    @PostMapping("/login/insertChar.do")
    public ModelAndView insetCharacter(@RequestParam("user_id") String user_id, @RequestParam("character_no")int char_no){
        ModelAndView mav = new ModelAndView("registSelect/selectResult");
        CharacterDto charDto = new CharacterDto();
        charDto.setCharacter_no(char_no);
        charDto.setUser_id(user_id);
        charService.insertChar(charDto);
        gameService.updateChar(charDto);
        List<CharacterDto> characterDto = charService.getSelectChar(char_no);
        mav.addObject("charDto", characterDto);
        return mav;
    }

    @GetMapping("/login/CharResult.do")
    public ModelAndView CharResult(@ModelAttribute("char_no") int char_no){
        ModelAndView mav = new ModelAndView("login/index");
        List<CharacterDto> charDto = charService.getSelectChar(char_no);
        mav.addObject("charDto", charDto);
        return mav;
    }

    public String combineTEl(String tel1, String tel2, String tel3){
        return tel1 + "-" + tel2 + "-" + tel3;
    }
    
    
    
 // 캐릭터 닉네임 추가 : 06-06
    @PostMapping("/login/putCharacterNickname.do")
    public String putUserCharacterNickName(@SessionAttribute("user_id") String user_id,
                                           @RequestParam("character_nickname") String nick,
                                           @RequestParam("character_no") int char_no,
                                           RedirectAttributes redirectAttributes) {
        userinfoService.putCharacterNickname(nick, user_id);
        System.out.println("char_no: " + char_no);
        redirectAttributes.addAttribute("char_no", char_no); // 리다이렉트 시 파라미터 추가
        return "redirect:/registSelect/selectResult";
    }

    @GetMapping("/registSelect/selectResult")
    public ModelAndView selectResult(@SessionAttribute("user_id") String user_id,
                                     @RequestParam("char_no") int char_no) {
        String userCharacterNick = userinfoService.getUserCharacterByUserId(user_id);
        ModelAndView mav = new ModelAndView("registSelect/selectResult");
        List<CharacterDto> charDto = charService.getSelectChar(char_no);
        System.out.println("userCharacterNick: " + userCharacterNick);
        mav.addObject("userCharacterNick", userCharacterNick);
        mav.addObject("charDto", charDto);
        return mav;
    }
    
}


