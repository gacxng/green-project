document.addEventListener("DOMContentLoaded", ()=> {
	// testUrl = window.location.href;
	// window.alert(testUrl);
	// getLocalShopAjax();
})


function getProductCategory(category_no) {
	var sndurl = '/shop/' + category_no;
	window.location.href = sndurl;
}

function getCategoryName(category_no) {
	var category = "";
    switch(category_no) {
        case 1 : category = "수경재배식물"; break;
        case 2 : category = "다육식물";  break;
        case 3 : category = "중대형식물";  break;
        case 4 : category = "소형식물";  break;
        case 5 : category = "선인장";  break;
        case 6 : category = "행잉식물";  break;
        case 7 : category = "난/분재";  break;
        case 8 : category = "모종";  break;
        case 9 : category = "씨앗/구근";  break;
        case 10 : category = "화분";  break;
        default: category = "알 수 없는 카테고리";
    }
    return category;
}

function toggleClassSideMenu() {
	let sidemenu = document.querySelectorAll('.product-sidemenu-li');
    for(let i=0; i<sidemenu.length; i++) {
		sidemenu[i].classList.remove('active');
		sidemenu[i].addEventListener('click', (e)=> {
			// window.alert('click');
			if(!sidemenu[i].classList.contains('active')) {
				sidemenu[i].classList.add('active');
			}
		})
	}
}

function getCategorizedProduct(category_no, product_no) {
    // window.alert("category_no : " + category_no);
    // window.alert("product_no : " + product_no);
    toggleClassSideMenu();
    
    var sndurl = '/shop/shop_product/' + product_no + '/' + category_no;
    var _jsonInfo = { 
        product_no: "1",
        product_name: "다육식물",
        url: "",
        category_no: category_no,
        manufacturer: "식물상점",
        p_reply: "",
        img_path: "",
    };

    $.ajax({
        type: 'get',
	    async: false,
	    url: sndurl,
	    contentType: 'application/json',
	    data: { jsonInfo: JSON.stringify(_jsonInfo) },
        success: function(data, textStatus) {
			var responseData = JSON.parse(data);
			//console.log(responseData);
			// 응답 데이터가 올바른지 확인
            if (!responseData || !responseData.list) {
                console.error("Invalid response structure");
                return;
            }
			
            var temp = '<div class="title-container mb-30">';
            temp += '<p class="h3">' + getCategoryName(category_no) + '</p>';
            temp += '</div>';
            temp += '<div class="localshop-slider">';
            temp += '<div class="shop-slide-wrapper">';
            responseData.list.forEach(function(dto) {
				// console.log(dto.product_name)
                temp += '<div class="shop-slide">';
                temp += '<a href="/shop/shop_product/' + dto.product_no + '" class="dis-block w-100" target="_blank">';
                temp += '<div class="img-container">';
                temp += '<img src="' + dto.img_path + '" alt="' + dto.img_name + '" />';
                temp += '</div>';
                temp += '<div class="shop-slide-txt over-hidden">';
                temp += '<span class="font-size-10 txt-overflow">'+ dto.manufacturer +'</span>';
                temp += '<p class="font-size-13 txt-bold txt-overflow mb-6">'+ dto.product_name +'</p>';
                temp += '</div>';
                temp += '</a>';
                temp += '</div>';
            });
            temp += '</div>';
            temp += '</div>';
            if (responseData.list.length === 0) {
                temp += '<p class="txt-green">분류에 해당하는 식물이 없습니다...</p>';
            }
            $("#product_category_json").html(temp);
			
        },
        error: function(xhr, textStatus, errorThrown) {
            alert("에러가 발생했습니다: " + errorThrown);
        }
    });
}


// 내 상품 찜하기
function addProductFavorite(product_no) {
	// window.alert("product_no: " + product_no);
    const favoriteBtn = document.querySelector('#btn-p-' + product_no);
    let favorite = favoriteBtn.classList.contains('active') ? 1 : 0;
    const newFavorite = favorite === 0 ? 1 : 0;

    // 버튼의 상태를 즉시 업데이트 (추후 실패 시 되돌릴 수 있도록)
    if (newFavorite === 1) {
        favoriteBtn.classList.add('active');
        favoriteBtn.innerHTML = '찜 해제';
    } else if (newFavorite === 0) {
        favoriteBtn.classList.remove('active');
        favoriteBtn.innerHTML = '찜 하기';
    }

    var sndurl = '/shop/shop_product/' + product_no + '/favorite/' + newFavorite;
    var _jsonInfo = { product_no: "8", favorite: "0", user_id: "aaa" };

    $.ajax({
        type: 'POST',
        async: false,
        url: sndurl,
        contentType: 'application/json',
        data: JSON.stringify(_jsonInfo), // JSON.stringify로 직접 변환
        success: function(responseData, textStatus) {
            console.log("Full response data:", responseData); // 응답 데이터 전체 출력

            // 응답 데이터가 객체인 경우 배열로 변환
            let favoriteList = Array.isArray(responseData) ? responseData : [responseData];
            
            // 응답 데이터가 올바른지 확인
            if (!favoriteList || favoriteList.length === 0) {
                console.error("Invalid response structure");
                return;
            }

            var temp = '';
            favoriteList.forEach(function(dto) {
				console.log("favoriteList: " + favoriteList)
                temp += '<div class="shop-slide">';
                temp += '<a href="/shop/shop_product/' + dto.product_no + '" class="dis-block w-100">';
                temp += '<div class="img-container">';
                temp += '<img src="' + dto.img_path + '" alt="' + dto.product_name + '" />';
                temp += '</div>';
                temp += '<div class="shop-slide-txt over-hidden">';
                temp += '<span class="font-size-10 txt-overflow">' + dto.manufacturer + '</span>';
                temp += '<p class="font-size-12 txt-medium txt-overflow mb-6">' + dto.product_name + '</p>';
                temp += '</div>';
                temp += '</a>';
                temp += '</div>';
            });
            if (favoriteList.length === 0) {
                temp += '<p class="txt-green">분류에 해당하는 식물이 없습니다...</p>';
            }

            $("#favorite_product_json").html(temp);
             window.location.href = '/shop/shop_product/' + product_no;
        },
        error: function(xhr, textStatus, errorThrown) {
			if (xhr.status === 401) { // 401 Unauthorized
		        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
		        window.location.href = "/login/login.do";
		    } else {
		        alert("에러가 발생했습니다: " + errorThrown);
		    }
        }
    });
}


function getLocalShopAjax(page) {
	// window.alert("page=" + page);
	var sndurl = '/shop/shop_home/' + page;
	var _jsonInfo = '{"product_no":"1", "product_name":"다육식물", "url":"", "category_no":"3", "manufacturer":"식물상점", "p_reply":"", "img_path":""}';

		$.ajax({
		    type: 'get',
		    async: false,
		    url: sndurl,
		    contentType: 'application/json',
		    data: { jsonInfo: _jsonInfo },
		    success: function(data, textStatus) {
				var responseData = JSON.parse(data);
				var nowPage = responseData.page.page;
				var endPage = responseData.page.endPage - 1;
				var totalGroups = responseData.page.pageGroupSize;
				
		        var temp = '<div class="page shop-slide-wrapper">';
		        // window.alert(responseData);
		        responseData.list.forEach(function(dto) {
					temp += '<div class="shop-slide">';
		            temp += '<a href="'+ dto.place_url +'" class="dis-block w-100" target="_blank">';
 					temp += '<div class="shop-slide-txt over-hidden">';
                    temp += '<p class="font-size-16 txt-bold mb-6">'+ dto.place_name +'</p>';
                    temp += '<p class="font-size-12 mb-6">'+ dto.address_name +'</p>';
                    temp += '<p>';
                    temp += '<i class="material-icons mr-4" style="font-size:12px;">&#xe32c;</i>'
                	temp +=	'<span class="font-size-12 mb-8">' + dto.phone + '</span>';
                    temp += '</p>';
                    temp += '</div>';
		            temp += '</a>';
		            temp += '</div>';
		        });
		        
		        temp += '</div>';
		        temp += '<div class="page-container">';
		        temp += '<div class="page">';

		        if (responseData.page && nowPage > 1) {
		            temp += '<button type="button" class="btn-prev active" onclick="getLocalShopAjax(' + (nowPage - 1) + ')">prev</button>';
		        }
		
		        temp += '<div class="flex-container ml-6 mr-6">';
		        temp += '<div class="font-size-13 mr-10 ml-10"><span class="txt-green">우리 동네 추천 샵</span>&nbsp;더보기&nbsp;</div>';
		        temp += '<div class="txt-gray font-size-12">';
		        
		        // 페이지 정보 출력
		        temp += '<span class="txt-green">' + nowPage + '</span><span class="txt-gray"> / ' + totalGroups + '&nbsp;</span>';
		        temp += '</div>';
		        temp += '</div>';
		
		        if (responseData.page && nowPage <= endPage) {
		            temp += '<button type="button" class="btn-next active" onclick="getLocalShopAjax(' + (nowPage + 1) + ')">next</button>';
		        }
		        
		        temp += '</div>';
		        temp += '</div>';
		
		        $("#localshop_json").html(temp);
		    },
		    error: function(xhr, textStatus, errorThrown) {
				if (xhr.status === 401) { // 401 Unauthorized
			        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
			        window.location.href = "/login/login.do";
			    } else {
			        alert("에러가 발생했습니다: " + errorThrown);
			    }
	        }
		});
}
