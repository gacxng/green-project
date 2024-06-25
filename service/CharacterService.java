package com.greenfinal.service;

import com.greenfinal.dto.CharacterDto;

import java.util.List;

public interface CharacterService {
    List<CharacterDto> getAllChar();
    void insertChar(CharacterDto charDto);
    List<CharacterDto> getSelectChar(int char_no);
    int getUserChar(String user_id);
}
