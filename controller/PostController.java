package com.greenfinal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfinal.dto.*;
import com.greenfinal.service.PostService;
import com.greenfinal.service.ReplyService;
import com.greenfinal.service.UserinfoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {
   private static final String REPOSITORY_DIR = "C:\\Users\\admin\\Desktop\\green_final\\src\\main\\resources\\static\\img\\post\\";
   private final PostService postService;
   private final ReplyService replyService;
   private final UserinfoService userinfoService;
   

   /* 글 목록_알쓸식잡 */
   @GetMapping("/community/article")
   public ModelAndView getAllPost1(@AuthenticationPrincipal User user) {
     int article = 1;
      
     BoardDto board = postService.selectBoardDto(article);
     String board_name1 = board.getBoard_name();
     
     
      List<PostDto> postTotList = postService.TotalPostList(article);

      // 게시판에 맞는지 판별
      boolean pb_status = true;
      boolean gd_status = false;
      ModelAndView mav = new ModelAndView("/community/community_home");
      if (user != null) {
         String id = user.getUsername();
         System.out.println(id);
         mav.addObject("nickname", userinfoService.findNickName(id));
         System.out.println(userinfoService.findNickName(id));
      }
      mav.addObject("board_name1", board_name1);
      mav.addObject("postTotList", postTotList);
      mav.addObject("pb_status", pb_status);
      mav.addObject("gd_status", gd_status);

      return mav;
   }

   /* 글 목록_가드너 다이어리 */
   @GetMapping("/community/gardenerDiary")
   public ModelAndView getAllPost2(@AuthenticationPrincipal User user) {
      int gardener = 2;
      
      BoardDto board = postService.selectBoardDto(gardener);
      String board_name2 = board.getBoard_name();
      
      List<PostDto> postTotList = postService.TotalPostList(gardener);
      
      // 게시판에 맞는지 판별
      boolean pb_status = false;
      boolean gd_status = true;
      ModelAndView mav = new ModelAndView("/community/community_home");
      if (user != null) {
         String id = user.getUsername();
         System.out.println(id);
         mav.addObject("nickname", userinfoService.findNickName(id));
         System.out.println(userinfoService.findNickName(id));
      }
      mav.addObject("board_name2", board_name2);
      mav.addObject("postTotList", postTotList);
      mav.addObject("pb_status", pb_status);
      mav.addObject("gd_status", gd_status);
      return mav;
   } 

   /* 검색 기능 구현을 위한 키워드 가져오기_알쓸식잡 */
   @GetMapping("/search1.do")
   public ModelAndView search1(SearchDto dto) {
      int article = 1;
      List<PostDto> postTotList;

      postTotList = postService.TotalPostSearchList(article, dto.getKeyword());

      boolean pb_status = true;
      boolean gd_status = false;
      ModelAndView mav = new ModelAndView("/community/community_home");
      mav.addObject("postTotList", postTotList);
      mav.addObject("pb_status", pb_status);
      mav.addObject("gd_status", gd_status);
      
      if (postTotList == null || postTotList.isEmpty()) {
         mav.addObject("pb_status", pb_status);
          mav.addObject("gd_status", gd_status);
          mav.addObject("noResult", true);
      }

      return mav;
   }

   /* 검색 기능 구현을 위한 키워드 가져오기_가드너 다이어리 */
   @GetMapping("/search2.do")
   public ModelAndView search2(SearchDto dto) {
      int gardener = 2;
      List<PostDto> postTotList;

      postTotList = postService.TotalPostSearchList(gardener, dto.getKeyword());

      boolean pb_status = false;
      boolean gd_status = true;
      ModelAndView mav = new ModelAndView("/community/community_home");
      mav.addObject("postTotList", postTotList);
      mav.addObject("pb_status", pb_status);
      mav.addObject("gd_status", gd_status);

      if (postTotList == null || postTotList.isEmpty()) {
         mav.addObject("pb_status", pb_status);
          mav.addObject("gd_status", gd_status);
          mav.addObject("noResult", true);
      }
      return mav;
   }

   /* writePost 글 작성_가드너 다이어리 */
   @GetMapping("/postForm.do")
   @ResponseBody
   public ResponseEntity<String> writePostForm(Principal principal) {

      String user_id = principal.getName();
      // 글 작성_게시판 번호 자동 불러오기
      int nextNo = postService.getNextNo();

      String send = "{\"nextNo\":" + nextNo + ", \"user_id\":\"" + user_id + "\"}";
      System.out.println("send : " + send);

      return ResponseEntity.ok().body(send);
   }

   @PostMapping("/community/write.do")
   public ModelAndView writePost(PostDto postDto, @RequestParam("post_img") MultipartFile[] files,
         Principal principal) {

      ModelAndView mav = new ModelAndView("redirect:/community/gardenerDiary");

      try {
         mav = new ModelAndView("redirect:/community/gardenerDiary");
         int nextNo = postService.getNextNo();
         String user_id = principal.getName();

         List<Integer> imgIds = new ArrayList<>();

         for (MultipartFile file : files) {
            FileDto fileDto = new FileDto();
            String convertedImgName;

            if (file.isEmpty() || file == null) {
               convertedImgName = "no-img.png";
               fileDto.setFile_name(convertedImgName);
               System.out.println("NO FILE");
            } else {
               convertedImgName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
               file.transferTo(new File(REPOSITORY_DIR + convertedImgName));
            
            fileDto.setUser_id(user_id);
            fileDto.setFile_name(convertedImgName);
            fileDto.setFile_path(REPOSITORY_DIR);
            fileDto.setOrg_file_name(file.getOriginalFilename());
            fileDto.setPost_no(nextNo);

            int img_id = postService.savePostImg(fileDto);

            imgIds.add(img_id); // 이미지의 img_id를 리스트에 추가
            }
         }
         postDto.setPost_no(nextNo);
         postDto.setUser_id(user_id);

         if (!imgIds.isEmpty()) {
            postDto.setImg_id(imgIds.get(0));
         }
         postService.writePost(postDto);

      } catch (IOException e) {
         e.printStackTrace();
      }

      return mav;
   }
   
   /* postView 글 상세페이지_가드너 다이어리 (post_no에 따른 해당 게시글 불러오기)*/
   @GetMapping("/community/postView/{post_no}")
   public String detailPost(@PathVariable("post_no") int post_no, Model model, Principal principal,
      RedirectAttributes redirectAttributes, HttpServletRequest request) {
	  PostDto Nickname = postService.TotalPostListFornickname(post_no);
      String nickName = Nickname.getUser_nickname();
      String userId = principal.getName(); // 로그인한 유저 아이디 불러오기(임시 - 닉네임으로 바꾸기)
      
      postService.hitCount(post_no);
      PostDto postDto = postService.findPostByPno(post_no); // post_no로 게시판 불러오기
      String loginUserNick = userinfoService.findNickName(userId);
      
      List<FileDto> fileDtos = postService.findPostImgBypno(post_no);
      List<ReplyDto> replyDto = replyService.getReplyListByPno(post_no);
      
      System.out.println(post_no);
      System.out.println("replyDto =================="+replyDto);
      System.out.println(replyDto.size());

      for (FileDto fileDto : fileDtos) {
         String filePath = fileDto.getFile_path();
         if (filePath != null) {
            fileDto.setFile_path(filePath.replace("\\", "/"));
         }
      }

      
      int nextNo = replyService.getNextNo(); // insert를 위한 댓글 번호 자동 불러오기
      int likeCnt = postService.countLike(post_no);
      
      model.addAttribute("loginUserNick", loginUserNick);
      model.addAttribute("nickName", nickName);
      model.addAttribute("fileDto", fileDtos);
      model.addAttribute("likeCnt", likeCnt);
      model.addAttribute("postDto", postDto); // 게시글 불러오기
      model.addAttribute("replyDto", replyDto); // 댓글 목록 불러오기
      model.addAttribute("userId", userId); // 로그인한 아이디 댓글쓰는 사람 id에 뿌려주기
      model.addAttribute("nextNo", nextNo);
      model.addAttribute("nickname", userinfoService.findNickName(userId));

      LikeDto list = postService.getAllLike(post_no, userId);
      if (list == null) {
         model.addAttribute("likeStatus", false);
      } else {
         model.addAttribute("likeStatus", true);
      }

      return "community/postView";
   }

   @GetMapping("/imgFile/{img_id}")
   public ResponseEntity<Resource> downloadFile(@PathVariable("img_id") int imgId) {
      try {
         FileDto fileDto = new FileDto();

         fileDto = postService.getFileNameByid(imgId);
         Path filePath = Paths.get(REPOSITORY_DIR).resolve(fileDto.getFile_name()).normalize(); // 파일 경로 구성
         System.out.println("================= " + filePath.toString());
         Resource resource = new UrlResource(filePath.toUri());

         if (resource.exists()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                  "attachment; filename=\"" + fileDto.getFile_name() + "\"").body(resource);
         } else {
            fileDto.setFile_name("no-img.png");
            return ResponseEntity.notFound().build();
         }
      } catch (IOException e) {
         e.printStackTrace();
         return ResponseEntity.notFound().build();
      }
   }

   /* editPost 글 수정 페이지 이동_가드너 다이어리 */
   @GetMapping("/community/editPost/{post_no}")
   @ResponseBody
   public ResponseEntity<String> editPostLoad(@PathVariable("post_no") int post_no) {
      PostDto postDto = postService.findPostByPno(post_no);
      Map<String, Object> responseMap = new HashMap<>();

      responseMap.put("pno", postDto.getPost_no());
      responseMap.put("bno", postDto.getBoard_no());
      responseMap.put("userId", postDto.getUser_id());
      responseMap.put("title", postDto.getTitle());
      responseMap.put("content", postDto.getP_content());

      String editSend = "";
      try {
         ObjectMapper objectMapper = new ObjectMapper();
         editSend = objectMapper.writeValueAsString(responseMap);
      } catch (JsonProcessingException e) {
         e.printStackTrace();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body("{\"error\": \"JSON processing error\"}");
      }

      System.out.println("editsend : " + editSend);
      return ResponseEntity.ok().body(editSend);
   }

   /* editPost 글 수정_가드너 다이어리 */
   @PostMapping("/community/postView/editPost.do")
   public ModelAndView editPost(PostDto postDto) {

      ModelAndView mav = new ModelAndView("/community/postView");

      postService.updatePost(postDto);

      mav.setViewName("redirect:/community/postView/" + postDto.getPost_no());

      return mav;
   }

   /* 글 삭제_가드너 다이어리 */
   @GetMapping("community/deletePost/{post_no}")
   public String deletePost(@PathVariable("post_no") int post_no, Model model) {

      model.addAttribute("dto", postService.findPostByPno(post_no));

      postService.deletePost(post_no);

      return "redirect:/community/gardenerDiary";
   }

   /* postView 댓글 쓰기 */
   @PostMapping("/replyWrite.do")
   public String writeReplyPost(ReplyDto replyDto, RedirectAttributes redirectAttributes) {

      replyService.writeReply(replyDto); // 작성한 글 db에 넣기

      redirectAttributes.addAttribute("post_no", replyDto.getPost_no());

      return "redirect:/community/postView/{post_no}";
   }

   /* 좋아요 */
   @PostMapping("/community/postView/{post_no}/{user_id}/like.do")
   @ResponseBody
   public ResponseEntity<Map<String, Object>> likeBtn(@PathVariable("post_no") int post_no,
         @PathVariable("user_id") String user_id, Principal principal) {
      System.out.println("call like");
      LikeDto list = postService.getAllLike(post_no, user_id);
      Map<String, Object> response = new HashMap<>();
      int likeCnt = 0;

      if (list == null) {
         LikeDto dto = new LikeDto();
         dto.setLike_no(postService.getLikeNo());
         dto.setUser_id(user_id);
         dto.setPost_no(post_no);
         dto.setLike_chk(1);
         postService.putLike(dto); // insert
         likeCnt = postService.countLike(post_no);

         response.put("likeStatus", true);
         response.put("likeCnt", likeCnt);
         
      } else {
          postService.deleteLike(post_no, user_id);
          likeCnt = postService.countLike(post_no);
          
          response.put("likeStatus", false);
          response.put("likeCnt", likeCnt);
       }

       return ResponseEntity.ok().body(response);
    }
   
   
   // 댓 수
   @PostMapping("/community/updateReply/{reply_no}")
   @ResponseBody
   public ResponseEntity<String> updateReply(@PathVariable("reply_no") int reply_no, @RequestBody Map<String, String> payload) {
       try {
           String r_content = payload.get("r_content");
           System.out.println("Reply No: " + reply_no);
           System.out.println("Updated Content: " + r_content);
           postService.updateReplyContent(reply_no, r_content);
           return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
       } catch (Exception e) {
           e.printStackTrace(); // 서버 로그에 예외 스택 트레이스 출력
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정에 실패했습니다.");
       }
   }


   
    @DeleteMapping("/community/deleteReply/{reply_no}")
    public ResponseEntity<String> deleteReply(@PathVariable("reply_no") int reply_no) {
        try {
            postService.deleteReply(reply_no);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제에 실패했습니다.");
        }
    }
    
    
    
}