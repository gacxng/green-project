package com.greenfinal.service;

import java.util.List;

import com.greenfinal.dto.*;

public interface PostService {
	BoardDto selectBoardDto(int boardNo);
    PostDto TotalPostListFornickname(int post_no);

	List<PostDto> getAllPosts(); // 전체리스트 가져오기
	List<PostDto> getPostByBoardNo(int boardNo);
	List<PostDto> TotalPostSearchList(int board_no, String keyword);
	
	// 가연
    List<PostDto> getAllPost1(); // Community_알쓸식잡
    List<PostDto> getAllPost2(); // Community_가드너 다이어리
    List<PostDto> getSearchPost1(String keyword); // 검색 기능 구현을 위한 키워드 가져오기_알쓸식잡
    List<PostDto> getSearchPost2(String keyword); // 검색 기능 구현을 위한 키워드 가져오기_알쓸식잡
	
    List<PostDto> getImg_name(List<PostDto> list); // seach했을 때 이미지 뜨도록 이미지 테이블과 조인
    int getNextNo();
    void writePost(PostDto postDto); // 가드너 다이어리 글 작성
    PostDto findPostByPno(int post_no); // 가드너 다이어리 상세페이지 (post_no에 따른 해당 게시글 불러오기)
    void updatePost(PostDto postDto); // 가드너 다이어리 글 수정
    void deletePost(int post_no); // 가드너 다이어리 - 글 삭제
    void hitCount(int post_no); // 조회수
    void updateLike(int post_no, String user_id); // 좋아요 누르기
    int getLikeNo();
    LikeDto getAllLike(int post_no, String user_id);
    void putLike(LikeDto likeDto);
    void deleteLike(int post_no, String user_id);
    int countLike(int post_no);
    int likeCountByPno(int post_no);
    List<BoardDto> getAllBoards(); /* board */
    // 이미지
    int savePostImg(FileDto imgDto);
    List<FileDto> findPostImgBypno(int post_no);
    FileDto getFileNameByid(int imgId);
    /* pagination */
    
    int getTotalPostCnt(); //!!!
   
    /* post + userinfo + heart */ 
    List<PostDto> TotalPostList(int board_no);
	
	// 종현
    void deletePosts(List<Integer> postNos); // post_no로 삭제 // admin
    List<BoardDto> getBoard();
    void deleteBoard(int board_no); // 게시판 삭제
    void addBoard(BoardDto boardDto); // 게시판 추가
    BoardDto getBoardByBoardNo(int board_no);
    void editBoard(BoardDto boardDto);
    void editBoardName(String board_name, int boardNo);
    List<ReplyDto> selectReply(String user_id);
    
    // 댓글 수정
    ReplyDto getReplyByReplyNo(int reply_no);
    
    void updateReplyContent(int reply_no, String r_content);
    ReplyDto selectReplyByReplyNo(int reply_no);
    void deleteReply(int reply_no); 
    
    
    // 호성(마이페이지 추가)
    List<PostDto> getPostList(String user_id);
    List<PostDto> getBoardName();
    // int deletePost(int postId);
    int deletePostReply(int postId);
    List<BoardDto> getBoardList();
    List<PostDto> getPostList1ByName(String user_id, String keyword);
    List<PostDto> getPostList2ByName(String user_id, String keyword);
    List<PostDto> getPostListByName(String user_id, String keyword);
    List<PostDto> topPost();
}
