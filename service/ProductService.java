package com.greenfinal.service;

import java.util.List;

import com.greenfinal.dto.*;

public interface ProductService {
	
	// 아름
	List<ProductDto> getAllProducts();
	int getProductNextNo();
	int getTotalProductCnt();
	List<ProductDto> searchProductList(SearchDto search);
	
	// 종현
	void deleteProducts(List<Integer> productNos);
	ProductDto getProductByProductNo(int product_no);
	void editProduct(ProductDto productDto);
	void editProductName(String product_name, int productNo);
	void editProductUrl(String url, int productNo);
	void editProductCategory_no(int category_no, int productNo);
	void editProductMan(String Man, int productNo);
	void editProductImg(String Img, int productNo);
	List<ProductDto> getProduct();
	int getNextNo();
	void saveProduct(ProductDto productDto);


	// 가연
	/* shop main 검색기능 */
	List<ProductDto> getSearchProduct(String keyword);
	
	// 샵 추가
	List<ProductDto> getPlantByCategoryNo(int categoryNo);
	List<CategoryDto> getAllCategories();
	ProductDto getProductByNo(int productNo);
	String getCategoryNameByNo(int category_no);
	void putFavorite(String userId, int productNo);
	void removeFavorite(String userId, int productNo);
	List<ProductDto> getFavoriteProductsByUserId(String userId);
	void saveShopResult(ShopResultDto shopResultDto);
	List<ShopResultDto> searchShopList(SearchDto search);
	List<ShopResultDto> getShopAllList();
	List<FavoriteDto> getProductNoByUserId(String userId);
}
