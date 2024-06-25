// To Top
function moveToTop() {
	const btnTop = document.querySelector('.btn-top');
	btnTop.addEventListener('click', (e)=> {
		let winTop = 0;
		window.scrollTo({
			top:winTop,
			behavior:'smooth'
		});
	});
}

// 형제요소 가져오기
function getSiblings(el) {
    const children = el.parentElement.children;
    const tempArr = [];
    for(let i=0; i<children.length; i++) {
        tempArr.push(children[i]);
    }
    return tempArr.filter((e)=> {
        return e !== el;
    })
}

// 공통 탭
const tab = function() {
    let tabNav = document.querySelectorAll('.tab-nav-item');
    let tabContent = document.querySelectorAll('.tab-content');
    let siblings = (el) => [...el.parentElement.children].filter(node => node !== el);
    
    // e.preventDefault();
    for(let i=0; i<tabNav.length; i++) {
        tabNav[i].addEventListener('click', function(e) {
            let currentTab = tabNav[i];
            let currentTabCon = tabContent[i];
    
            siblings(currentTab).forEach(el => el.classList.remove('active'));
            siblings(currentTabCon).forEach(el=>el.classList.remove('active'));

            currentTab.classList.add('active');
            currentTabCon.classList.add('active');
        });
    }
}

// 사이드메뉴 토글
function toggleSideMenu() {
    const li = document.querySelectorAll('.side-menu-ul > li');
    for(let i=0; i<li.length; i++) {
        li[i].addEventListener('click', (e)=> {
            const depth2 = li[i].children[0]; // side-depth2-container
            const siblingLi = getSiblings(depth2.parentNode);
            if(depth2.classList.contains('side-depth2-container')) {
                if(depth2.childNodes[3].classList.contains('active')) {
                    depth2.childNodes[3].classList.remove('active');
                }
                else {
                    depth2.childNodes[3].classList.add('active');
                }
            }
        })
    }
}

/*// 팝업 닫기
function closePopup(el) {
    // console.log(el)
    el.addEventListener('click', (e)=> {
        if(el.className === 'popup-bg') {
            el.parentElement.classList.remove('active');
            el.remove();
        }
        if(el.className === 'popup-close') {
            el.parentElement.parentElement.classList.remove('active');
            el.parentElement.nextElementSibling.remove();
        }
    });
}*/

// 팝업 닫기 함수
function closePopup(el) {
    el.addEventListener('click', (e) => {
        if (el.className === 'popup-bg') {
            el.parentElement.classList.remove('active');
            el.remove();
        }
        if (el.className === 'popup-close') {
            el.parentElement.parentElement.classList.remove('active');
            el.parentElement.nextElementSibling.remove();
        }
    });
}

// 마이플랜트 팝업 열기
function openPopup() {
    const btnPopup = document.querySelectorAll('.btn-popup');
    const div = document.createElement('div');

    for(let i=0; i<btnPopup.length; i++) {
        btnPopup[i].addEventListener('click', (e)=> {
			// let nowPopup = document.querySelector(".popup").parentElement;
			let nowPopup = btnPopup[i].parentElement.parentElement.nextElementSibling;
			let nowBg = nowPopup.querySelector('.popup-bg');
			let nowBtnClose = nowPopup.querySelector('.popup-close');

			e.preventDefault();
            if (!nowBg) {
				console.log(nowBg);
				nowPopup.classList.add('active');
				nowPopup.appendChild(div).classList.add('popup-bg');
            }
            
            getSiblings(nowPopup).forEach(el => el.classList.remove('active'));
            closePopup(nowBg || div);
            closePopup(nowBtnClose);
        })
    }
}

// 팝업 열기 - 종현
function openPopup2() {
    const btnPopup = document.querySelectorAll('.btn-adminPopup');
    const popup = document.querySelectorAll('.popup-container');
    const div = document.createElement('div');

    for(let i=0; i<btnPopup.length; i++) {
        btnPopup[i].addEventListener('click', (e)=> {
            let idx = i;
            let nowPopup = popup[idx];
            let nowBg = nowPopup.querySelector('.popup-bg'); // 수정된 부분
            let btnClose = popup[i].children[0].children;
            
            e.defaultPrevented;
            nowPopup.classList.add('active');
            if (!nowBg) { // 수정된 부분
                nowPopup.appendChild(div).classList.add('popup-bg'); // 수정된 부분
            } // 수정된 부분
            getSiblings(nowPopup).forEach(el => el.classList.remove('active'));
            closePopup(nowBg || div); // 수정된 부분
            closePopup(btnClose[0]);
        })
    }
}

// 팝업 열기2 - 종현
function openPopup22() {
    const btnPopup = document.querySelectorAll('.btn-adminPopup2');
    const popup = document.querySelectorAll('.popup-container2');
    const div = document.createElement('div');

    for (let i = 0; i < btnPopup.length; i++) {
        btnPopup[i].addEventListener('click', (e) => {
            e.preventDefault(); // 기본 동작 방지
            let idx = i;
            let nowPopup = popup[idx];
            let nowBg = nowPopup.querySelector('.popup-bg');
            let btnClose = nowPopup.querySelector('.popup-close');

            nowPopup.classList.add('active');
            if (!nowBg) {
                nowPopup.appendChild(div).classList.add('popup-bg');
            }

            // 형제 요소들이 active 상태가 아니도록 설정
            getSiblings(nowPopup).forEach(el => el.classList.remove('active'));
            closePopup(nowBg || div);
            closePopup(btnClose);
        });
    }
}

// 형제 요소 가져오기
function getSiblings(elem) {
    let siblings = [];
    let sibling = elem.parentNode.firstChild;
    for (; sibling; sibling = sibling.nextSibling) {
        if (sibling.nodeType === 1 && sibling !== elem) {
            siblings.push(sibling);
        }
    }
    return siblings;
}

// 커뮤니티 글쓰기 팝업 열기
function openWritePopup() {
    const btnPopup = document.querySelectorAll('.btn-writePopup');
    const div = document.createElement('div');

    for(let i=0; i<btnPopup.length; i++) {
        btnPopup[i].addEventListener('click', (e)=> {
			let nowPopup = document.querySelector(".popup").parentElement;
			let nowBg = nowPopup.querySelector('.popup-bg');
			let nowBtnClose = nowPopup.querySelector('.popup-close');

			e.preventDefault();
            if (!nowBg) {
				console.log(nowBg);
				nowPopup.classList.add('active');
				nowPopup.appendChild(div).classList.add('popup-bg');
            }
            
            getSiblings(nowPopup).forEach(el => el.classList.remove('active'));
            closePopup(nowBg || div);
            closePopup(nowBtnClose);
        })
    }
}

// 업로드 이미지 미리보기
function readURL(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    var previewImgs = document.querySelectorAll('.preview');
    reader.onload = function(e) {
		for(let i=0; i<previewImgs.length; i++) {
			previewImgs[i].src = e.target.result;
		}
      //document.querySelector('.preview').src = e.target.result;
    };
    reader.readAsDataURL(input.files[0]);
  } else {
    document.querySelector('.preview').src = "";
  }
}

// 글 삭제
function deletePost() {
	window.confirm("식물을 삭제하시겠습니까?");
}

// 더보기 토글
const showMoreContent = function() {
    let showMoreTxt = document.querySelectorAll('.show-more');
    let hiddenContent = document.querySelectorAll('.general-info');
    
    for(let i=0; i<showMoreTxt.length; i++) {
		let txt = showMoreTxt[i];
		let content = hiddenContent[i];
		let card = showMoreTxt[i].parentElement;
		
		txt.innerText = '펼쳐보기';
        txt.addEventListener('click', function(e) {
			e.preventDefault;
			if (!content.classList.contains('active')) {
                content.classList.add('active');
                card.classList.add('active');
                txt.classList.add('active');
                txt.innerText = '접기';
            }
            else {
				txt.innerText = '펼쳐보기';
				txt.classList.remove('active');
				content.classList.remove('active');
				card.classList.remove('active');
			}
        });
    }
}


document.addEventListener('DOMContentLoaded', function() {
    moveToTop();
    tab();
    openPopup();
    openWritePopup();
    toggleSideMenu();
    showMoreContent();
});


// 관리자 페이지 사이드바
document.addEventListener("DOMContentLoaded", function() {
    // 모든 depth1-title 요소들을 가져온다
    var menuItems = document.querySelectorAll(".depth1-title");

    // 현재 페이지 URL 가져오기
    var currentUrl = window.location.pathname;

    // 각 메뉴 항목을 순회하며 현재 URL과 비교
    menuItems.forEach(function(item) {
        var itemUrl = item.getAttribute("href");
        if (currentUrl === itemUrl) {
            item.classList.add("menu-active");
        }

        // 클릭 이벤트 리스너 추가
        item.addEventListener("click", function(event) {
            // 모든 항목에서 menu-active 클래스 제거
            menuItems.forEach(function(menuItem) {
                menuItem.classList.remove("menu-active");
            });

            // 클릭된 항목에 menu-active 클래스 추가
            event.currentTarget.classList.add("menu-active");
        });
    });
});