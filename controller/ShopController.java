package com.greenfinal.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ShopController {
	private static final String REPOSITORY_DIR = "C:\\Users\\admin\\Desktop\\green_final\\src\\main\\resources\\static\\img\\post\\";
	private final ProductService productService;
	private final PostService postService;
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
        // System.out.println(search.isExistPrevPage());
        return pageNode;
	}
	
	
	@GetMapping("/shop")
	public ModelAndView getShop(Principal principal, @AuthenticationPrincipal User user) {
		ModelAndView mav = new ModelAndView("/shop/shop_home");
		if(user ==  null) {
			mav = new ModelAndView("login/login");
		}
		else {
			int listSize = 10;
			int groupSize = 5;
			SearchDto search = new SearchDto(listSize, groupSize);
		    int gardnersDiary = 2;
		    String user_id = (user.getUsername() != null) ? user.getUsername() : null;
		    int categoryNo = 1;
	
		    List<PostDto> gardnerDiaryList = postService.getPostByBoardNo(gardnersDiary);
		    List<PostDto> postList = postService.getAllPosts();
		    List<CategoryDto> categoryList = productService.getAllCategories();
		    List<ProductDto> productAllList = productService.getAllProducts();
		    List<ProductDto> productList = productService.getPlantByCategoryNo(categoryNo);
		    List<ProductDto> productListWithPage = productService.searchProductList(search);
		    List<ProductDto> favoriteProductList = productService.getFavoriteProductsByUserId(user_id);
		    List<ShopResultDto> shopAllList = productService.getShopAllList();
		    List<ShopResultDto> shopListWithPage = productService.searchShopList(search);
		    List<PostDto> topPost = postService.topPost();
		    System.out.println("shopListWithPage : " + shopListWithPage);
		    System.out.println(topPost);
		    UserinfoDto userDto = (user_id != null) ? userinfoService.getUserinfoById(user_id) : null;
		    mav.addObject("topPost", topPost);
		    mav.addObject("nickname",userinfoService.findNickName(user_id));
		    mav.addObject("user_id", user_id);
		    mav.addObject("userDto", userDto);
	
		    for (PostDto post : gardnerDiaryList) {
				/*
				 * if (post.getImg_url() == null || post.getImg_url().isEmpty()) { String
				 * noImgUrl = "/img/no-img.png"; post.setImg_url(noImgUrl); }
				 */
		        int postNo = post.getPost_no();
	        	List<FileDto> fileDtos = postService.findPostImgBypno(postNo);
	        	
	        	for (FileDto fileDto : fileDtos) {
			         String filePath = fileDto.getFile_path();
			         String noImgUrl = "/img/no-img.png";
			         
			         if (filePath != null) {
			            fileDto.setFile_path(filePath.replace("\\", "/"));
			         }
			         else {
			        	 noImgUrl = "/img/no-img.png"; post.setImg_url(noImgUrl);
			         }
			      }
		    }
		    
		    if (favoriteProductList == null) {
	            favoriteProductList = new ArrayList<>();
	        }
	
		    mav.addObject("productList", productList);
		    mav.addObject("productAllList", productAllList);
		    mav.addObject("favoriteProductList", favoriteProductList);
		    mav.addObject("productListWithPage", productListWithPage);
		    mav.addObject("shopAllList", shopAllList);
		    mav.addObject("shopListWithPage", shopListWithPage);
		    mav.addObject("gardnerDiaryList", gardnerDiaryList);
		    mav.addObject("postList", postList);
		    mav.addObject("categoryList", categoryList);
		    mav.addObject("categoryNo", categoryNo);
		    mav.addObject("page", search);
		}
	    return mav;
	}
	
	@GetMapping("/shop/imgFile/{img_id}")
	   public ResponseEntity<Resource> shopDownloadFile(@PathVariable("img_id") int imgId) {
	      try {
	          // System.out.println("img id: " + imgId);
	          FileDto fileDto = new FileDto();
	          
	          fileDto = postService.getFileNameByid(imgId);
	          Path filePath = Paths.get(REPOSITORY_DIR).resolve(fileDto.getFile_name()).normalize();
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
	
	// 샵 카테고리별로 검색탭
	   @GetMapping("/shop/{category_no}")
	   public ModelAndView getPlantCategory(
           @PathVariable("category_no") int categoryNo, 
           Principal principal) {

       int listSize = 10;
       int groupSize = 5;
       SearchDto search = new SearchDto(listSize, groupSize);
       int gardnersDiary = 2;

       List<PostDto> gardnerDiaryList = postService.getPostByBoardNo(gardnersDiary);
       List<PostDto> postList = postService.getAllPosts();
       List<CategoryDto> categoryList = productService.getAllCategories();
       List<ProductDto> productAllList = productService.getAllProducts();
       List<ProductDto> productList = productService.getPlantByCategoryNo(categoryNo);
       List<ProductDto> productListWithPage = productService.searchProductList(search);
       List<ShopResultDto> shopAllList = productService.getShopAllList();
       List<ShopResultDto> shopListWithPage = productService.searchShopList(search);

       ModelAndView mav = new ModelAndView("shop/shop_home");

       for (PostDto post : gardnerDiaryList) {
           if (post.getImg_url() == null || post.getImg_url().isEmpty()) {
               String noImgUrl = "http://localhost:8080/img/no-img.png";
               post.setImg_url(noImgUrl);
           }
       }

       if (principal != null) {
           String userId = principal.getName();
           List<ProductDto> favoriteProductList = productService.getFavoriteProductsByUserId(userId);
           mav.addObject("favoriteProductList", favoriteProductList);
           mav.addObject("nickname", userinfoService.findNickName(userId));
       } else {
           // principal이 null일 경우 빈 리스트로 설정
           mav.addObject("favoriteProductList", new ArrayList<ProductDto>());
       }

       // 요청된 카테고리에 해당하는 productList가 있을 때만 추가
       if (productList != null && !productList.isEmpty()) {
           mav.addObject("productList", productList);
       } else {
           // productList가 없을 때 빈 리스트로 설정
           mav.addObject("productList", new ArrayList<ProductDto>());
       }

       mav.addObject("productAllList", productAllList);
       mav.addObject("productListWithPage", productListWithPage);
       mav.addObject("shopAllList", shopAllList);
       mav.addObject("shopListWithPage", shopListWithPage);
       mav.addObject("gardnerDiaryList", gardnerDiaryList);
       mav.addObject("postList", postList);
       mav.addObject("categoryList", categoryList);
       mav.addObject("categoryNo", categoryNo);
       mav.addObject("page", search);

       return mav;
   }
	   
	
	// 로컬샵 JSON 부분
	@GetMapping("/shop/shop_home/{page}")
	@ResponseBody
	public ResponseEntity<String> getShopListJson(@PathVariable("page") int page) {
		// System.out.println("call getShopListJson("+ page + ")");
		int listSize = 10;
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
			System.out.println();

	        // root 노드에 page 노드 추가
			rootNode.set("page", putPageNode(search));
			
			// JSON 문자열 반환
            String json = objectMapper.writeValueAsString(rootNode);
            System.out.println("shop json :" + json);
            
            // 변환된 JSON 문자열을 ResponseEntity에 담아 반환
            return ResponseEntity.ok(json);
		}
		catch(JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
		}
	}
	
	
	@GetMapping("/shop/shop_product")
	public ModelAndView getProductPage(@AuthenticationPrincipal User user) {
		List<ProductDto> productAllList = productService.getAllProducts();
		List<CategoryDto> categoryList = productService.getAllCategories();
		// int categoryNo = 1;
		ModelAndView mav = new ModelAndView("shop/shop_product");
		
		if(user != null) {
			mav.addObject("nickaname", userinfoService.findNickName(user.getUsername()));
		}
		mav.addObject("categoryList", categoryList);
		mav.addObject("productAllList", productAllList);
		return mav;
	}
	

	@GetMapping("/shop/shop_product/{product_no}")
	public ModelAndView getProductPageByProductNo(
	        @PathVariable("product_no") int productNo,
	        Principal principal) {
		
		ProductDto product = productService.getProductByNo(productNo);
		int categoryNo = product.getCategory_no();
	    List<CategoryDto> categoryList = productService.getAllCategories();
	    List<ProductDto> productList = productService.getPlantByCategoryNo(categoryNo);
	    int product_no = productNo;
	    String categoryName = productService.getCategoryNameByNo(categoryNo);
		ModelAndView mav = new ModelAndView("shop/shop_product");
		
		if(principal != null) {
			String userId = principal.getName();
			List<ProductDto> favoriteProductList = productService.getFavoriteProductsByUserId(userId);
			mav.addObject("favoriteProductList", favoriteProductList);
			mav.addObject("nickname",userinfoService.findNickName(principal.getName()));
			List<FavoriteDto> favoriteList = productService.getProductNoByUserId(userId);
	        int favorite = 0;
	        for (FavoriteDto favoriteProduct : favoriteList) {
	            if (productNo == favoriteProduct.getProduct_no()) {
	                favorite = 1;
	                break;
	            }
	        }
	        mav.addObject("favorite", favorite);
	        
	        if (favoriteProductList == null) {
	        	mav.addObject("favoriteProductList", new ArrayList<ProductDto>());
	        }
		}
		
	    mav.addObject("categoryList", categoryList);
	    mav.addObject("product", product);
	    mav.addObject("productList", productList);
	    mav.addObject("categoryNo", categoryNo);
	    mav.addObject("categoryName", categoryName);
	    mav.addObject("product_no", product_no);
	    return mav;
	}
	
	
	@GetMapping("/shop/shop_product/{product_no}/{category_no}")
	@ResponseBody
	public ResponseEntity<String> getProductPageByProductNoJson(
	        @PathVariable("product_no") int productNo,
	        @PathVariable("category_no") Integer categoryNo) {
	    
	    List<ProductDto> productList;
	    if (categoryNo != null) {
	        productList = productService.getPlantByCategoryNo(categoryNo);
	    } else {
	        productList = productService.getPlantByCategoryNo(1); // 기본 카테고리 처리
	    }

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode rootNode = objectMapper.createObjectNode();

	        ArrayNode productArrayNode = objectMapper.createArrayNode();
	        for (ProductDto productDto : productList) {
	            ObjectNode productNode = objectMapper.createObjectNode();
	            productNode.put("product_no", productDto.getProduct_no());
	            productNode.put("product_name", productDto.getProduct_name());
	            productNode.put("url", productDto.getUrl());
	            productNode.put("category_no", productDto.getCategory_no());
	            productNode.put("manufacturer", productDto.getManufacturer());
	            productNode.put("p_reply", productDto.getP_reply());
	            productNode.put("img_path", productDto.getImg_path());
	            productArrayNode.add(productNode);
	        }
	        rootNode.set("list", productArrayNode);

	        String json = objectMapper.writeValueAsString(rootNode);
	        //System.out.println("/shop/shop_product_json/{product_no} json : " + json);

	        return ResponseEntity.ok().body(json);

	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting to JSON");
	    }
	}
	

	@PostMapping("/shop/shop_product/{product_no}/favorite/{newFavorite}")
	@ResponseBody
	public ResponseEntity<String> getProductFavoriteJson(
	        @PathVariable("product_no") int productNo,
	        @PathVariable("newFavorite") Integer favorite,
	        Principal principal) {
	    try {
	        if (principal == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 정보가 없습니다.");
	        }
	        // System.out.println("call getProductFavoriteJson");
			// System.out.println("product_no : " + productNo);
			// System.out.println("favorite : " + favorite);
			// System.out.println("user_id : " + principal.getName());

	        String user_id = principal.getName();
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode rootNode = objectMapper.createObjectNode();
	        rootNode.put("product_no", productNo);
	        rootNode.put("favorite", favorite);
	        rootNode.put("user_id", user_id);
	        String json = objectMapper.writeValueAsString(rootNode);

	        if(favorite == 1) {
	        	productService.putFavorite(user_id, productNo);
	        }
	        if(favorite == 0) {
	        	productService.removeFavorite(user_id, productNo);
	        }
	       //  System.out.println("shop favorite json : " + json);
        	return ResponseEntity.ok().body(json);
	        
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 처리 오류");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
	    }
	}
	
	
	// 종현
	@GetMapping(path="/shop/shop_map.do")
	public ModelAndView getShopMap(@AuthenticationPrincipal User user) {
		ModelAndView mav = new ModelAndView("/shop/shop_map");
		if(user != null) {
			mav.addObject("nickname",userinfoService.findNickName(user.getUsername()));
		}
		return mav;
	}
	
	
	// 샵 네이버 지도 json 받아옴
	@PostMapping("/shop/shop_map.do")
    public ResponseEntity<String> getMapJson(@RequestBody List<ShopResultDto> shopList) {
        try {
            System.out.println("Received shop list: " + shopList);
            // 변환된 객체를 데이터베이스에 저장합니다.
            for (ShopResultDto shopResultDto : shopList) {
                productService.saveShopResult(shopResultDto);
            }

            // 성공적으로 처리되었다는 응답을 반환합니다.
            return ResponseEntity.ok("Search results received and processed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // 처리 중 오류가 발생하면 내부 서버 오류 응답을 반환합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process search results");
        }
    }
	
	
}
