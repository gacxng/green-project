package com.greenfinal.dao;

import com.greenfinal.dto.*;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface ReplyDao {
    @Select("select r.reply_no, r.post_no, r.user_id, b.board_name, p.title, r.r_content, r.update_date " +
            "from reply r join post p on r.post_no = p.post_no join board b on p.board_no = b.board_no " +
            "where r.user_id=#{user_id}")
    List<ReplyDto> getReplyList(@Param("user_id") String user_id) throws DataAccessException;

    @Delete("delete from reply where reply_no = #{reply_no}")
    int deleteReply(@Param("reply_no") int replyId) throws DataAccessException;

    @Select("select * from reply r join post p on r.post_no = p.post_no join board b on p.board_no = b.board_no " +
            "where b.board_no=1 and r.user_id = #{user_id} and r.r_content like concat('%',#{keyword},'%')")
    List<ReplyDto> getReplyList1ByName(@Param("user_id")String user_id, @Param("keyword")String keyword) throws DataAccessException;
    @Select("select * from reply r join post p on r.post_no = p.post_no join board b on p.board_no = b.board_no " +
            "where b.board_no=2 and r.user_id = #{user_id} and r.r_content like concat('%',#{keyword},'%')")
    List<ReplyDto> getReplyList2ByName(@Param("user_id")String user_id, @Param("keyword")String keyword) throws DataAccessException;
    @Select("select * from reply r join post p on r.post_no = p.post_no join board b on p.board_no = b.board_no " +
            "where r.user_id = #{user_id} and r.r_content like concat('%',#{keyword},'%')")
    List<ReplyDto> getReplyListByName(@Param("user_id")String user_id, @Param("keyword")String keyword) throws DataAccessException;
    @Delete("<script>" +
            "DELETE FROM reply WHERE reply_no IN " +
            "<foreach item='item' index='index' collection='replyNos' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    void deleteReplies(@Param("replyNos") List<Long> replyNos);

    // 가연
    /* replyForm에 불러올 post_no에 따른 info 가져오기 */
    @Select("select * from reply")
    public ReplyDto selectReplyInfo() throws DataAccessException;

    // next_no가 null인지 확인
    @Select("select count(*) from reply")
    public boolean booleanMaxNo() throws DataAccessException;

    // next_no 최댓값 들고오기
    @Select("select max(reply_no) from reply")
    public int selectMaxNo() throws DataAccessException;

    /* post_no에 따른 댓글 insert */
    @Insert("insert into reply values(#{reply_no}, #{post_no}, #{user_id}, #{r_content}, now())")
    public void insertReply(ReplyDto replyDto) throws DataAccessException;

    /* post_no에 따른 reply 목록 */
    @Select("select r.reply_no, r.post_no, r.user_id, u.user_id, r.r_content, r.update_date, u.user_nickname from reply r join userinfo u on r.user_id = u.user_id where post_no = #{post_no} order by reply_no Desc")
    public List<ReplyDto> seletReplyListByPno(@Param("post_no") int post_no) throws DataAccessException;
}
