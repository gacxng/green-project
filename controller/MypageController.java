package com.greenfinal.controller;

import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MypageController {
    private final PostService postService;
    private final ReplyService replyService;
    private final UserinfoService userinfoService;
    
    // 내가 쓴 글
    @GetMapping("/mypage/userPostList.do")
    public ModelAndView managePost(Principal principal){
        ModelAndView mav = new ModelAndView("mypage/userPostList");
        String user_id = principal.getName();
        List<PostDto> postlist = postService.getPostList(user_id);
        List<PostDto> boardName = postService.getBoardName();
        List<BoardDto> boardList = postService.getBoardList();
        mav.addObject("nickname", userinfoService.findNickName(user_id));
        
        if(postlist == null || postlist.isEmpty()){
            mav.addObject("noResults", true);
        }
        else {
            mav.addObject("user_img", userinfoService.findUserImg(user_id));
            mav.addObject("postList", postlist);
            mav.addObject("boardName", boardName);
            mav.addObject("boardList", boardList);
        }
        return mav;
    }

    // 마이페이지 게시물 검색
    @GetMapping("/mypage/postSearch.do")
    public ModelAndView searchPost(Principal principal, SearchDto searchDto){
        String user_id = principal.getName();
        List<PostDto> postDto = postService.getPostList(user_id);
        ModelAndView mav = new ModelAndView("mypage/userPostList");
        mav.addObject("nickname", userinfoService.findNickName(user_id));
        
        if("1".equals(searchDto.getSearchType())){
            postDto = postService.getPostList1ByName(user_id,searchDto.getKeyword());
        }
        else if("2".equals(searchDto.getSearchType())){
            postDto = postService.getPostList2ByName(user_id, searchDto.getKeyword());
        }
        else {
            postDto = postService.getPostListByName(user_id, searchDto.getKeyword());
        }
        if(postDto == null || postDto.isEmpty()){
            mav.addObject("noResults", true);
        }
        else {
            List<PostDto> boardName = postService.getBoardName();
            List<BoardDto> boardList = postService.getBoardList();
            
            mav.addObject("user_img", userinfoService.findUserImg(user_id));
            mav.addObject("boardName", boardName);
            mav.addObject("postList", postDto);
            mav.addObject("boardList", boardList);
        }
        return mav;
    }

    // 마이페이지 게시물 삭제
    @GetMapping("/mypage/deletePost.do")
    public ModelAndView deletePost(@RequestParam("postId") List<Integer> postId, Principal principal){
        PostDto postDto = new PostDto();
        String user_id = principal.getName();
        ModelAndView mav = new ModelAndView("mypage/userPostList");
        for(int i=0;i<postId.size();i++){
            postService.deletePost(postId.get(i));
            postService.deletePostReply(postId.get(i));
        }
        // 삭제 후 리스트 보여주기
        List<PostDto> postlist = postService.getPostList(user_id);
        List<PostDto> boardName = postService.getBoardName();
        mav.addObject("nickname", userinfoService.findNickName(user_id));
        mav.addObject("user_img", userinfoService.findUserImg(user_id));
        mav.addObject("postList", postlist);
        mav.addObject("boardName", boardName);
        return mav;
    }

    // 마이페이지 전체 댓글
    @GetMapping("/mypage/userReplyList.do")
    public ModelAndView manageReply(Principal principal){
        ModelAndView mav = new ModelAndView("mypage/userReplyList");
        String user_id = principal.getName();
        List<ReplyDto> replyList = replyService.getReplyList(user_id);
        List<BoardDto> boardList = postService.getBoardList();
        mav.addObject("nickname", userinfoService.findNickName(user_id));
        
        if(replyList == null || replyList.isEmpty()){
            mav.addObject("noResults", true);
        }
        else {
            mav.addObject("user_img", userinfoService.findUserImg(user_id));
            mav.addObject("replyList", replyList);
            mav.addObject("boardList", boardList);
        }
        return mav;
    }

    // 마이페이지 댓글 검색
    @GetMapping("/mypage/replySearch.do")
    public ModelAndView searchReply(Principal principal, SearchDto searchDto){
        String user_id = principal.getName();
        List<ReplyDto> replyDto;
        replyDto = replyService.getReplyList(user_id);
        ModelAndView mav = new ModelAndView("mypage/userReplyList");
        mav.addObject("nickname", userinfoService.findNickName(user_id));

        if("1".equals(searchDto.getSearchType())){
            replyDto = replyService.getReplyList1ByName(user_id,searchDto.getKeyword());
        }
        else if("2".equals(searchDto.getSearchType())){
            replyDto = replyService.getReplyList2ByName(user_id, searchDto.getKeyword());
        }
        else {
            replyDto = replyService.getReplyListByName(user_id, searchDto.getKeyword());
        }

        if(replyDto == null || replyDto.isEmpty()){
            mav.addObject("noResults", true);
        }
        else {
            List<PostDto> boardName = postService.getBoardName();
            List<BoardDto> boardList = postService.getBoardList();
            mav.addObject("user_img", userinfoService.findUserImg(user_id));
            mav.addObject("boardName", boardName);
            mav.addObject("replyList", replyDto);
            mav.addObject("boardList", boardList);
        }
        return mav;
    }

    @GetMapping("/mypage/deleteReply.do")
    public ModelAndView deleteReply(@RequestParam("replyId") List<Integer> replyId, Principal principal){
        ReplyDto replyDto = new ReplyDto();
        String user_id = principal.getName();
        ModelAndView mav = new ModelAndView("mypage/userReplyList");
        for(int i=0;i<replyId.size();i++){
            replyService.deleteReply(replyId.get(i));
        }
        List<ReplyDto> replylist = replyService.getReplyList(user_id);
        mav.addObject("nickname", userinfoService.findNickName(user_id));
        mav.addObject("user_img", userinfoService.findUserImg(user_id));
        mav.addObject("replyList", replylist);
        return mav;
    }

    @PostMapping("/mypage/deletePost.do")
    @ResponseBody
    public ResponseEntity<?> deletePosts(@RequestBody Map<String, List<String>> request) {
        List<String> postNosStr = request.get("postNos"); // 문자열로 받은 user_no 목록

        if (postNosStr == null || postNosStr.isEmpty()) {
            return ResponseEntity.badRequest().body("No post numbers provided");
        }

        // 문자열 리스트를 정수 리스트로 변환
        List<Integer> postNos = postNosStr.stream()
                .map(Integer::parseInt) // 문자열을 정수로 변환
                .collect(Collectors.toList());

        postService.deletePosts(postNos); // 서비스 클래스를 통해 사용자 삭제

        return ResponseEntity.ok("Posts deleted successfully");
    }
    

    @PostMapping("/mypage/deleteReply.do")
    public ResponseEntity<?> deleteReplies(@RequestBody DeleteReplyRequest deleteReplyRequest) {
        try {
            replyService.deleteReplies(deleteReplyRequest.getReplyNos());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    public static class DeleteReplyRequest {
        private List<Long> replyNos;

        public List<Long> getReplyNos() {
            return replyNos;
        }

        public void setReplyNos(List<Long> replyNos) {
            this.replyNos = replyNos;
        }
    }
}
