package com.greenfinal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenfinal.dao.*;
import com.greenfinal.dto.*;
import com.greenfinal.service.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductDao productDao;
	
	@Override
	public int getProductNextNo() {
		int productNextNo = productDao.selectTotalProductCnt() + 1;
		return productNextNo;
	}
	
	@Override
	public int getTotalProductCnt() {
		int totProductCnt = productDao.selectTotalProductCnt();
		return totProductCnt;
	}

	@Override
	public List<ProductDto> getAllProducts() {
		List<ProductDto> productList = productDao.selectAllProducts();
		return productList;
	}
	
	@Override
	public List<ProductDto> searchProductList(SearchDto search) {
		search.calcPage(productDao.selectTotalProductCnt());
		int page = search.getOffset();
		int cnt = search.getListSizePerTime();
		return productDao.selectProductList(page, cnt);
	}
	

	@Override
	public List<ProductDto> getFavoriteProductsByUserId(String userId) {
		List<ProductDto> favoriteProductList = productDao.selectFavoriteProductByUserId(userId);
		return favoriteProductList;
	};
	
	@Override
	public List<ProductDto> getPlantByCategoryNo(int categoryNo) {
		List<ProductDto> productList = productDao.selectProductByCategoryNo(categoryNo);
		return productList;
	}
	

	@Override
	public List<ProductDto> getProduct() {
		List<ProductDto> list = productDao.selectProduct();

		return list;
	}
	
	public int getNextNo() {
		boolean product_no = productDao.booleanMaxNo();
		int nextNo = 0;

		if(product_no == false) {
			nextNo = 1;
		}
		else {
			nextNo = productDao.selectMaxNo() +1;
		}
		return nextNo;
	}

	@Override
	public void saveProduct(ProductDto productDto) {
		productDao.insertProduct(productDto);
	}

	
	public String getCategoryNameByNo(int categoryNo) {
		String category = "";
		switch(categoryNo) {
			case 1: 
				category = "수경식물"; // 수경식물
				break;
			case 2: 
				category = "다육식물"; // 다육식물
				break;
			case 3: 
				category = "중대형 식물"; // 중대형 식물
				break;
			case 4: 
				category = "소형 식물"; // 소형 식물
				break;
			case 5: 
				category = "행잉 식물"; // 행잉 식물
				break;
			case 6: 
				category = "난/분재"; // 난
				break;
			case 7: 
				category = "모종"; // 모종
				break;
			case 8: 
				category = "씨앗/구근"; // 구근
				break;
			case 9: 
				category = "화분"; // 화분
				break;
		}
		// System.out.println("category : " + category);
		return category;
	}

    @Override
    public void deleteProducts(List<Integer> productNos) { // 게시물 삭제
        if (productNos != null && !productNos.isEmpty()) {
            productDao.deleteProducts(productNos); // DAO를 통해 사용자 목록 삭제
        }
    }
    @Override
    public ProductDto getProductByNo(int productNo) {
    	ProductDto product = productDao.selectProductByNo(productNo);
    	return product;
    }
    
    @Override
    public List<CategoryDto> getAllCategories() {
    	List<CategoryDto> categoryList = productDao.selectAllCategories();
    	return categoryList;
    }
    
	// 가연
	/* shop main 검색기능 */
	@Override
	public List<ProductDto> getSearchProduct(String keyword) {
		return productDao.selectSearchProduct(keyword);
	}
	
   @Override
   public ProductDto getProductByProductNo(int product_no) {
      return productDao.selectProductByProductNo(product_no);
   }
   
   @Override
   public void editProduct(ProductDto productDto) {
      productDao.updateProduct(productDto);
   }
   
   @Override
   public void editProductName(String product_name, int productNo) {
      productDao.updateProductName(product_name, productNo);
   };
   
   @Override
   public void editProductUrl(String url, int productNo) {
      productDao.updateProductUrl(url, productNo); 
   };
   
   @Override
   public void editProductCategory_no(int category_no, int productNo) {
      productDao.updateProductCategory_no(category_no, productNo); 
   };
   
   @Override
   public void editProductMan(String man, int productNo) {
      productDao.updateProductMan(man, productNo);
   }; 
   
   @Override
   public void editProductImg(String img, int productNo) {
      productDao.updateProductImg(img, productNo);
   }; 
   
	@Override
	@Transactional
	public void putFavorite(String userId, int productNo) {
		productDao.insertFavoriteTblFavorite(userId, productNo);
		// productDao.updateFavoriteTblProduct(userId, productNo);
	};
	
	// 샵 : 아름 찜하기 삭제
	@Override
	@Transactional
	public void removeFavorite(String userId, int productNo) {
		productDao.removeFavoriteTblFavorite(userId, productNo);
		// productDao.removeFavoriteTblProduct(userId);
	};
   
   // 샵 : 지도 추가
   public void saveShopResult(ShopResultDto shopResultDto) {
	   productDao.insertShopList(shopResultDto);
   };
   
   // 샵 : 지도리스트 가져오기
   public List<ShopResultDto> searchShopList(SearchDto search) {
		search.calcPage(productDao.selectTotalShop());
		int page = search.getOffset();
		int cnt = search.getListSizePerTime();
		return productDao.selectShopList(page, cnt);
	}
   
   // 전체 샵 리스트 가져오기
   public List<ShopResultDto> getShopAllList() {
	   return productDao.selectAllShops();
   };
   
	@Override
	public List<FavoriteDto> getProductNoByUserId(String userId) {
		return productDao.selectProductNoByUserId(userId);
	};

   
}

