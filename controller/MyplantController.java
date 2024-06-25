package com.greenfinal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@MultipartConfig
@Controller
@RequiredArgsConstructor
public class MyplantController {
	private static final String REPOSITORY_DIR = "C:\\Users\\admin\\Desktop\\green_final\\src\\main\\resources\\static\\img\\myplant\\";
	private final MyplantService myplantService;
	private final UserinfoService userinfoService;
	
	private ObjectNode putPageNode(SearchDto search) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode pageNode = objectMapper.createObjectNode();
		pageNode.put("page", search.getPage());
        pageNode.put("listSizePerTime", search.getListSizePerTime());
        pageNode.put("pageGroupSize", search.getPageGroupSize());
        pageNode.put("keyword", search.getKeyword());
        pageNode.put("searchType", search.getSearchType());
        pageNode.put("totDataCnt", search.getTotDataCnt());
        pageNode.put("totPageCnt", search.getTotPageCnt());
        pageNode.put("startPage", search.getStartPage());
        pageNode.put("endPage", search.getEndPage());
        pageNode.put("existPrevPage", search.isExistPrevPage());
        pageNode.put("existNextPage", search.isExistNextPage());
        return pageNode;
	}

	
	@GetMapping("/myplant")
	public ModelAndView getMyplant(Principal principal) {
		if(principal == null) {
	        return new ModelAndView("redirect:login/login.do");
	    }
	    SearchDto search = new SearchDto();
	    String userId = principal.getName();
	    List<MyplantDto> myplantList = myplantService.searchMyplantListByUserId(userId, search);
	    ModelAndView mav = new ModelAndView("myplant/myplant");
	    int plantNo = 0;
	    
	    for(MyplantDto myplant : myplantList) {
	        plantNo = myplant.getPlant_no();
	    }
	    
	    mav.addObject("nickname",userinfoService.findNickName(userId));
	    mav.addObject("myplantList", myplantList);
	    mav.addObject("page", search);
	    // mav.addObject("userId", userId);
	    mav.addObject("plantNo", plantNo);
	    return mav;
	}
	

	@GetMapping("/myplant/{page}")
	@ResponseBody
	public ResponseEntity<String> getMyplantListJson(
			@PathVariable("page") int page,
			Principal principal) {
		// System.out.println("call getMyplantListJson("+ page + ")");
		SearchDto search = new SearchDto();
		String userId = principal.getName();
		search.setPage(page);
		List<MyplantDto> myplantList = myplantService.searchMyplantList(search);

        try {
            // 리스트를 JSON 형식의 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();

            // MyplantDto의 필드 추가
            ArrayNode myplantArrayNode = objectMapper.createArrayNode();
            for (MyplantDto myplantDto : myplantList) {
                ObjectNode myplantNode = objectMapper.createObjectNode();
                myplantNode.put("plant_no", myplantDto.getPlant_no());
                myplantNode.put("plant_name", myplantDto.getPlant_name());
                myplantNode.put("plant_nickname", myplantDto.getPlant_nickname());
                myplantNode.put("first_date", myplantDto.getFirst_date());
                myplantNode.put("last_water", myplantDto.getLast_water());
                myplantNode.put("user_memo", myplantDto.getUser_memo());
                myplantNode.put("user_id", myplantDto.getUser_id());
                myplantNode.put("img_name", myplantDto.getImg_name());
                // myplantNode.put("keyword_no", myplantDto.getKeyword_no());
                // myplantNode.put("scientific_name", myplantDto.getScientific_name());
                myplantNode.put("org_img_name", myplantDto.getOrg_img_name());
                myplantNode.put("favorite", myplantDto.getFavorite());
                myplantArrayNode.add(myplantNode);
            }
            rootNode.set("list", myplantArrayNode);

            // root 노드에 page 노드 추가
            rootNode.set("page", putPageNode(search));

            // JSON 문자열 반환
            String json = objectMapper.writeValueAsString(rootNode);
           // System.out.println(json);
            
            // 변환된 JSON 문자열을 ResponseEntity에 담아 반환
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 변환 중 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
        }
	}

	@PostMapping("/inputMyPlant.do")
	public ModelAndView putMyplant(MyplantDto myplantDto,
	        @RequestParam("myplant_img") MultipartFile file,
	        Principal principal) {
	    ModelAndView mav = new ModelAndView();
	    if (principal == null) {
	        return new ModelAndView("redirect:login/login.do");
	    }
	    try {
	        mav = new ModelAndView("redirect:/myplant");
	        int plant_no = myplantService.getMyplantNextNo();
	        String user_id = principal.getName();

	        String convertedImgName;
	        FileDto fileDto = new FileDto();
	        if (file.isEmpty() || file == null) {
	            convertedImgName = "no-img.png";
	            fileDto.setFile_name(convertedImgName);
	            System.out.println("NO FILE");
	        } else {
	            convertedImgName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	            file.transferTo(new File(REPOSITORY_DIR + convertedImgName));
	        }
	        fileDto.setUser_id(user_id);
	        fileDto.setFile_name(convertedImgName);
	        fileDto.setFile_path(REPOSITORY_DIR);
	        fileDto.setOrg_file_name(file.getOriginalFilename());

	        int img_id = myplantService.saveMyplantImg(fileDto);
	        myplantDto.setPlant_no(plant_no);
	        myplantDto.setUser_id(user_id);
	        if (img_id == 0) {
	            System.out.println("img_id: 0");
	        } else {
	            myplantDto.setImg_id(img_id);
	        }
	        myplantService.saveMyplant(myplantDto);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return mav;
	}

	
	@GetMapping("/download/{img_id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("img_id") int imgId) {
	   try {
	       // System.out.println("img id: " + imgId);
	       FileDto fileDto = new FileDto();
	       
	       fileDto = myplantService.getFileNameByid(imgId);
	       Path filePath = Paths.get(REPOSITORY_DIR).resolve(fileDto.getFile_name()).normalize(); // 파일 경로 구성
	       System.out.println("================= "+filePath.toString());
	       Resource resource = new UrlResource(filePath.toUri());
	       if(resource.exists()) {
	           return ResponseEntity.ok()
	                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFile_name() + "\"")
	                 .body(resource);
	       } else {
	    	   fileDto.setFile_name("no-img.png");
	           return ResponseEntity.notFound().build();
	       }
	   } catch(IOException e) {
	       e.printStackTrace();
	       return ResponseEntity.notFound().build();
	   }
	}
	
	
	@GetMapping("/myplant/myplant_list")
	public ModelAndView getMyplantAllList(Principal principal) {
        
		ModelAndView mav = new ModelAndView("myplant/myplant_list");
		if(principal == null) {
			 return new ModelAndView("redirect:/login/login.do");
		}
        String user_id = principal.getName();
        System.out.println("user_id : " + principal.getName());
		List<MyplantDto> myplantList =  myplantService.getMyplantListByUserId(user_id);
		mav.addObject("nickname", userinfoService.findNickName(user_id));
		mav.addObject("myplantList", myplantList);
		return mav;
	}
	
	// 내 식물 찜하기
	@GetMapping("/myplant/myplant_list/{plant_no}/{favorite}")
    @ResponseBody
    public ResponseEntity<String> getMyplantFavoriteJson(
            @PathVariable("plant_no") int plantNo,
            @PathVariable("favorite") Integer favorite) {
	    
		Logger logger = LoggerFactory.getLogger(ShopController.class);
		
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode rootNode = objectMapper.createObjectNode();
	        rootNode.put("plant_no", plantNo);
	        rootNode.put("favorite", favorite);
	        String json = objectMapper.writeValueAsString(rootNode);
	        
	        if(favorite == 1) {
	        	myplantService.putFavorite(plantNo);
	        }
	        if(favorite == 0) {
	        	myplantService.removeFavorite(plantNo);
	        }
	        
	        logger.info("Favorite 상태 업데이트 성공: " + json);
        	return ResponseEntity.ok().body(json);
	        
	    } catch (JsonProcessingException e) {
	        logger.error("JSON 처리 오류: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 처리 오류");
	    } catch (Exception e) {
	        logger.error("서버 오류: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
	    }
	}
	
	
	@PostMapping("/myplant/updateMyplant.do")
	   public ModelAndView updateMyplant(MyplantDto myplantDto, 
	           @RequestParam("update_img") MultipartFile file) {
	       ModelAndView mav = new ModelAndView("redirect:/myplant/myplant_list");
	       
	       try {
	           // 기존 식물 정보 조회
	           MyplantDto existingMyplantDto = myplantService.getMyplantByPlantNo(myplantDto.getPlant_no());
	           
	           // 변경된 필드 있는지 확인
	           boolean isPlantNameChanged = !Objects.equals(existingMyplantDto.getPlant_name(), myplantDto.getPlant_name());
	           boolean isPlantNicknameChanged = !Objects.equals(existingMyplantDto.getPlant_nickname(), myplantDto.getPlant_nickname());
	           boolean isUserMemoChanged = !Objects.equals(existingMyplantDto.getUser_memo(), myplantDto.getUser_memo());
	           boolean isFirstDateChanged = !Objects.equals(existingMyplantDto.getFirst_date(), myplantDto.getFirst_date()); // 추가
	           boolean isLastWaterChanged = !Objects.equals(existingMyplantDto.getLast_water(), myplantDto.getLast_water()); // 추가
	           
	           // 필드 업데이트
	           String name = myplantDto.getPlant_name();
	           String nick = myplantDto.getPlant_nickname();
	           String memo = myplantDto.getUser_memo();
	           String firstDate = myplantDto.getFirst_date(); // 추가
	           String lastWater = myplantDto.getLast_water(); // 추가
	           
	           if (isPlantNameChanged && name != null) existingMyplantDto.setPlant_name(name);
	           if (isPlantNicknameChanged && nick != null) existingMyplantDto.setPlant_nickname(nick);
	           if (isUserMemoChanged && memo != null) existingMyplantDto.setUser_memo(memo);
	           if (isFirstDateChanged && firstDate != null) existingMyplantDto.setFirst_date(firstDate); // 추가
	           if (isLastWaterChanged && lastWater != null) existingMyplantDto.setLast_water(lastWater); // 추가
	           System.out.println("updated myplantDto: " + myplantDto);
	           
	           // 이미지 파일 처리
	           FileDto existingImg = myplantService.getImgByImgId(existingMyplantDto.getImg_id());
	           if (existingImg == null) {
	               System.out.println("existingImg is null");
	           } else {
	               System.out.println("existingImg img name: " + existingImg.getFile_name());
	           }
	           
	           if (file != null && !file.isEmpty()) {
	               String updateImgName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	               file.transferTo(new File(REPOSITORY_DIR + updateImgName));
	               existingMyplantDto.setImg_name(updateImgName);
	               existingMyplantDto.setOrg_img_name(file.getOriginalFilename());
	               System.out.println("updateImgName: " + updateImgName);

	               // 새로운 이미지 정보를 데이터베이스에 저장
	               FileDto newFileDto = new FileDto();
	               newFileDto.setUser_id(existingMyplantDto.getUser_id());
	               newFileDto.setFile_name(updateImgName);
	               newFileDto.setFile_path(REPOSITORY_DIR);
	               newFileDto.setOrg_file_name(file.getOriginalFilename());
	               myplantService.saveMyplantImg(newFileDto);

	               // 업데이트된 이미지 ID를 MyplantDto에 설정
	               existingMyplantDto.setImg_id(newFileDto.getImg_id());
	           } else {
	               // 파일 업로드가 없는 경우에는 기존 이미지 정보 유지
	               if (existingImg != null) {
	                   existingMyplantDto.setImg_id(existingImg.getImg_id());
	                   existingMyplantDto.setImg_name(existingImg.getFile_name());
	                   existingMyplantDto.setOrg_img_name(existingImg.getOrg_file_name());
	                   System.out.println("existingImg img name: " + existingImg.getFile_name());
	               }
	           }

	           // 사용자 ID 설정
	           existingMyplantDto.setUser_id(existingMyplantDto.getUser_id());
	           myplantService.editMyplant(existingMyplantDto);
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       return mav;
	   }
	
	
	@GetMapping("/myplant/deleteMyplant.do")
	public ModelAndView deleteMyplant(@RequestParam("plant_no") int plantNo,
			Principal principal) {
		System.out.println("plant_no : " + plantNo);
		String user_id = principal.getName();
		myplantService.deleteMyplant(plantNo, user_id);
		ModelAndView mav = new ModelAndView("redirect:/myplant/myplant_list");
		return mav;
	}
	
	
}
