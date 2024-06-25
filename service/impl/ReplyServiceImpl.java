package com.greenfinal.service.impl;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {
    private final ReplyDao replyDao;

    public List<ReplyDto> getReplyList(String user_id){
        return replyDao.getReplyList(user_id);
    }

    public int deleteReply(int replyId){
        return replyDao.deleteReply(replyId);
    }

    public List<ReplyDto> getReplyList1ByName(String user_id, String keyword){
        return replyDao.getReplyList1ByName(user_id, keyword);
    }
    public List<ReplyDto> getReplyList2ByName(String user_id, String keyword) {
        return replyDao.getReplyList2ByName(user_id, keyword);
    }
    public List<ReplyDto> getReplyListByName(String user_id, String keyword) {
        return replyDao.getReplyListByName(user_id, keyword);
    }
    public void deleteReplies(List<Long> replyNos){
        replyDao.deleteReplies(replyNos);
    }

    // 가연
    @Override
    public ReplyDto getReplyInfo() {
        return replyDao.selectReplyInfo();
    }

    @Override
    public int getNextNo() {
        boolean reply_no = replyDao.booleanMaxNo();
        int nextNo = 0;

        if (reply_no == false) {
            nextNo = 1;
        } else {
            nextNo = replyDao.selectMaxNo() + 1;
        }
        return nextNo;
    }

    @Override
    public void writeReply(ReplyDto replyDto) {
        replyDao.insertReply(replyDto);
    }

    @Override
    public List<ReplyDto> getReplyListByPno(int post_no) {
        return replyDao.seletReplyListByPno(post_no);
    }
}
