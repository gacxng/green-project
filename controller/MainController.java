package com.greenfinal.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ProductService productService;
	private final PostService postService;
	private final MyplantService myplantService;
	private final UserinfoService userinfoService;
	private final GameService gameService;
	private final CharacterService characterService;
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
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
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@GetMapping("/main")
	public ModelAndView getMyplant(Principal principal, HttpSession session) {
		String user_id = principal.getName();
	    if (principal == null) {
	        return new ModelAndView("redirect:login/login.do");
	    }
	    if(characterService.getUserChar(user_id)==0) {
	    	session.setAttribute("user_id", user_id);
	    	return new ModelAndView("redirect:/login/selectChar.do");
	    }
	    
	    int listSize = 8;
	    int groupSize = 3;
	    SearchDto search = new SearchDto(listSize, groupSize);
	    
	    List<PostDto> postList = postService.getAllPosts();
	    List<ProductDto> productList = productService.searchProductList(search);
	    List<MyplantDto> myplantList = myplantService.getMyplantListByUserId(user_id);
	    List<MyplantDto> favoriteMyplantList = myplantService.getFavoriteMyplants(user_id);
	    List<ShopResultDto> shopAllList = productService.getShopAllList();
	    List<ShopResultDto> shopListWithPage = productService.searchShopList(search);
	    ModelAndView mav = new ModelAndView("main"); 

	    String user_nickname = userinfoService.findNickName(user_id);
	    mav.addObject("user_nickname", user_nickname);

	    String postLinkImgPath;
	    for (PostDto post : postList) {
	        if (post.getLocation() == null || post.getLocation().isEmpty()) {
	            post.setLocation("false");
	        }

	        if (post.getTitle() == null || post.getTitle().isEmpty()) {
	            post.setTitle("no title");
	        }

	        if (post.getImg_path() == null) {
	            postLinkImgPath = "/img/post/post-img.png";
	            post.setImg_path(postLinkImgPath);
	        } else {
	            postLinkImgPath = post.getImg_path();
	        }
	    }
	    
        try {
            LocalDate registDate = userinfoService.GetRegist_date(user_id).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            long daysSinceRegistration = ChronoUnit.DAYS.between(registDate, currentDate);

            System.out.println("registDate : " + registDate);
            System.out.println("currentDate : " + currentDate);
            System.out.println("daysSinceRegistration : " + daysSinceRegistration);

            mav.addObject("daysSinceRegistration", daysSinceRegistration);
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
        
        // 게임
        String userCharacterNick = userinfoService.getUserCharacterByUserId(user_id);
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String userId = "";
	       int waterCount = 0;
	       int loveCount = 0;
	       int sunCount = 0;
	      int pooCount = 0;
	       int nutritionCount = 0;
	       int bugCount = 0;
	       int potCount = 0;
	       int musicCount = 0;
	       
	       System.out.println("userCharacterNick: " + userCharacterNick);

	       if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	           UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	           userId = userDetails.getUsername();
	           
	           // 사용자의 레벨에 따라 각종 카운트 설정
	           int g_level = gameService.userLevel(userId);
	           if (g_level == 1) {
	               waterCount = gameService.waterCount(userId);
	               loveCount = gameService.loveCount(userId);
	           } else if (g_level == 2) {
	               waterCount = gameService.waterCount(userId);
	               loveCount = gameService.loveCount(userId);
	               sunCount = gameService.sunCount(userId);
	               pooCount = gameService.pooCount(userId);
	           } else if (g_level == 3) {
	               waterCount = gameService.waterCount(userId);
	               loveCount = gameService.loveCount(userId);
	               sunCount = gameService.sunCount(userId);
	               pooCount = gameService.pooCount(userId);
	               bugCount = gameService.bugCount(userId);
	               potCount = gameService.potCount(userId);
	           } else if (g_level == 4) {
	               waterCount = gameService.waterCount(userId);
	               loveCount = gameService.loveCount(userId);
	               sunCount = gameService.sunCount(userId);
	               pooCount = gameService.pooCount(userId);
	               bugCount = gameService.bugCount(userId);
	               potCount = gameService.potCount(userId);
	               musicCount = gameService.musicCount(userId);
	               nutritionCount = gameService.nutritionCount(userId);
	         }
	       }
	       
	       int g_level = gameService.userLevel(userId);
	       int progressValue = gameService.ProgressValue(userId);
	       String char_no = gameService.selectCharNo(userId);
	       
	       GameDto game = new GameDto();
	       game.setG_level(g_level);
	       

	      String characterImage;
	      switch(g_level){
	         case 1:
	            characterImage = "img/game_character/seed-1_"+ char_no + "lv_1.png";
	            break;
	         case 2:
	        	 characterImage = "img/game_character/seed-2_"+ char_no + "lv_2.png";
	            break;
	         case 3:
	        	 characterImage = "img/game_character/seed-3_"+ char_no + "lv_3.png";
	            break;
	         case 4:
	        	 characterImage = "img/game_character/seed-4_"+ char_no + "lv_4.png";
	            break;
	         case 5:
	        	 characterImage = "img/game_character/seed-5_" + char_no + "lv_5.png";
	        	 break;
	         default:
	            characterImage = "img/myplant/no-img.png";
	            break;   
	      }
	       
	       mav.addObject("nickname", userinfoService.findNickName(user_id));
	       mav.addObject("postList", postList);
	       mav.addObject("productList", productList);
	       mav.addObject("myplantList", myplantList);
	       mav.addObject("favoriteMyplantList", favoriteMyplantList);
	       mav.addObject("shopAllList", shopAllList);
	       mav.addObject("shopListWithPage", shopListWithPage);
	       mav.addObject("page", search);
	       mav.addObject("game",game);
	       mav.addObject("g_level", g_level);
	       mav.addObject("progressValue", progressValue);
	       mav.addObject("waterCount", waterCount);
	       mav.addObject("loveCount", loveCount);
	       mav.addObject("sunCount", sunCount);
	       mav.addObject("pooCount", pooCount);
	       mav.addObject("nutritionCount", nutritionCount);
	       mav.addObject("bugCount", bugCount);
	       mav.addObject("potCount", potCount);
	       mav.addObject("musicCount", musicCount);
	       mav.addObject("progressValue", progressValue);
	       mav.addObject("characterImage",characterImage);
	       mav.addObject("userCharacterNick",userCharacterNick);
	       return mav;

	}

	@GetMapping("/main/{page}")
	@ResponseBody
	public ResponseEntity<String> getProductListJson(@PathVariable("page") int page) {
		//System.out.println("call getProductListJson("+ page + ")");
		int listSize = 8;
		int groupSize = 2;
		SearchDto search = new SearchDto(listSize, groupSize);
		// search.setListSizePerTime(8);
		search.setPage(page);
		List<ProductDto> productList = productService.searchProductList(search);

		try {
			// 리스트 JSON 형식의 문자열로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode rootNode = objectMapper.createObjectNode();
			
			// ProductDto의 필드 추가
			ArrayNode productArrayNode = objectMapper.createArrayNode();
			for(ProductDto productDto : productList) {
				ObjectNode productNode = objectMapper.createObjectNode();
				productNode.put("product_no", productDto.getProduct_no());
				productNode.put("product_name", productDto.getProduct_name());
				productNode.put("url", productDto.getUrl());
				productNode.put("category_no", productDto.getCategory_no());
				productNode.put("manufacturer", productDto.getManufacturer());
				productNode.put("p_reply", productDto.getP_reply());
				productNode.put("img_path", productDto.getImg_path());
				productNode.put("favorite", productDto.getFavorite());
				productArrayNode.add(productNode);
			}
			rootNode.set("list", productArrayNode);

	        // root 노드에 page 노드 추가
			rootNode.set("page", putPageNode(search));
			
			// JSON 문자열 반환
            String json = objectMapper.writeValueAsString(rootNode);
            // System.out.println(json);
            
            // 변환된 JSON 문자열을 ResponseEntity에 담아 반환
            return ResponseEntity.ok(json);
		}
		catch(JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
		}
	}
	
	// 로컬 샵
		@GetMapping("/main/shop/{page}")
		@ResponseBody
		public ResponseEntity<String> getLocalShopJson(@PathVariable("page") int page) {
			// System.out.println("Main : call getProductListJson("+ page + ")");
			int listSize = 8;
			int groupSize = 5;
			SearchDto search = new SearchDto(listSize, groupSize);
			// search.setListSizePerTime(8);
			search.setPage(page);
			List<ShopResultDto> shopList = productService.searchShopList(search);
			
			try {
				// 리스트 JSON 형식의 문자열로 변환
				ObjectMapper objectMapper = new ObjectMapper();
				ObjectNode rootNode = objectMapper.createObjectNode();
				
				// ProductDto의 필드 추가
				ArrayNode shopArrayNode = objectMapper.createArrayNode();
				for(ShopResultDto shop : shopList) {
					ObjectNode shopNode = objectMapper.createObjectNode();
					shopNode.put("address_name", shop.getAddress_name());
					shopNode.put("category_group_code", shop.getCategory_group_code());
					shopNode.put("category_name", shop.getCategory_name());
					shopNode.put("distance", shop.getDistance());
					shopNode.put("id", shop.getId());
					shopNode.put("phone", shop.getPhone());
					shopNode.put("place_name", shop.getPlace_name());
					shopNode.put("place_url", shop.getPlace_url());
					shopNode.put("road_address_name", shop.getAddress_name());
					shopNode.put("x", shop.getX());
					shopNode.put("y", shop.getY());
					shopArrayNode.add(shopNode);
				}
				rootNode.set("list", shopArrayNode);
				
				// root 노드에 page 노드 추가
				rootNode.set("page", putPageNode(search));
				
				// JSON 문자열 반환
				String json = objectMapper.writeValueAsString(rootNode);
				// System.out.println(json);
				
				// 변환된 JSON 문자열을 ResponseEntity에 담아 반환
				return ResponseEntity.ok(json);
			}
			catch(JsonProcessingException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
			}
		}
	
	
	@GetMapping("/join")
	public ModelAndView getJoin() {
		ModelAndView mav = new ModelAndView("user/join");
		return mav;
	}
	
	@GetMapping("/community")
	   public ModelAndView getCommuinity(@AuthenticationPrincipal User user) {
	      ModelAndView mav = new ModelAndView("community/community_home");
	      System.out.println("User : "+ user);
	      if(user == null) {
	         mav = new ModelAndView("login/login");
	      }
	      else {
	         mav.setViewName("redirect:/community/article");
	      }
	      return mav;
	   }
}

