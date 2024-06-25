package com.greenfinal.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.greenfinal.dto.*;

@Mapper
@Repository("productDao")
public interface ProductDao {

	@Select("SELECT * FROM product")
	public List<ProductDto> selectAllProducts() throws DataAccessException;

	@Select("SELECT count(*) FROM product")
	public int selectTotalProductCnt() throws DataAccessException;
	
	@Select("SELECT * FROM product WHERE favorite=1")
	public List<ProductDto> selectFavoriteProducts() throws DataAccessException;

	@Select("SELECT * from product limit #{offset}, #{count}")
	public List<ProductDto> selectProductList(@Param("offset") int offset, @Param("count") int count);

	   // 상품 삭제
    @Delete({
        "<script>",
        "DELETE FROM product WHERE product_no IN ",
        "<foreach item='productNo' collection='productNos' open='(' separator=',' close=')'>",
        "#{productNo}",
        "</foreach>",
        "</script>"
    })
    void deleteProducts(@Param("productNos") List<Integer> productNos);
    
	@Select("SELECT * FROM product WHERE manufacturer LIKE CONCAT('%', #{keyword}, '%') OR product_name LIKE CONCAT('%', #{keyword}, '%')")
	public List<ProductDto> selectSearchProduct(@Param("keyword") String keyword) throws DataAccessException;
	
   @Select("SELECT * FROM product WHERE product_no=#{product_no}")
   public ProductDto selectProductByProductNo(int product_no) throws DataAccessException;
   
   @Update("UPDATE product "
         + "SET product_name=#{product_name}, "
         + "url=#{url}, "
         + "manufacturer=#{manufacturer}, "
         + "img_path=#{img_path} "
         + "WHERE product_no=#{product_no}")
   public void updateProduct(ProductDto productDto) throws DataAccessException;
   
   @Update("UPDATE product SET product_name=#{product_name} WHERE product_no=#{productNo}")
   public void updateProductName(@Param("product_name") String productName, @Param("productNo") int productNo) throws DataAccessException;

   @Update("UPDATE product SET url=#{url} WHERE product_no=#{productNo}")
   public void updateProductUrl(@Param("url") String url, @Param("productNo") int productNo) throws DataAccessException;

   @Update("UPDATE product SET category_no=#{category_no} WHERE product_no=#{productNo}")
   public void updateProductCategory_no(@Param("category_no") int category_no, @Param("productNo") int productNo) throws DataAccessException;
   
   @Update("UPDATE product SET manufacturer=#{man} WHERE product_no=#{productNo}")
   public void updateProductMan(@Param("man") String man, @Param("productNo") int productNo) throws DataAccessException;

   @Update("UPDATE product SET img_path=#{img} WHERE product_no=#{productNo}")
   public void updateProductImg(@Param("img") String img, @Param("productNo") int productNo) throws DataAccessException;

   @Select("SELECT * FROM product where category_no=#{categoryNo}")
   public List<ProductDto> selectProductByCategoryNo(@Param("categoryNo") int categoryNo);

   @Select("SELECT * FROM category")
   public List<CategoryDto> selectAllCategories();

   @Select("SELECT * FROM product WHERE product_no=#{productNo}")
	public ProductDto selectProductByNo(@Param("productNo") int productNo);

   // 샵 찜하기 추가
   @Insert("INSERT INTO favorite (user_id, product_no, favorite) VALUES (#{userId}, #{productNo}, 1)")
   public int insertFavoriteTblFavorite(@Param("userId") String userId, @Param("productNo") int plantNo) throws DataAccessException;
   
   // 샵 찜하기 제거
   @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND product_no = #{productNo}")
   public int removeFavoriteTblFavorite(@Param("userId") String userId, @Param("productNo") int productNo) throws DataAccessException;
   
   // 샵 찜하기 리스트 출력
   @Select("SELECT DISTINCT p.* FROM product p JOIN favorite f ON p.product_no = f.product_no WHERE f.user_id = #{userId} AND f.favorite = 1")
   public List<ProductDto> selectFavoriteProductByUserId(@Param("userId") String userId) throws DataAccessException;
   
   // 샵 찜된 제품 출력
   @Select("SELECT product_no FROM favorite WHERE user_id = #{userId} AND favorite = 1")
   public List<FavoriteDto> selectProductNoByUserId(@Param("userId") String userId) throws DataAccessException;
   
   // 샵 지도에서 json 리스트 추가/업데이트
   @Insert("INSERT INTO shop (address_name, category_group_code, category_group_name, category_name, distance, id, phone, place_name, place_url, road_address_name, x, y) "
		   + "VALUES (#{address_name}, #{category_group_code}, #{category_group_name}, #{category_name}, #{distance}, #{id}, #{phone}, #{place_name}, #{place_url}, #{road_address_name}, #{x}, #{y}) "
		   + "ON DUPLICATE KEY UPDATE "
		   + "address_name = #{address_name}, "
		   + "category_group_code = #{category_group_code}, "
		   + "category_group_name = #{category_group_name}, "
		   + "category_name = #{category_name}, "
		   + "distance = #{distance}, "
		   + "phone = #{phone}, "
		   + "place_name = #{place_name}, "
		   + "place_url = #{place_url}, "
		   + "road_address_name = #{road_address_name}, "
		   + "x = #{x}, "
		   + "y = #{y}")
		   public void insertShopList(ShopResultDto shopResultDto) throws DataAccessException;
   
   // 샵리스트 전체개수
   @Select("SELECT count(*) FROM shop")
   public int selectTotalShop() throws DataAccessException;
   
   // 샵 리스트 가져오기
   @Select("SELECT * from shop limit #{offset}, #{count}")
	public List<ShopResultDto> selectShopList(@Param("offset") int offset, @Param("count") int count);

   // 전체 샵 리스트
   @Select("SELECT * FROM shop")
   public List<ShopResultDto> selectAllShops();
   
   // 종현
   @Select("select * from product order by product_no Desc")
   public List<ProductDto> selectProduct() throws DataAccessException;
   
	// next_no가 null인지 확인
	@Select("select count(*) from product")
	public boolean booleanMaxNo() throws DataAccessException;
	
	// next_no 최댓값 들고오기
	@Select("select max(product_no) from product")
	public int selectMaxNo() throws DataAccessException;
   
	@Insert("INSERT INTO product(product_no, product_name, url, category_no, manufacturer, "
         + "img_path) "
         + "VALUES(#{product_no}, #{product_name}, #{url}, #{category_no}, #{manufacturer}, "
         + "#{img_path})")
	public void insertProduct(ProductDto ProductDto) throws DataAccessException;
}
