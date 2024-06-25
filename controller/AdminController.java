package com.greenfinal.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.greenfinal.dto.*;
import com.greenfinal.service.*;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class AdminController {
	private static final String REPOSITORY_DIR = "C:\\Users\\admin\\Desktop\\green_final\\src\\main\\resources\\static\\img\\post\\";
	private final UserinfoService userinfoService;
	private final PostService postService;
	private final ProductService productService;
	private final KeywordService keywordService;


	@GetMapping(path="/manage")
	public ModelAndView getManage() {
		ModelAndView mav = new ModelAndView("manage/admin_home");
		return mav;
	}


	// 회원 관리
	@GetMapping(path="/manage/user_info.do")
	public ModelAndView getUserinfo(@AuthenticationPrincipal User user) {
		List<UserinfoDto> list = userinfoService.getUserinfo();
		String userId = user.getUsername();

		ModelAndView mav = new ModelAndView("/manage/user_info");
		mav.addObject("list", list);
		mav.addObject("nickname", userinfoService.findNickName(userId));

		return mav;
	}

	@PostMapping("/manage/deleteUsers")
	@ResponseBody
	public ResponseEntity<?> deleteUsers(@RequestBody Map<String, List<String>> request) {
		List<String> userNosStr = request.get("userNos"); // 회원 번호 문자열 목록

		if (userNosStr == null || userNosStr.isEmpty()) {
			return ResponseEntity.badRequest().body("No user numbers provided"); // 요청에 데이터가 없는 경우
		}

		// 문자열 리스트를 정수 리스트로 변환
		List<Integer> userNos = userNosStr.stream()
				.map(Integer::parseInt) // 문자열을 정수로 변환
				.collect(Collectors.toList());

		// 사용자 삭제 서비스 호출
		userinfoService.deleteUsers(userNos);

		return ResponseEntity.ok("Users deleted successfully"); // 성공 응답
	}

	@GetMapping("/searchUser.do")
	public ModelAndView searchUser(SearchDto dto, @AuthenticationPrincipal User user) {
		List<UserinfoDto> list = userinfoService.getSearchUser(dto.getKeyword());
		String userId = user.getUsername();
		ModelAndView mav = new ModelAndView("/manage/user_info");
		mav.addObject("nickname", userinfoService.findNickName(userId));
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
		} else {
			mav.addObject("list", list);  // 결과가 있으면 리스트 전달
		}

		return mav;
	}


	@GetMapping("/manage/user_info/{page}")
	public ModelAndView changePage(@PathVariable("page") int pageNumber, @AuthenticationPrincipal User user) {
		SearchDto search = new SearchDto();
		search.setPage(pageNumber);
		search.calcPage(userinfoService.getTotalUserCount());
		List<UserinfoDto> list = userinfoService.getUserList(search);
		String userId = user.getUsername();
		ModelAndView mav = new ModelAndView("/manage/user_info");
		mav.addObject("nickname", userId);
		mav.addObject("list", list);
		mav.addObject("page", search);

		// 페이지 정보 로깅
		System.out.println("Page: " + search.getPage());
		System.out.println("Start Page: " + search.getStartPage());
		System.out.println("End Page: " + search.getEndPage());

		return mav;
	}


// ----------------------------------------------------------------------------------------


	// 게시판 관리
	@GetMapping(path="/manage/board.do")
	public ModelAndView getBoard(@AuthenticationPrincipal User user) {
		List<BoardDto> list = postService.getBoard();
		String userId = user.getUsername();
		ModelAndView mav = new ModelAndView("/manage/board");
		mav.addObject("list", list);
		mav.addObject("nickname",userId);

		return mav;
	}

	// 게시판 삭제
	@DeleteMapping("/manage/board/{board_no}") // DELETE 요청 엔드포인트
	public ResponseEntity<?> deleteBoard(@PathVariable("board_no") int board_no) {
		try {
			postService.deleteBoard(board_no); // 서비스에서 게시판 삭제
			return ResponseEntity.ok("Board deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to delete board");
		}
	}

	// 게시판 추가
	@PostMapping("/manage/addBoard")
	public ResponseEntity<?> addBoard(@RequestBody BoardDto boardDto) {
		// 게시판 이름이 유효한지 검증
		if (boardDto.getBoard_name() == null || boardDto.getBoard_name().isEmpty()) {
			return ResponseEntity.badRequest().body("Board name is required");
		}

		// 게시판 생성
		postService.addBoard(boardDto);

		return ResponseEntity.ok("Board created successfully");
	}

	// 게시판 수정
	//수정
	@PostMapping("/manage/updateBoard.do")
	public ModelAndView updateBoard(BoardDto boardDto) {
		ModelAndView mav = new ModelAndView("redirect:/manage/board.do");
		System.out.println("update Controller");
		System.out.println(boardDto.getBoard_no());

		// 기존 식물 정보 조회
		BoardDto existingBoardDto = postService.getBoardByBoardNo(boardDto.getBoard_no());
		// System.out.println(existingMyplantDto);

		// 변경된 필드 있는지 확인
		boolean isBoardNameChanged = !Objects.equals(existingBoardDto.getBoard_name(), boardDto.getBoard_name());

		// 필드 업데이트
		int boardNo = boardDto.getBoard_no();
		String name = boardDto.getBoard_name();

		if(isBoardNameChanged && name != null) postService.editBoardName(name, boardNo);

		System.out.println("no : " + boardDto.getBoard_no());
		System.out.println("name : " + boardDto.getBoard_name());

		postService.editBoard(boardDto);
		return mav;
	}

	// 게시물 관리
	@GetMapping(path="/manage/deletePost1.do")
	public ModelAndView getAllPost1(@AuthenticationPrincipal User user) {
		List<PostDto> list = postService.getAllPost1();
		String userId = user.getUsername();
		ModelAndView mav = new ModelAndView("/manage/deletePost1");
		mav.addObject("nickname",userId);
		mav.addObject("list", list);

		return mav;
	}

	@GetMapping(path="/manage/deletePost2.do")
	public ModelAndView getAllPost2(@AuthenticationPrincipal User user) {
		List<PostDto> list = postService.getAllPost2();
		String userId = user.getUsername();
		
		ModelAndView mav = new ModelAndView("/manage/deletePost2");
		mav.addObject("nickname",userId);
		mav.addObject("list", list);

		return mav;
	}

	@PostMapping("/manage/deletePosts")
	@ResponseBody
	public ResponseEntity<?> deletePosts(@RequestBody Map<String, List<String>> request) {
		List<String> postNosStr = request.get("postNos"); // 문자열로 받은 user_no 목록

		if (postNosStr == null || postNosStr.isEmpty()) {
			return ResponseEntity.badRequest().body("No post numbers provided");
		}

		// 문자열 리스트를 정수 리스트로 변환
		List<Integer> postNos = postNosStr.stream()
				.map(Integer::parseInt) // 문자열을 정수로 변환
				.collect(Collectors.toList());

		postService.deletePosts(postNos); // 서비스 클래스를 통해 사용자 삭제

		return ResponseEntity.ok("Posts deleted successfully");
	}

	/* 검색 기능 _알쓸식잡 */
	@GetMapping("/searchP1.do")
	public ModelAndView searchP1(SearchDto dto, @AuthenticationPrincipal User user) {
		List<PostDto> list;
		String userId = user.getUsername();
		list = postService.getSearchPost1(dto.getKeyword());

		System.out.println(list);

		ModelAndView mav = new ModelAndView("/manage/deletePost1");
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
			mav.addObject("nickname",userId);
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	/* 검색 기능 _가드너 다이어리 */
	@GetMapping("/searchP2.do")
	public ModelAndView searchP2(SearchDto dto, @AuthenticationPrincipal User user) {
		List<PostDto> list;
		String userId = user.getUsername();
		list = postService.getSearchPost2(dto.getKeyword());

		ModelAndView mav = new ModelAndView("/manage/deletePost2");
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
			mav.addObject("nickname",userId);
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	
	// 알쓸 추가
    @GetMapping("/articleForm.do") 
    @ResponseBody
    public ResponseEntity<String> writeArticleForm(Principal principal) {
       
      String user_id = principal.getName();
       // 글 작성_게시판 번호 자동 불러오기
       int nextNo = postService.getNextNo();
       
       String send = "{\"nextNo\":"+nextNo+", \"user_id\":\""+user_id+"\"}";
       System.out.println("send : " + send);

       return ResponseEntity.ok().body(send);
    }
    @PostMapping("/manage/writeArticle.do")
    public ModelAndView writeArticle(PostDto postDto, Principal principal, @RequestParam("post_img") MultipartFile file) {

       ModelAndView mav = new ModelAndView();    
       
       try {
           mav = new ModelAndView("redirect:/manage/deletePost1.do");
           int nextNo = postService.getNextNo();
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
           fileDto.setPost_no(nextNo);

           int img_id = postService.savePostImg(fileDto);
           
           postDto.setPost_no(nextNo);
           postDto.setUser_id(user_id);
           
           if (img_id == 0) {
               System.out.println("img_id: 0");
           } else {
              postDto.setImg_id(img_id);
           }
           postService.writePost(postDto);
           
       } catch (IOException e) {
           e.printStackTrace();
       }
       return mav;
 } 

//   ----------------------------------------------------------------------------------------

	// 상품 관리
	@GetMapping(path="/manage/product.do")
	public ModelAndView getProduct(@AuthenticationPrincipal User user) {
		List<ProductDto> list = productService.getProduct();
		String userId = user.getUsername();

		ModelAndView mav = new ModelAndView("/manage/product");
		mav.addObject("nickname",userId);
		mav.addObject("list", list);

		return mav;
	}

	@PostMapping("/manage/deleteProducts")
	@ResponseBody
	public ResponseEntity<?> deleteProducts(@RequestBody Map<String, List<String>> request) {
		List<String> productNosStr = request.get("productNos"); // 문자열로 받은 product_no 목록

		if (productNosStr == null || productNosStr.isEmpty()) {
			return ResponseEntity.badRequest().body("No product numbers provided");
		}

		// 문자열 리스트를 정수 리스트로 변환
		List<Integer> productNos = productNosStr.stream()
				.map(Integer::parseInt) // 문자열을 정수로 변환
				.collect(Collectors.toList());

		productService.deleteProducts(productNos); // 서비스 클래스를 통해 사용자 삭제

		return ResponseEntity.ok("Products deleted successfully");
	}

	// 상품 검색
	@GetMapping("/searchProduct.do")
	public ModelAndView searchProduct(SearchDto dto, @AuthenticationPrincipal User user) {
		List<ProductDto> list;
		String userId= user.getUsername();
		list = productService.getSearchProduct(dto.getKeyword());

		System.out.println(list);

		ModelAndView mav = new ModelAndView("/manage/product");
		mav.addObject("nickname",userId);
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	//수정
	@PostMapping("/manage/updateProduct.do")
   public ModelAndView updateProduct(ProductDto productDto) {
      ModelAndView mav = new ModelAndView("redirect:/manage/product.do");
      System.out.println("update Controller");
      System.out.println(productDto.getProduct_no());

      // 기존 식물 정보 조회
      ProductDto existingProductDto = productService.getProductByProductNo(productDto.getProduct_no());
      // System.out.println(existingMyplantDto);

      // 변경된 필드 있는지 확인
      boolean isProductNameChanged = !Objects.equals(existingProductDto.getProduct_name(), productDto.getProduct_name());
      boolean isProductUrlChanged = !Objects.equals(existingProductDto.getUrl(), productDto.getUrl());
      boolean isProductCategory_noChanged = !Objects.equals(existingProductDto.getCategory_no(), productDto.getCategory_no());
      boolean isProductManufactureChanged = !Objects.equals(existingProductDto.getManufacturer(), productDto.getManufacturer());
      boolean isProductImgChanged = !Objects.equals(existingProductDto.getImg_path(), productDto.getImg_path());

      // 필드 업데이트
      int productNo = productDto.getProduct_no();
      String name = productDto.getProduct_name();
      String url = productDto.getUrl();
      int category_no = productDto.getCategory_no();
      String man = productDto.getManufacturer();
      String img = productDto.getImg_path();

      if(isProductNameChanged && name != null) productService.editProductName(name, productNo);
      if (isProductUrlChanged && url != null) productService.editProductUrl(url, productNo);
      if (isProductCategory_noChanged) productService.editProductCategory_no(category_no, productNo);
      if(isProductManufactureChanged && man != null) productService.editProductMan(man, productNo);
      if(isProductImgChanged && img != null) productService.editProductImg(img, productNo);

      System.out.println("no : " + productDto.getProduct_no());
      System.out.println("name : " + productDto.getProduct_name());
      System.out.println("url : " + productDto.getUrl());
      System.out.println("Category_no : " + productDto.getCategory_no());
      System.out.println("man : " + productDto.getManufacturer());
      System.out.println("img : " + productDto.getImg_path());

      productService.editProduct(productDto);
      return mav;
   }

	@GetMapping(path="/manage/addProduct.do")
	public ModelAndView getAddProduct(@AuthenticationPrincipal User user) {
		String userId= user.getUsername();
		ModelAndView mav = new ModelAndView("/manage/addProduct");
		mav.addObject("nickname",userId);
		return mav;
	}
	
    @GetMapping("/getProductNextNo")
    public ResponseEntity<Map<String, Integer>> getProductNextNo() {
        int productNextNo = productService.getProductNextNo();
        Map<String, Integer> response = new HashMap<>();
        response.put("productNextNo", productNextNo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/inputProduct.do")
    public ModelAndView putProduct(@ModelAttribute ProductDto productDto) {
//        int productNextNo = productService.getProductNextNo();
//        productDto.setProduct_no(productNextNo); // product_no 설정

        List<ProductDto> list = productService.getProduct();

        ModelAndView mav = new ModelAndView("redirect:/manage/product.do");
        mav.addObject("list", list);
//        System.out.println(productNextNo);

        productService.saveProduct(productDto);

        return mav;
    }




//   ----------------------------------------------------------------------------------------


	// 키워드 관리
	@GetMapping(path="/manage/keyword.do")
	public ModelAndView getKeyword() {
		List<KeywordDto> list = keywordService.getKeyword();

		ModelAndView mav = new ModelAndView("/manage/keyword");
		mav.addObject("list", list);

		return mav;
	}

	@GetMapping(path="/manage/keyword1.do")
	public ModelAndView getKeyword1(@AuthenticationPrincipal User user) {
		List<KeywordDto> list = keywordService.getAllKeyword1();
		String userId = user.getUsername();
		

		ModelAndView mav = new ModelAndView("/manage/keyword1");
		mav.addObject("list", list);
		mav.addObject("nickname",userinfoService.findNickName(userId));

		return mav;
	}

	@GetMapping(path="/manage/keyword2.do")
	public ModelAndView getKeyword2(@AuthenticationPrincipal User user) {
		List<KeywordDto> list = keywordService.getAllKeyword2();
		String userId = user.getUsername();

		ModelAndView mav = new ModelAndView("/manage/keyword2");
		mav.addObject("nickname",userinfoService.findNickName(userId));
		mav.addObject("list", list);

		return mav;
	}

	// 삭제
	@PostMapping("/manage/deleteKeywords")
	@ResponseBody
	public ResponseEntity<?> deleteKeywords(@RequestBody Map<String, List<String>> request) {
		List<String> keywordNosStr = request.get("keywordNos"); // 문자열로 받은 product_no 목록

		if (keywordNosStr == null || keywordNosStr.isEmpty()) {
			return ResponseEntity.badRequest().body("No keyword numbers provided");
		}

		// 문자열 리스트를 정수 리스트로 변환
		List<Integer> keywordNos = keywordNosStr.stream()
				.map(Integer::parseInt) // 문자열을 정수로 변환
				.collect(Collectors.toList());

		keywordService.deleteKeywords(keywordNos); // 서비스 클래스를 통해 사용자 삭제

		return ResponseEntity.ok("Keywords deleted successfully");
	}

	// 키워드 검색
	@GetMapping("/searchKeyword.do")
	public ModelAndView searchKeyword(SearchDto dto) {
		List<KeywordDto> list;

		list = keywordService.getSearchKeyword(dto.getKeyword());

		System.out.println(list);

		ModelAndView mav = new ModelAndView("/manage/keyword");
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	@GetMapping("/searchKeyword1.do")
	public ModelAndView searchKeyword1(SearchDto dto) {
		List<KeywordDto> list;

		list = keywordService.getSearchKeyword1(dto.getKeyword());

		System.out.println(list);

		ModelAndView mav = new ModelAndView("/manage/keyword1");
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	@GetMapping("/searchKeyword2.do")
	public ModelAndView searchKeyword2(SearchDto dto) {
		List<KeywordDto> list;

		list = keywordService.getSearchKeyword2(dto.getKeyword());

		System.out.println(list);

		ModelAndView mav = new ModelAndView("/manage/keyword2");
		if (list == null || list.isEmpty()) {
			mav.addObject("noResults", true);  // 결과가 없음을 표시
		} else {
			mav.addObject("list", list);
		}

		return mav;
	}

	//수정
	@PostMapping("/manage/updateKeyword.do")
	public ModelAndView updateKeyword(KeywordDto keywordDto) {
		ModelAndView mav = new ModelAndView("redirect:/manage/keyword.do");
		System.out.println("update Controller");
		System.out.println("keyword_no : " + keywordDto.getKeyword_no());

		// 기존 키워드 정보 조회
		KeywordDto existingKeywordDto = keywordService.getKeywordByKeywordNo(keywordDto.getKeyword_no());

		// 변경된 필드 있는지 확인
		boolean isKeywordChanged = !Objects.equals(existingKeywordDto.getKeyword(), keywordDto.getKeyword());
		boolean isCategory_noChanged = existingKeywordDto.getCategory_no() != keywordDto.getCategory_no();
		boolean isKeygory_noChanged = existingKeywordDto.getKeygory_no() != keywordDto.getKeygory_no();

		// 필드 업데이트
		int keywordNo = keywordDto.getKeyword_no();
		String keyword = keywordDto.getKeyword();
		int categoryNo = keywordDto.getCategory_no();
		int keygoryNo = keywordDto.getKeygory_no();

		if (isKeywordChanged && keyword != null) {
			keywordService.editKeyword(keyword, keywordNo);
		}
		if (isCategory_noChanged) {
			keywordService.editCategory_no(categoryNo, keywordNo);
		}
		if (isKeygory_noChanged) {
			keywordService.editKeygory_no(keygoryNo, keywordNo);
		}

		System.out.println("keyword_no : " + keywordDto.getKeyword_no());
		System.out.println("keyword : " + keywordDto.getKeyword());
		System.out.println("category_no : " + keywordDto.getCategory_no());
		System.out.println("keygory_no : " + keywordDto.getKeygory_no());

		return mav;
	}

	@GetMapping(path="/manage/addKeyword.do")
	public ModelAndView addKeyword() {

		ModelAndView mav = new ModelAndView("/manage/addKeyword");

		return mav;
	}

	// 키워드 추가
	@PostMapping("/manage/inputKeyword.do")
	public ModelAndView putKeyword(KeywordDto keywordDto, @AuthenticationPrincipal User user) {
		int keyword_nextNo = keywordService.getKeywordNextNo();
		String userId = user.getUsername();
		List<KeywordDto> list = keywordService.getKeyword();

		ModelAndView mav = new ModelAndView("redirect:/manage/keygory.do");
		mav.addObject("nickname",userinfoService.findNickName(userId));
		mav.addObject("list", list);
		System.out.println(keyword_nextNo);

		keywordService.saveKeyword(keywordDto);

		return mav;
	}

//-------------

	// 키워드 카테고리(keygory)
	// 게시판 관리
	@GetMapping(path="/manage/keygory.do")
	public ModelAndView getKeygory(@AuthenticationPrincipal User user) {
		List<KeygoryDto> list = keywordService.getKeygory();
		String userId = user.getUsername();

		ModelAndView mav = new ModelAndView("/manage/keygory");
		mav.addObject("list", list);
		mav.addObject("nickname",userinfoService.findNickName(userId));

		return mav;
	}

	// keygory 삭제
	@DeleteMapping("/manage/keygory/{keygory_no}") // DELETE 요청 엔드포인트
	public ResponseEntity<?> deleteKeygory(@PathVariable("keygory_no") int keygory_no) {
		try {
			keywordService.deleteKeygory(keygory_no); // 서비스에서 게시판 삭제
			return ResponseEntity.ok("Keygory deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to delete keygory");
		}
	}

	// keygory 추가
	@PostMapping("/manage/addKeygory")
	public ResponseEntity<?> addKeygory(@RequestBody KeygoryDto keygoryDto) {
		// keygory 이름이 유효한지 검증
		if (keygoryDto.getKeygory_name() == null || keygoryDto.getKeygory_name().isEmpty()) {
			return ResponseEntity.badRequest().body("Keygory name is required");
		}

		// keygory 생성
		keywordService.addKeygory(keygoryDto);

		return ResponseEntity.ok("Keygory created successfully");
	}

	// keygory 수정
	//수정
	@PostMapping("/manage/updateKeygory.do")
	public ModelAndView updateKeygory(KeygoryDto keygoryDto) {
		ModelAndView mav = new ModelAndView("redirect:/manage/keygory.do");
		System.out.println("update Controller");
		System.out.println(keygoryDto.getKeygory_no());

		// 기존 식물 정보 조회
		KeygoryDto existingKeygoryDto = keywordService.getKeygoryByKeygoryNo(keygoryDto.getKeygory_no());

		// 변경된 필드 있는지 확인
		boolean isKeygoryNameChanged = !Objects.equals(existingKeygoryDto.getKeygory_name(), keygoryDto.getKeygory_name());

		// 필드 업데이트
		int keygoryNo = keygoryDto.getKeygory_no();
		String name = keygoryDto.getKeygory_name();

		if(isKeygoryNameChanged && name != null) keywordService.editKeygoryName(name, keygoryNo);

		System.out.println("no : " + keygoryDto.getKeygory_no());
		System.out.println("name : " + keygoryDto.getKeygory_name());

		keywordService.editKeygory(keygoryDto);
		return mav;
	}


}

