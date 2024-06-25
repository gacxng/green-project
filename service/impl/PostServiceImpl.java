package com.greenfinal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostDao postDao;

	public BoardDto selectBoardDto(int boardNo) {
		return postDao.selectBoardDto(boardNo);
	}

	public PostDto TotalPostListFornickname(int post_no) {
		return postDao.TotalPostListFornickname(post_no);
	}

	public List<PostDto> getAllPosts() {
		List<PostDto> postList = postDao.selectAllPosts();
		return postList;
	}

	public List<PostDto> getPostByBoardNo(int boardNo) {
		List<PostDto> postList = postDao.selectPostByBoardNo(boardNo);
		return postList;
	}

	public List<PostDto> TotalPostSearchList(int board_no, String keyword) {
		List<PostDto> newPostSearch = postDao.TotalPostSearchList(board_no, keyword);
		return newPostSearch;
	}

	/* Community_알쓸식잡 */
	public List<PostDto> getAllPost1() {
		List<PostDto> list = postDao.selectPost1();

		return list;
	}

	/* Community_가드너 다이어리 */
	public List<PostDto> getAllPost2() {
		List<PostDto> list = postDao.selectPost2();

		return list;
	}

	/* 검색 기능 구현을 위한 키워드 가져오기_알쓸식잡 */
	public List<PostDto> getSearchPost1(String keyword) {
		return postDao.selectSearchBoard1(keyword);
	}

	/* 검색 기능 구현을 위한 키워드 가져오기_가드너 다이어리 */
	public List<PostDto> getSearchPost2(String keyword) {
		return postDao.selectSearchBoard2(keyword);
	}

	// search했을 때 이미지 뜨도록 이미지 테이블과 조인
	public List<PostDto> getImg_name(List<PostDto> list) {
		List<PostDto> responseDtoList = new ArrayList<>();
		for (PostDto dto : list) {
			dto.setImg_name(postDao.selectImgName(Integer.toString(dto.getPost_no())));
			responseDtoList.add(dto);
		}
		return responseDtoList;
	}

	/* 가드너 다이어리 - 글 작성 */
	public int getNextNo() {
		boolean post_no = postDao.booleanMaxNo();
		int nextNo = 0;

		if (post_no == false) {
			nextNo = 1;
		} else {
			nextNo = postDao.selectMaxNo() + 1;
		}
		return nextNo;
	}

	public void writePost(PostDto postDto) {
		postDao.insertPost(postDto);
	}

	/* 글 상세페이지_가드너 다이어리 (post_no에 따른 해당 게시글 불러오기) */
	public PostDto findPostByPno(int post_no) {
		return postDao.selectPostDetail(post_no);
	}

	/* 가드너 다이어리 - 글 수정 */
	public void updatePost(PostDto postDto) {
		postDao.updatePost(postDto);
	}

	/* 가드너 다이어리 - 글 삭제 */
	public void deletePost(int post_no) {
		postDao.deletePost(post_no);
	}

	// admin
	public void deletePosts(List<Integer> postNos) { // 게시물 삭제
		if (postNos != null && !postNos.isEmpty()) {
			postDao.deletePosts(postNos); // DAO를 통해 사용자 목록 삭제
		}
	}

	// admin board
	public List<BoardDto> getBoard() {
		List<BoardDto> list = postDao.selectBoard();

		return list;
	}

	// 게시판 삭제
	public void deleteBoard(int board_no) {
		postDao.deleteBoard(board_no); // DAO를 통해 게시판 삭제
	}

	// 게시판 추가
	public void addBoard(BoardDto boardDto) {
		postDao.insertBoard(boardDto);
	}

	/* 게시판 조회수 */
	@Override
	public void hitCount(int post_no) {
		postDao.updateHit(post_no);
	}

	/* 좋아요 누르기 */
	@Override
	public void updateLike(int post_no, String user_id) {
		postDao.updateLike(post_no, user_id);
	}

	/* 좋아요 like_no */
	@Override
	public int getLikeNo() {
		boolean like_no = postDao.booleanLikeNo();
		int nextNo = 0;

		if (like_no == false) {
			nextNo = 1;
		} else {
			nextNo = postDao.selectLikeNo() + 1;
		}
		return nextNo;
	}

	/* like list 가져오기 */
	@Override
	public LikeDto getAllLike(int post_no, String user_id) {
		LikeDto list = postDao.selectLikelist(post_no, user_id);

		return list;
	}

	/* like insert */
	@Override
	public void putLike(LikeDto likeDto) {
		postDao.insertLike(likeDto);
	}

	/* like delete */
	@Override
	public void deleteLike(int post_no, String user_id) {
		postDao.deleteLike(post_no, user_id);
	}

	@Override
	public int countLike(int post_no) {
		return postDao.selectLikeCount(post_no);
	}

	@Override
	public List<FileDto> findPostImgBypno(int post_no) {
		return postDao.selectPostImgBypno(post_no);
	};

	@Override
	public FileDto getFileNameByid(int imgId) {
		return postDao.selectFileNameByid(imgId);
	};

	/* post img */
	@Override
	public int savePostImg(FileDto imgDto) {
		postDao.insertPostImg(imgDto);
		return imgDto.getImg_id(); // 저장 후 반환된 img_id 반환
	}

	@Override
	public int getTotalPostCnt() {
		int totProductCnt = postDao.selectTotalPostCnt();
		return totProductCnt;
	}

	@Override
	public int likeCountByPno(int post_no) {
		return postDao.likeCountByPno(post_no);
	}

	/* post + userinfo + heart */
	@Override
	public List<PostDto> TotalPostList(int board_no) {
		return postDao.TotalPostList(board_no);
	}

	// 호성(마이페이지 추가)
	public List<PostDto> getPostList(String user_id) {
		return postDao.getPostList(user_id);
	}

	public List<PostDto> getBoardName() {
		return postDao.getBoardName();
	}

	/*
	 * public int deletePost(int postId){ return postDao.deletePost(postId); }
	 */

	public int deletePostReply(int postId) {
		return postDao.deletePostReply(postId);
	}

	public List<BoardDto> getBoardList() {
		return postDao.getBoardList();
	}

	public String getSearchBoardName(int board_no) {
		return postDao.getSearchBoardName(board_no);
	}

	public List<PostDto> getPostList1ByName(String user_id, String keyword) {
		return postDao.getPostList1ByName(user_id, keyword);
	}

	public List<PostDto> getPostList2ByName(String user_id, String keyword) {
		return postDao.getPostList2ByName(user_id, keyword);
	}

	public List<PostDto> getPostListByName(String user_id, String keyword) {
		return postDao.getPostListByName(user_id, keyword);
	}

	public List<PostDto> topPost() {
		return postDao.topPost();
	}

	/* board */
	public List<BoardDto> getAllBoards() {
		List<BoardDto> boardList = postDao.selectAllBoards();
		return boardList;
	}

	// 종현이 추가
	public BoardDto getBoardByBoardNo(int board_no) {
		return postDao.selectBoardByBoardNo(board_no);
	}

	public void editBoard(BoardDto boardDto) {
		postDao.updateBoard(boardDto);
	}

	public void editBoardName(String board_name, int boardNo) {
		postDao.updateBoardName(board_name, boardNo);
	};

	public List<ReplyDto> selectReply(String user_id) {
		return postDao.selectReply(user_id);
	}

	@Override
	public ReplyDto getReplyByReplyNo(int reply_no) {
		return postDao.selectReplyByReplyNo(reply_no);
	}

	public void updateReplyContent(int reply_no, String r_content) {
		try {
			postDao.updateReplyContent(reply_no, r_content);
		} catch (Exception e) {
			e.printStackTrace(); // 예외 발생 시 스택 트레이스를 로그에 출력
			throw new RuntimeException("댓글 업데이트 중 오류 발생");
		}
	}

	@Override
	public ReplyDto selectReplyByReplyNo(int reply_no) {
		return postDao.selectReplyByReplyNo(reply_no);
	}

	@Override
	public void deleteReply(int reply_no) {
		postDao.deleteReply(reply_no);
	}
}
