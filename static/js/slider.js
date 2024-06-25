// 메인 슬라이더 재생버튼 토글
/*const playBtn = document.querySelector('.btn-play');
//const firstCard = document.queryselector('.main-card:nth-child(1)');
let isBtnPlayed = false;

playBtn.addEventListener('click', (e)=> {
	if(!isBtnPlayed) {
		swiper.autoplay.stop();
		playBtn.innerHTML = 'pause';
		isBtnPlayed = true;
	} else {
		swiper.autoplay.start();
		playBtn.innerHTML = 'play';
		isBtnPlayed = false;
	}
});*/

/*
function convertPercentageToPixels(element) {
  // 부모 컨테이너의 너비를 가져옴
  const parentWidth = element.parentElement.offsetWidth;
  // 요소의 백분율 너비를 가져옴
  const percentageWidth = parseFloat(window.getComputedStyle(element).width);
  // 백분율 너비를 픽셀 단위로 변환
  const pixelWidth = (percentageWidth / 100) * parentWidth;
  return pixelWidth;
}

document.addEventListener('DOMContentLoaded', () => {
  const element = document.querySelector('.element');
  const pixelWidth = convertPercentageToPixels(element);
  console.log(`Element width in pixels: ${pixelWidth}px`);
});

// 창 크기 조절 시 픽셀 너비를 다시 계산하여 출력
window.addEventListener('resize', () => {
  const element = document.querySelector('.element');
  const pixelWidth = convertPercentageToPixels(element);
  console.log(`Element width in pixels after resize: ${pixelWidth}px`);
}); */

document.addEventListener('DOMContentLoaded', ()=> {
	const mainSwiper = new Swiper('#main-post-swiper', {
		slidesPerView: 8,
		slidesPerGroup: 1,
		spaceBetween: 40,
		autoplay:{
			  delay: 6000, // 시간 설정
	          disableOnInteraction: false, // false-스와이프 후 자동 재생
	    loop: true,
	  
		},
	    pagination : {   // 페이저 버튼 사용자 설정
	        el: '.swiper-pagination',  // 페이저 버튼을 담을 태그 설정
	        clickable: true,  // 버튼 클릭 여부
	        type: 'bullets', // 버튼 모양 결정 "bullets", "fraction" 
	    },
	});
	
	// 쇼핑 : 샵 슬라이더
	const shopSwiper = new Swiper('#shop-home-swiper', {
	  autoplay:{
			  delay: 6000, // 시간 설정
	          disableOnInteraction: false, // false-스와이프 후 자동 재생
	    loop: true,
	  
		},	
	  pagination: {
	        el: '.swiper-pagination',
	        clickable: true,  // 버튼 클릭 여부
	        type: 'bullets', // 버튼 모양 결정 "bullets", "fraction" 
	    },
	});
	
	// 쇼핑 : 포스트 슬라이더
	const postSwiper = new Swiper('#shop-post-swiper', {
		slidesPerView: 6,
		slidesPerGroup: 1,
		spaceBetween: 20,
		centeredSlides: true,
		autoplay:{
			delay: 6000, // 시간 설정
			disableOnInteraction: false, // false-스와이프 후 자동 재생
			loop: false,
		  
			},	
		scrollbar:{
			el : '.shop-post-swiper-scrollbar',
	        draggable: true, 
			dragSize: 200,
	    },
	});
	
	const postImgSwiper = new Swiper('#post-img-swiper', {
		slidesPerView: 3,
		slidesPerGroup: 1,
		// spaceBetween: 20,
		centeredSlides: true,
		autoplay:{
			delay: 6000, // 시간 설정
			disableOnInteraction: false, // false-스와이프 후 자동 재생
			loop: false,
		  
			},
		navigation: {
	         nextEl: ".swiper-button-next",
	         prevEl: ".swiper-button-prev",
		},
	});
})

/*let isBtnPlayed = false;
function playSlider(swiper) {
	const slider = document.getElementById(swiper);
	const playBtn = document.querySelector('.btn-play');
	window.alert(slider.id);
	if(!isBtnPlayed) {
		slider.autoplay.stop();
		playBtn.innerHTML = 'pause';
		isBtnPlayed = true;
	} else {
		slider.autoplay.start();
		playBtn.innerHTML = 'play';
		isBtnPlayed = false;
	}
}*/



