// 알쓸
function getArticleForm() {
   $.ajax ({
      type : "get",
      async : false,
      url : "/articleForm.do",
      data : "json",
      success : function(data, textStatus) {
         var responseData = JSON.parse(data);
         console.log(responseData);
         
         if (responseData.nextNo) {
         $("#post_no").val(responseData.nextNo);    
       }
         
         if (responseData.user_id) {
         $("#user_id").val(responseData.user_id);    
       }
      },
      error : function() {
         console.error("에러가 났습니다");
      },
      complete : function() {
      }
   });
}

function saveArticle() {
    let urlPattern = /^(ftp|http|https):\/\/[^ "]+$/;
    const maxFiles = 1;

    if(writeArticle.title.value == "") {
        window.alert("글 제목이 입력되지 않았습니다.");
        writeArticle.title.focus();
        return false;
    }
    else if(writeArticle.p_content.value == "") {
        window.alert("글 내용이 입력되지 않았습니다.");
        writeArticle.p_content.focus();
        return false;
    }
    else if(writeArticle.keyword.value == "") {
        window.alert("키워드가 입력되지 않았습니다.");
        writeArticle.keyword.focus();
        return false;
    }
    else if (writeArticle.keyword.value.includes("#")) { // 키워드에 #이 있는지 확인
        window.alert("키워드에 # 문자를 제거해주세요.");
        writeArticle.keyword.focus();
        return false;
    }    
    else {
        // 키워드를 공백으로 분리하여 단어의 개수를 세기
        const keywordWords = writeArticle.keyword.value.trim().split(/\s+/);
        if (keywordWords.length > 3) { // 단어가 3개 이상인지 확인
            window.alert("키워드에 단어는 최대 3개까지 가능합니다.");
            writeArticle.keyword.focus();
            return false;
        }
        else if(writeArticle.location.value == "") {
        window.alert("기사 링크가 입력되지 않았습니다.");
        writeArticle.location.focus();
        return false;
    }
    else if(!urlPattern.test(writeArticle.location.value)) {
        window.alert("유효하지 않은 기사 링크입니다. URL 형식을 확인하세요.");
        writeArticle.location.focus();
        return false;
    }
        // 모든 단어 앞에 # 붙이기
        writeArticle.keyword.value = keywordWords.map(word => `#${word}`).join(' ');
    }
     if (fileInput.files.length > maxFiles) {
        window.alert(`최대 ${maxFiles}개의 파일만 업로드할 수 있습니다.`);
        return false;
    } else {
        if (confirm("글을 등록하시겠습니까?")) {
            writeArticle.submit();
        } else {
            window.alert("취소되었습니다.");
            return false;
        }
    }
}
    
function readURL3(input) {
  if (input.files && input.files[0]) {
    // 파일이 하나 이상 선택되었을 때만 미리보기 이미지 업데이트
    var previewImgs = document.querySelectorAll('.preview');
    if (input.files.length > 1) {
      alert("사진 등록은 한 장만 가능합니다.");
      input.value = ""; // 파일 선택 초기화
      return;
    }
    var reader = new FileReader();
    reader.onload = function(e) {
      for (let i = 0; i < previewImgs.length; i++) {
        previewImgs[i].src = e.target.result;
      }
    };
    reader.readAsDataURL(input.files[0]); // 파일을 읽어서 데이터 URL로 변환하여 이미지로 표시
  }
}
    

// 초기화
function resetArticleForm() {
   window.alert("정보를 지우고 처음부터 다시 입력합니다.");
   writeArticle.query.value = '';
   writeArticle.title.value = '';
   writeArticle.keyword.value = '';
   writeArticle.p_content.value = '';
   writeArticle.location.value = '';
   writeArticle.img_url.value = '';
   
   
   // 기존의 모든 미리보기 이미지 초기화
    var previewImgs = document.querySelectorAll('.preview');
    previewImgs.forEach(img => img.src = "/img/no-img.png");
    
    // 파일 입력필드 초기화
    var fileInput = document.getElementById("fileInput");
   fileInput.type = "text"; // 파일 입력 필드를 텍스트 입력 필드로 변경하여 값 초기화
   fileInput.type = "file";
   
   writeArticle.title.focus();

}
/* ------------------------------------------------------------- *//* ------------------------------------------------------------- */

// form.do 가드너스 팝업 - nextNo 부르기
function getPostForm() {
   $.ajax ({
      type : "get",
      async : false,
      url : "/postForm.do",
      data : "json",
      success : function(data, textStatus) {
         var responseData = JSON.parse(data);
         console.log(responseData);
         
         if (responseData.nextNo) {
         $("#post_no").val(responseData.nextNo);    
       }
         
         if (responseData.user_id) {
         $("#user_id").val(responseData.user_id);    
       }
       
       if (responseData.nickname) {
         $("#nickname").val(responseData.nickname);    
       }
      },
      error : function() {
         console.error("에러가 났습니다");
      },
      complete : function() {
//        console.complete("요청이 완료됐습니다시마");
      }
   });
}

/* writePost */
function savePost() {
   const maxFiles = 5; // 업로드할 수 있는 최대 파일 개수
    const fileInput = document.getElementById("fileInput");
 //   const keywordWords = writePost.keyword.value.trim().split(/\s+/);
   
   if(writePost.title.value == "") {
      window.alert("글 제목이 입력되지 않았습니다.");
      writePost.title.focus();
      return false;
      }
      else if (writePost.p_content.value == "") {
        window.alert("글 내용이 입력되지 않았습니다.");
        writePost.p_content.focus();
        return false;
   }
   else if(writePost.keyword.value == "") {
        window.alert("키워드가 입력되지 않았습니다.");
        writePost.keyword.focus();
        return false;
    }
    else if (writePost.keyword.value.includes("#")) { // 키워드에 #이 있는지 확인
        window.alert("키워드에 # 문자를 제거해주세요.");
        writePost.keyword.focus();
        return false;
    }    
    else {
        // 키워드를 공백으로 분리하여 단어의 개수를 세기
        const keywordWords = writePost.keyword.value.trim().split(/\s+/);
        if (keywordWords.length > 3) { // 단어가 3개 이상인지 확인
            window.alert("키워드에 단어는 최대 3개까지 가능합니다.");
            writePost.keyword.focus();
            return false;
        }
        // 모든 단어 앞에 # 붙이기
        writePost.keyword.value = keywordWords.map(word => `#${word}`).join(' ');
    }
     if (fileInput.files.length > maxFiles) {
        window.alert(`최대 ${maxFiles}개의 파일만 업로드할 수 있습니다.`);
        return false;
    } else {
        if (confirm("글을 등록하시겠습니까?")) {
            writePost.submit();
        } else {
            window.alert("취소되었습니다.");
            return false;
        }
    }
}

// 업로드 이미지 미리보기
function readURL2(input) {
  if (input.files) {
    var maxFiles = 5;
    var previewImgs = document.querySelectorAll('.preview');
    
    // 선택된 파일 수가 5개를 초과하는 경우 경고 메시지 표시
    if (input.files.length > maxFiles) {
      alert("사진 등록은 5장까지 가능합니다.");
      input.value = "";  // 파일 선택 초기화
      return;
    }
    
    // 기존의 모든 미리보기 이미지 초기화
    previewImgs.forEach(img => img.src = "/img/no-img.png");

    // 선택된 파일을 순회하면서 각각의 미리보기 이미지에 할당
    for (let i = 0; i < input.files.length && i < previewImgs.length; i++) {
      var reader = new FileReader();
      reader.onload = (function(img) {
        return function(e) {
          img.src = e.target.result;
        };
      })(previewImgs[i]);

      reader.readAsDataURL(input.files[i]);
    }
  }
}

// 초기화
function resetForm() {
   
   window.alert("정보를 지우고 처음부터 다시 입력합니다.");
   writePost.title.value = '';
   writePost.p_content.value = '';
   writePost.keyword.value = '';
   document.getElementById("fileInput").value = '';
   writePost.title.focus();
   
   // 기존의 모든 미리보기 이미지 초기화
    var previewImgs = document.querySelectorAll('.preview');
    previewImgs.forEach(img => img.src = "/img/no-img.png");
}

// 폼의 submit 버튼에 save 함수를 연결
document.addEventListener('DOMContentLoaded', function() {
   document.getElementById('diarySave').addEventListener('click', function(event) {
      event.preventDefault(); // 기본 클릭 동작 중지
      savePost(); // savePost 함수 호출
   });
});

// 폼의 reset 버튼에 reset 함수를 연결
document.addEventListener('DOMContentLoaded', function() {
   document.getElementById('diaryReset').addEventListener('click', function(event) {
      event.preventDefault(); // 기본 클릭 동작 중지
      resetForm(); // savePost 함수 호출
   });
});

/* ------------------------------------------------------------- */

function postEdit(post_no) {
    $.ajax({
        type: "get",
        async: false,
        url: '/community/editPost/' + post_no,
        dataType: "json", // 응답 데이터 유형을 JSON으로 설정
        success: function(responseData, textStatus) {
            console.log(responseData);

            if (responseData.pno) {
                $("#post_no").val(responseData.pno);    
            }

            if (responseData.bno) {
                $("#board_no").val(responseData.bno);    
            }

            if (responseData.userId) {
                $("#user_id").val(responseData.userId);    
            }

            if (responseData.title) {
                $("#title").val(responseData.title);    
            }

            if (responseData.content) {
                $("#p_content").val(responseData.content);    
            }
        },
        error : function() {
           console.error("에러가 났습니다");
         },
         complete : function() {
         console.complete("요청이 완료됐습니다");
      }
    });
 }  
   function postDelete(post_no) {
      uri = '/community/deletePost/'+post_no;
      var confirmed = confirm("정말 삭제하시겠습니까?");
      
      if(confirmed) {
      
      window.location.href = uri;        
     }
     else {
        window.alert("취소되었습니다.");
     }
}

/* editPost */
function saveEdit() {
   const maxFiles = 5; // 업로드할 수 있는 최대 파일 개수
    const fileInput = document.getElementById("fileInput");
    const editPost = document.forms['editPost'];
   
   if(editPost.title.value == "") {
      window.alert("글 제목이 입력되지 않았습니다.");
      editPost.title.focus();
      return false;
   }
   else if(editPost.p_content.value == "") {
      window.alert("글 내용이 입력되지 않았습니다.");
      editPost.p_content.focus();
      return false;
   }
    else if (fileInput.files.length > maxFiles) {
        window.alert(`최대 ${maxFiles}개의 파일만 업로드할 수 있습니다.`);
        return false;
   }
   else {
      if(confirm("글을 수정하시겠습니까?")) {
         editPost.submit();
      }
      else {
         window.alert("글 수정이 취소되었습니다.");
         return false;
      }
   }
}

// 초기화
function resetEdit() {
   window.alert("정보를 지우고 처음부터 다시 입력합니다.");
   editPost.title.value = '';
   editPost.p_content.value = '';
   editPost.title.focus();
}

// 폼의 reset 버튼에 reset 함수를 연결
document.addEventListener('DOMContentLoaded', function() {
   document.getElementById('editReset').addEventListener('click', function(event) {
      event.preventDefault(); // 기본 클릭 동작 중지
      resetEdit(); // savePost 함수 호출
   });
});

/* ------------------------------------------------------------- */

/* postView - 댓글 저장 */
function saveReply(btn) {
   // const reply = document.querySelector('#reply');
   const reply = document.forms['reply'];
   
   if(reply.r_content.value == "") {
      window.alert("입력된 글이 없습니다.");
      reply.r_content.focus();
   }
   else {
      if(confirm("글을 등록하시겠습니까?")) {
         reply.submit();
      }
      else {
         window.alert("취소되었습니다.");
      }
   }
}

/* postView p_content box크기 */
function resize(obj) {
   obj.style.height = '1px';
   obj.style.height = (12 + obj.scrollHeight) + 'px';
} 


/* 가드너스 postView 들어갈 때 오류 메시지 */
function communityView() {
   var msg = '로그인이 필요한 서비스입니다.';
   var viewUrl = '/community';
   var loginUrl = '/login/login.do';
   
   if (userId == null) { 
      alert(msg);
      location.href = loginUrl;
   }
   else if (userId != null) {
      location.href = viewUrl;
   }
}

function postView(post_no) {
   var msg = '좋아요 중복 오류입니다.';
   var url = '/community/postView/'+post_no;
   
   alert(msg);
   location.href = url;
}

function likeBtn(button) {
    var postNo = button.dataset.postNo; // 버튼의 data-post-no 속성 값
    var userId = button.dataset.userId; // 버튼의 data-user-id 속성 값

    console.log('data-post-no:', postNo);
    console.log('data-user-id:', userId);
    
    var msg = '좋아요';
    var url = '/community/postView/' + postNo + '/' + userId + '/like.do';
    
      $.ajax({
        type: 'POST',
        url: url,
        async: false,
        dataType: "json",
        data: { postNo: postNo, userId: userId },
        success: function(response) {
         
         if (response.likeStatus) {
               $(button).addClass('liked');
               $(button).find('i.material-icons').html('&#xe87d;');
               $('.like').text(response.likeCnt);
            }
            else {
            	$(button).removeClass('liked');
                $(button).find('i.material-icons').html('&#xe87d;');
                $('.like').text(response.likeCnt);
         }
            
        },
        error: function(xhr, status, error) {
            // 요청이 실패한 경우
            console.error('좋아요 요청이 실패했습니다.');
            console.error('에러: ', error);
        }
    });
}  

/* postView -> 가드너다이어리 */
function goList() {
   var url = '/community/gardenerDiary';
   
   location.href = url;
}

    
/* ------------------------------------------------------------- */    
    
document.addEventListener("DOMContentLoaded", function() {
    const fileInput = document.getElementById("fileInput");
    const fileCountMessage = document.getElementById("fileCountMessage");
    const maxFiles = 5; // 업로드할 수 있는 최대 파일 개수

    fileInput.addEventListener("change", function() {
        const files = fileInput.files;
        if (files.length > maxFiles) {
            fileCountMessage.textContent = `You can upload a maximum of ${maxFiles} files.`;
            fileInput.value = ""; // 파일 입력 초기화
        } else {
            fileCountMessage.textContent = `${files.length} file(s) selected.`;
        }
    });

    document.getElementById("uploadForm").addEventListener("submit", function(event) {
        const files = fileInput.files;
        if (files.length > maxFiles) {
            event.preventDefault();
            alert(`You can upload a maximum of ${maxFiles} files.`);
        }
    });
});    


// ---------------------------------------------------------------------------------------------------------------------------------------------------
              

function replyEdit(reply_no) {
    const preTag = document.getElementById(`replyContent-`+reply_no);
    const inputTag = document.getElementById(`replyEdit-`+reply_no);
    console.log(reply_no);
    console.log(preTag);
    console.log(inputTag);
    
    if (preTag && inputTag) {
        // 이전 댓글 내용을 가져와서 input 요소에 설정
        const content = preTag.textContent.trim();
        inputTag.value = content;
        
        // 이전 댓글 내용을 숨기고 input 요소를 보이게 함
        preTag.style.display = "none";
        inputTag.style.display = "block";
        
        // 수정 완료 버튼을 활성화
        const editButton = document.getElementById(`editButton(`+reply_no+')');
        console.log(editButton);
        if (editButton) {
            editButton.value = "수정 완료";
            editButton.onclick = function() { updateReply(reply_no); }; // 수정 완료 버튼을 누를 때 updateReply 함수 호출
        }
    }
}

function updateReply(reply_no) {
    const preTag = document.getElementById(`replyContent-` + reply_no);
    const inputTag = document.getElementById(`replyEdit-` + reply_no);

    if (preTag && inputTag) {
        const updatedContent = inputTag.value.trim();
        console.log("updatedContent : " + updatedContent);

        // 수정된 내용을 서버에 전송
        $.ajax({
            url: `/community/updateReply/` + reply_no,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ r_content: updatedContent }),
            success: function(data) {
                // 수정된 내용을 이전 댓글 내용에 반영
                preTag.textContent = data.r_content;
                console.log("수정된 댓글 내용:", data.r_content);
                location.reload(); // 페이지 새로고침
                preTag.style.display = "block";
                inputTag.style.display = "none";
                

                // 수정 완료 버튼을 다시 수정 버튼으로 변경
                const editButton = document.getElementById(`editButton(` + reply_no + `)`);
                if (editButton) {
                    editButton.value = "수정";
                    editButton.onclick = function() { replyEdit(reply_no); }; // 다시 수정 버튼을 누를 때 replyEdit 함수 호출
                }
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });
    }
}

function deleteReply(reply_no) {
    if (confirm("정말로 댓글을 삭제하시겠습니까?")) {
        $.ajax({
            url: `/community/deleteReply/` + reply_no,
            type: 'DELETE',
            success: function(response) {
                // 댓글 삭제 성공 시, DOM에서 해당 댓글을 제거
                $(`#reply-${reply_no}`).remove();
                location.reload(); // 페이지 새로고침
                alert("댓글이 성공적으로 삭제되었습니다.");
            },
            error: function(error) {
                console.error('Error:', error);
                alert("댓글 삭제에 실패했습니다.");
            }
        });
    }
}
