// 메인 게임 영역 탭
function mainTopTab(btn) {
    const gameContainer = document.querySelector('.main-game-room');
    const myplantContainer = document.querySelector('.main-myplant-room');
    const btnGame = document.querySelector('.btn-game');
    const btnMyplant = document.querySelector('.btn-myplant');

    btnGame.classList.remove('active');
    btnMyplant.classList.remove('active');

    if(btn === btnGame) {
        gameContainer.classList.add('active');
        myplantContainer.classList.remove('active');
        btnGame.classList.add('active');
    }
    if(btn === btnMyplant) {
        myplantContainer.classList.add('active');
        gameContainer.classList.remove('active');
        btnMyplant.classList.add('active');
    }
}

// 프로덕트 리스트 ajax
function getMainProductAjax(page) {
	// window.alert("page=" + page);
	var sndurl = '/main/' + page;
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
		            temp += '<a href="/shop/shop_product/'+ dto.product_no +'" class="dis-block w-100" target="_blank">';
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
		        temp += '<div class="page-container">';
		        temp += '<div class="page">';

		        if (responseData.page && nowPage > 1) {
		            temp += '<button type="button" class="btn-prev active" onclick="getMainProductAjax(' + (nowPage - 1) + ')">prev</button>';
		        }
		
		        temp += '<div class="flex-container ml-6 mr-6">';
		        temp += '<div class="font-size-13 mr-10 ml-10"><span class="txt-green">요즘 뜨는 식물 샵</span>&nbsp;더보기&nbsp;</div>';
		        temp += '<div class="txt-gray font-size-12">';
		        
		        // 페이지 정보 출력
		        temp += '<span class="txt-green">' + nowPage + '</span><span class="txt-gray"> / ' + totalGroups + '&nbsp;</span>';
		        temp += '</div>';
		        temp += '</div>';
		
		        if (responseData.page && nowPage <= endPage) {
		            temp += '<button type="button" class="btn-next active" onclick="getMainProductAjax(' + (nowPage + 1) + ')">next</button>';
		        }
		        
		        temp += '</div>';
		        temp += '</div>';
		
		        $("#main_product_json").html(temp);
		    },
		    error: function(data, textStatus) {
		        alert("에러가 발생했습니다.");
		    }
		});
}

// 샵 리스트 ajax
function getMainShopAjax(page) {
	// window.alert("page=" + page);
	var sndurl = '/main/shop/' + page;
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
                    temp += '<p class="font-size-13 txt-bold mb-4">'+ dto.place_name +'</p>';
                    temp += '<p class="font-size-10 mb-4">'+ dto.address_name +'</p>';
                    temp += '<p>';
                    temp += '<i class="material-icons mr-4" style="font-size:12px;">&#xe32c;</i>'
                	temp +=	'<span class="font-size-10">' + dto.phone + '</span>';
                    temp += '</p>';
                    temp += '</div>';
		            temp += '</a>';
		            temp += '</div>';
		        });
		        
		        temp += '</div>';
		        temp += '<div class="page-container">';
		        temp += '<div class="page">';

		        if (responseData.page && nowPage > 1) {
		            temp += '<button type="button" class="btn-prev active" onclick="getMainShopAjax(' + (nowPage - 1) + ')">prev</button>';
		        }
		
		        temp += '<div class="flex-container ml-6 mr-6">';
		        temp += '<div class="font-size-13 mr-10 ml-10"><span class="txt-green">우리 동네 추천 샵</span>&nbsp;더보기&nbsp;</div>';
		        temp += '<div class="txt-gray font-size-12">';
		        
		        // 페이지 정보 출력
		        temp += '<span class="txt-green">' + nowPage + '</span><span class="txt-gray"> / ' + totalGroups + '&nbsp;</span>';
		        temp += '</div>';
		        temp += '</div>';
		
		        if (responseData.page && nowPage <= endPage) {
		            temp += '<button type="button" class="btn-next active" onclick="getMainShopAjax(' + (nowPage + 1) + ')">next</button>';
		        }
		        
		        temp += '</div>';
		        temp += '</div>';
		
		        $("#main_shop_json").html(temp);
		    },
		    error: function(data, textStatus) {
		        alert("에러가 발생했습니다.");
		    }
		});
}





