package com.greenfinal.service;

import com.greenfinal.dto.ReplyDto;

import java.util.List;

public interface ReplyService {
    List<ReplyDto> getReplyList(String user_id);
    int deleteReply(int replyId);
    List<ReplyDto> getReplyList1ByName(String user_id, String keyword);
    List<ReplyDto> getReplyList2ByName(String user_id, String keyword);
    List<ReplyDto> getReplyListByName(String user_id, String keyword);
    void deleteReplies(List<Long> replyNos);

    ReplyDto getReplyInfo();
    int getNextNo();
    void writeReply(ReplyDto replyDto);
    List<ReplyDto> getReplyListByPno(int post_no);
}
