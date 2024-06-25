package com.greenfinal.service;

import java.util.List;

import com.greenfinal.dto.AiplantDto;
import com.greenfinal.dto.SaveplantDto;

public interface AiplantService {
	
	List<AiplantDto> getAiplant(String user_id);
	List<AiplantDto> getAiplant2(String user_id);
   
	// aiplant_result
	List<AiplantDto> getAiplantResult(String user_id);
   
	void savePlant(SaveplantDto saveplantDto);
   
	List<AiplantDto> selectAiplantList(String user_id);
   
	void deleteAiPlant(SaveplantDto saveplantDto);

	int[] processUserKeywords(List<String> userKeywords);
}
