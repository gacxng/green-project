package com.greenfinal.service.impl;

import com.greenfinal.dao.CharacterDao;
import com.greenfinal.dto.CharacterDto;
import com.greenfinal.service.CharacterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterDao charDao;

    public List<CharacterDto> getAllChar(){
        return charDao.getAllChar();
    }

    public void insertChar(CharacterDto charDto){
        charDao.insertChar(charDto);
    }
    public List<CharacterDto> getSelectChar(int char_no){
        return charDao.getSelectChar(char_no);
    }
    public int getUserChar(String user_id) {
    	return charDao.getUserChar(user_id);
    }
}
