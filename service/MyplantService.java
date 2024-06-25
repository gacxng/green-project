package com.greenfinal.service;

import java.util.List;

import com.greenfinal.dto.FileDto;
import com.greenfinal.dto.MyplantDto;
import com.greenfinal.dto.SearchDto;

public interface MyplantService {
	List<MyplantDto> getAllMyplant();
	void saveMyplant(MyplantDto myplantDto);
	int getTotalCntMyplant();
	void editMyplant(MyplantDto myplantDto);
	MyplantDto getMyplantByPlantNo(int plant_no);
	List<MyplantDto> searchMyplantList(SearchDto search);
	public List<MyplantDto> searchMyplantListByUserId(String userId, SearchDto search);
	int getMyplantNextNo();
	
	void editMyplantName(String plant_name, int plantNo);
	void editMyplantNickname(String plant_nickname, int plantNo);
	void editMyplantUserMemo(String user_memo, int plantNo);

	void putFavorite(int plantNo);
	void removeFavorite(int plantNo);
	List<MyplantDto> getFavoriteMyplants(String userId);
	List<MyplantDto> getMyplantListByUserId(String userId);
	void deleteMyplant(int plantNo, String userId);
	int saveMyplantImg(FileDto imgDto);
	int getImgIdByImgName(String convertedImgName);
	public FileDto getFileNameByid(int imgId);
	FileDto getImgByImgId(int img_id);
}
