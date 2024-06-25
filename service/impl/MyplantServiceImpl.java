package com.greenfinal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greenfinal.dao.MyplantDao;
import com.greenfinal.dto.FileDto;
import com.greenfinal.dto.MyplantDto;
import com.greenfinal.dto.SearchDto;
import com.greenfinal.service.MyplantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyplantServiceImpl implements MyplantService {
	private final MyplantDao myplantDao;
	
	@Override
	public List<MyplantDto> getAllMyplant() {
		List<MyplantDto> myplantList = myplantDao.selectAllMyplants();
		return myplantList;
	}
	
	@Override
	public List<MyplantDto> getMyplantListByUserId(String userId) {
		List<MyplantDto> myplantList = myplantDao.selectMyplantListByUserId(userId);
		return myplantList;
	}
	
	@Override
	public List<MyplantDto> getFavoriteMyplants(String userId) {
		List<MyplantDto> favoriteMyplantList = myplantDao.selectFavoriteMyplants(userId);
		return favoriteMyplantList;
	};
	
	@Override
	public int getMyplantNextNo() {
		return myplantDao.selectMyPlantLastNo() + 1;
	}
	
	@Override
	public int getTotalCntMyplant() {
		return myplantDao.selectTotalMyplantCnt();
	}
	
	@Override
	public void saveMyplant(MyplantDto myplantDto) {
		myplantDao.insertMyplant(myplantDto);
	}
	
	@Override
	public int saveMyplantImg(FileDto imgDto) {
	    myplantDao.insertMyplantImg(imgDto);
	    return imgDto.getImg_id(); // 저장 후 반환된 img_id 반환
	}
	
	@Override
	public void editMyplant(MyplantDto myplantDto) {
		myplantDao.updateMyplant(myplantDto);
	}
	
	@Override
	public void editMyplantName(String plant_name, int plantNo) {
		myplantDao.updateMyplantName(plant_name, plantNo);
	};
	
	@Override
	public void editMyplantNickname(String plant_nickname, int plantNo) {
		myplantDao.updateMyplantNickname(plant_nickname, plantNo); 
	};
	
	@Override
	public void editMyplantUserMemo(String user_memo, int plantNo) {
		myplantDao.updateMyplantUserMemo(user_memo, plantNo);
	};
	
	@Override
	public List<MyplantDto> searchMyplantList(SearchDto search) {
		search.calcPage(myplantDao.selectTotalMyplantCnt());
		int page = search.getOffset();
		int cnt = search.getListSizePerTime();
		// System.out.println("myplantList : " + myplantDao.selectMyplantList(userId, page, cnt));
		return myplantDao.searchMyplantList(page, cnt);
	};
	
	@Override
	public List<MyplantDto> searchMyplantListByUserId(String userId, SearchDto search) {
		search.calcPage(myplantDao.selectTotalMyplantCntByUserId(userId));
		int page = search.getOffset();
		int cnt = search.getListSizePerTime();
		// System.out.println("myplantList : " + myplantDao.selectMyplantList(userId, page, cnt));
		return myplantDao.searchMyplantListByUserId(userId, page, cnt);
	};
	
	@Override
	public MyplantDto getMyplantByPlantNo(int plant_no) {
		return myplantDao.selectMyplantByPlantNo(plant_no);
	}
	
	@Override
	public void deleteMyplant(int plantNo, String userId) {
		myplantDao.deleteMyplant(plantNo, userId);
	};

	// 내 식물 즐겨찾기 추가
	@Override
	public void putFavorite(int plantNo) {
		myplantDao.putFavoriteByPlantNo(plantNo);
	};
	
	@Override
	public void removeFavorite(int plantNo) {
		myplantDao.removeFavoriteByPlantNo(plantNo);
	}
	
	@Override
	public FileDto getFileNameByid(int imgId) {
		return myplantDao.selectFileNameByid(imgId);
	};
	
	@Override
	public int getImgIdByImgName(String imgName) {
		return myplantDao.selectImgIdByImgName(imgName);
	};
	
	// 이미지 아이디로 이미지 가져오기 추가
	public FileDto getImgByImgId(int imgId) {
		return myplantDao.selectImgByImgId(imgId);
	};
	
}
