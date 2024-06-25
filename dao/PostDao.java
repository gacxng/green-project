package com.greenfinal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.greenfinal.dto.*;

@Mapper
@Repository("postDao")
public interface PostDao {
	@Select("select * from board where board_no = #{board_no}")
	public BoardDto selectBoardDto(@Param("board_no") int boardNo) throws DataAccessException;

	/* post + userinfo + heart 근데 post_no에 따른 */
	@Select("select u.user_nickname, p.img_id, p.post_no, p.board_no, p.user_id, p.title, p.p_content, p.regist_date, p.update_date, p.keyword, p.location, p.hit, p.img_url, "
			+ "coalesce(h.like_cnt, 0) like_cnt from post p join userinfo u on p.user_id = u.user_id left outer join (select count(*) like_cnt, post_no from heart group by post_no) "
			+ "h on p.post_no = h.post_no where p.post_no = #{post_no} order by post_no;")
	public PostDto TotalPostListFornickname(@Param("post_no") int post_no) throws DataAccessException;

	@Select("SELECT * FROM post")
	public List<PostDto> selectAllPosts() throws DataAccessException;

	/* 알쓸식잡 - post list */
	@Select("select * " + "from post p " + "left outer join image i " + "on p.post_no = i.post_no "
			+ "where board_no like 1 " + "order by p.post_no;")
	public List<PostDto> selectPost1() throws DataAccessException;

	/* 가드너 다이어리 - post list */
	@Select("select * " + "from post p " + "left outer join image i " + "on p.post_no = i.post_no "
			+ "where board_no like 2 " + "order by p.post_no Desc;")
	public List<PostDto> selectPost2() throws DataAccessException;

	/* 검색기능 new */
	@Select("select u.user_nickname, p.img_id, p.post_no, p.board_no, p.user_id, p.title, p.p_content, p.regist_date, p.update_date, p.keyword, p.location, p.hit, p.img_url, "
			+ "coalesce(h.like_cnt, 0) like_cnt from post p join userinfo u on p.user_id = u.user_id left outer join (select count(*) like_cnt, post_no from heart group by post_no) "
			+ "h on p.post_no = h.post_no where board_no = 2 and (title like concat('%', #{keyword}, '%') or p_content like concat('%', #{keyword}, '%'))")
	public List<PostDto> TotalPostSearchList(@Param("board_no") int board_no, @Param("keyword") String keyword)
			throws DataAccessException;

	/* 알쓸식잡 - 검색 */
	@Select("select * from post where board_no = 1 and (title like concat('%', #{keyword}, '%') or p_content like concat('%', #{keyword}, '%'))")
	public List<PostDto> selectSearchBoard1(@Param("keyword") String keyword) throws DataAccessException;

	/* 가드너 다이어리 - 검색 */
	@Select("select * from post where board_no = 2 and (title like concat('%', #{keyword}, '%') or p_content like concat('%', #{keyword}, '%'))")
	public List<PostDto> selectSearchBoard2(@Param("keyword") String keyword) throws DataAccessException;

	// search했을 때 이미지 뜨도록 이미지 테이블과 조인
	@Select("select img_name from image where img_no=#{post_no}")
	public String selectImgName(String img_name) throws DataAccessException;

	/* 가드너 다이어리 - 글 작성 */
	// next_no가 null인지 확인
	@Select("select count(*) from post")
	public boolean booleanMaxNo() throws DataAccessException;

	// next_no 최댓값 들고오기
	@Select("select max(post_no) from post")
	public int selectMaxNo() throws DataAccessException;

	@Insert("insert into post values(#{post_no}, #{board_no}, #{user_id}, #{title}, #{p_content}, now(), now(), #{p_like}, #{keyword}, #{location}, #{hit}, #{img_url}, #{img_id})")
	public void insertPost(PostDto postDto) throws DataAccessException; /* db에 맞춰서 input된 정보 insert */

	/* 글 상세페이지_가드너 다이어리 (post_no에 따른 해당 게시글 불러오기) */
	@Select("select * from post where board_no = 2 AND post_no = #{post_no}")
	public PostDto selectPostDetail(int post_no) throws DataAccessException;

	/* 가드너 다이어리 - 글 수정 */
	@Update("update post set title = #{title}, p_content = #{p_content}, update_date = now() where post_no = #{post_no}")
	public void updatePost(PostDto postDto) throws DataAccessException;

	/* 가드너 다이어리 - 글 삭제 */
	@Delete("delete from post where post_no = #{post_no}")
	public void deletePost(int post_no) throws DataAccessException;

	/* 게시판 조회수 */
	@Update("update post set hit = hit + 1 where post_no = #{post_no}")
	public void updateHit(int post_no) throws DataAccessException;

	/* 좋아요 누르기 */
	@Update("update heart set like_chk = like_chk +1 where post_no = #{post_no}, user_id = #{user_id}")
	public void updateLike(int post_no, String user_id) throws DataAccessException;

	/* 첫 좋아요 정보 입력 */
	@Insert("insert into heart values(#{like_no}, #{user_id}, #{post_no}, #{like_chk})")
	public void insertLike(LikeDto likeDto) throws DataAccessException;

	// like_no가 null인지 확인
	@Select("select count(*) from heart")
	public boolean booleanLikeNo() throws DataAccessException;

	// like_no 최댓값 들고오기
	@Select("select max(like_no) from heart")
	public int selectLikeNo() throws DataAccessException;

	/* post_no에 따른 좋아요 리스트 가져오기 */
	@Select("select * from heart where post_no = #{post_no} and user_id = #{user_id}")
	public LikeDto selectLikelist(@Param("post_no") int post_no, @Param("user_id") String user_id)
			throws DataAccessException;

	/* 좋아요 삭제 */
	@Delete("delete from heart where post_no = #{post_no} and user_id = #{user_id}")
	public void deleteLike(@Param("post_no") int post_no, @Param("user_id") String user_id) throws DataAccessException;

	/* 좋아요 상태 여부 */
	@Select("select count(*) from heart where post_no = #{post_no}")
	public int selectLikeCount(@Param("post_no") int post_no) throws DataAccessException;

	/* post_no에 따른 좋아요 count */
	@Select("select count(*) from heart h join post p on h.post_no = p.post_no where h.post_no = #{post_no}")
	public int likeCountByPno(@Param("post_no") int post_no) throws DataAccessException;

	/* post img */
	@Insert("INSERT INTO file (user_id, file_name, file_path, org_file_name, post_no) "
			+ "VALUES (#{user_id}, #{file_name}, #{file_path}, #{org_file_name}, #{post_no})")
	@Options(useGeneratedKeys = true, keyProperty = "img_id")
	public void insertPostImg(FileDto imgDto) throws DataAccessException;

	/* post_no 맞는 img_id로 img 불러오기 */
	@Select("select * from file where post_no = #{post_no}")
	public List<FileDto> selectPostImgBypno(@Param("post_no") int post_no) throws DataAccessException;

	@Select("SELECT * FROM file WHERE img_id=#{imgId}")
	public FileDto selectFileNameByid(@Param("imgId") int imgId) throws DataAccessException;

	/* postTotalCnt */
	@Select("SELECT count(*) FROM post")
	public int selectTotalPostCnt() throws DataAccessException;

	@Select("SELECT * from product limit #{offset}, #{count}")
	public List<PostDto> selectPostList(@Param("offset") int offset, @Param("count") int count);

	@Select("SELECT * FROM post where board_no=#{boardNo}")
	public List<PostDto> selectPostByBoardNo(@Param("boardNo") int boardNo);

	@Select("SELECT * FROM product where board_no=#{boardNo}")
	public List<PostDto> selectProductByBoardNo(@Param("boardNo") int boardNo);

	/* 탭으로 전환 */
	@Select("select * from board")
	public List<BoardDto> selectAllBoards() throws DataAccessException;

	/* post + userinfo + heart */
	@Select("select u.user_nickname, p.img_id, p.post_no, p.board_no, p.user_id, p.title, p.p_content, p.regist_date, p.update_date, p.keyword, p.location, p.hit, p.img_url, "
			+ "coalesce(h.like_cnt, 0) like_cnt from post p join userinfo u on p.user_id = u.user_id left outer join (select count(*) like_cnt, post_no from heart group by post_no) "
			+ "h on p.post_no = h.post_no where p.board_no = #{board_no} order by post_no;")
	public List<PostDto> TotalPostList(@Param("board_no") int board_no) throws DataAccessException;

	// 댓 수

	@Select("select * from reply where user_id = #{user_id}")
	List<ReplyDto> selectReply(@Param("user_id") String user_id) throws DataAccessException;

	@Select("SELECT user_id FROM reply WHERE reply_no = #{reply_no}")
	String getUserIdByReplyNo(@Param("reply_no") int reply_no);

	@Select("SELECT * FROM reply WHERE reply_no = #{reply_no}")
	ReplyDto selectReplyByReplyNo(int reply_no);

	//
	@Update("UPDATE reply SET r_content = #{r_content} WHERE reply_no = #{reply_no}")
	void updateReplyContent(@Param("reply_no") int replyNo, @Param("r_content") String r_content);

	@Delete("DELETE FROM reply WHERE reply_no = #{reply_no}")
	void deleteReply(@Param("reply_no") int reply_no);

	// 종현
	// 관리자 페이지
	// 게시물 삭제
	@Delete({ "<script>", "DELETE FROM post WHERE post_no IN ",
			"<foreach item='postNo' collection='postNos' open='(' separator=',' close=')'>", "#{postNo}", "</foreach>",
			"</script>" })
	void deletePosts(@Param("postNos") List<Integer> postNos);

	// admin board
	@Select("Select * from board")
	public List<BoardDto> selectBoard() throws DataAccessException;

	// 게시판 삭제
	@Delete("DELETE FROM board WHERE board_no = #{board_no}") // 게시판 번호로 삭제
	void deleteBoard(@Param("board_no") int board_no) throws DataAccessException;

	// 게시판 추가
	@Insert("INSERT INTO board (board_no, board_name) VALUES (#{board_no}, #{board_name})")
	void insertBoard(BoardDto boardDto) throws DataAccessException;

	// 호성(마이페이지 추가)
	@Select("select * from post p join board b on p.board_no = b.board_no where user_id = #{user_id}")
	List<PostDto> getPostList(@Param("user_id") String user_id) throws DataAccessException;

	@Select("select distinct b.board_name from board b join post p on b.board_no = p.board_no")
	List<PostDto> getBoardName() throws DataAccessException;

	@Select("select * from board")
	List<BoardDto> getBoardList() throws DataAccessException;

	@Delete("delete from reply where post_no = #{post_no}")
	int deletePostReply(@Param("post_no") int postId) throws DataAccessException;

	@Select("select * from board where board_no=#{board_no}")
	String getSearchBoardName(@Param("board_no") int board_no) throws DataAccessException;

	@Select("select * from post p join board b on b.board_no = p.board_no where p.board_no=1 and user_id=#{user_id} and "
			+ "(p.title like concat('%',#{keyword},'%') or p.p_content like concat('%',#{keyword},'%'))")
	List<PostDto> getPostList1ByName(@Param("user_id") String user_id, @Param("keyword") String keyword)
			throws DataAccessException;

	@Select("select * from post p join board b on b.board_no = p.board_no where p.board_no=2 and user_id=#{user_id} and "
			+ "(p.title like concat('%',#{keyword},'%') or p.p_content like concat('%',#{keyword},'%'))")
	List<PostDto> getPostList2ByName(@Param("user_id") String user_id, @Param("keyword") String keyword)
			throws DataAccessException;

	@Select("select * from post p join board b on b.board_no = p.board_no where user_id=#{user_id} and "
			+ "(p.title like concat('%', #{keyword}, '%') or p.p_content like concat('%', #{keyword}, '%'))")
	List<PostDto> getPostListByName(@Param("user_id") String user_id, @Param("keyword") String keyword)
			throws DataAccessException;

	@Select("SELECT * FROM board WHERE board_no=#{board_no}")
	public BoardDto selectBoardByBoardNo(int board_no) throws DataAccessException;

	@Update("UPDATE board " + "SET board_name=#{board_name} " + "WHERE board_no=#{board_no}")
	public void updateBoard(BoardDto boardDto) throws DataAccessException;

	@Update("UPDATE board SET board_name=#{board_name} WHERE board_no=#{boardNo}")
	public void updateBoardName(@Param("board_name") String board_name, @Param("boardNo") int boardNo)
			throws DataAccessException;

	@Select("SELECT post_no, title FROM POST WHERE board_no=2 ORDER BY hit DESC LIMIT 5")
	List<PostDto> topPost() throws DataAccessException;
}
