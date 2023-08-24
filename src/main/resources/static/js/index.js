$(function() {
	let isDetailVisible = false;
	let currentRegionCode = null;
	let currentPage = 0;

	console.log("CURRENT REGION CODE:" + currentRegionCode);
	console.log("CURRENT PAGE:" + currentPage);

    $(document).on("click", function(event) {
        if(isDetailVisible && !$(event.target).closest("#detail-container").length) {
            toggleDetail();
        }
    });

    function toggleDetail() {
        isDetailVisible = !isDetailVisible;
        if(isDetailVisible) {
            $("#detail-container").fadeIn();
        } else {
            $("#detail-container").fadeOut();
            currentPage = 0;
        }
    }

    if (sessionStorage.getItem("accessToken")) {
        let accessToken = sessionStorage.getItem("accessToken");
        console.log("LOGIN USER CHECK");
        sendRequest("/check-auth", "POST", accessToken,
            function (response) {
                if (response) {
                    currentMemberId = response.id;
                    currentNickname = response.nickname;
                    $("#nav-user-info-group").css("display", "flex");
                    $(".nav-userinfo-nickname").text(response.nickname);
                    $("#nav-signin-btn, #nav-signup-btn").hide();
                } else {
                    $("#nav-signin-btn, #nav-signup-btn").show();
                    $("#nav-user-info-group").hide();
                }
            },
            function (error) {
                console.log(error);
            });
    }

    function refreshToken(refreshToken) {
        sessionStorage.removeItem("accessToken");
        console.log("토큰 재발급 요청...");
        console.log(refreshToken == null);
        return $.ajax({
            url: "/auth/refresh-token",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ refreshToken: refreshToken }),
            error: function(error) {
                alert("로그인이 필요합니다.");
                window.location.href = "/";
            }
        });
    }

    // 토큰을 사용하기 위한 편의 함수
    function sendRequest(url, method, requestData, successCallback, errorCallback) {
        $.ajax({
            url: url,
            method: method,
            headers: {
                "Authorization": "Bearer " + sessionStorage.getItem("accessToken"),
                "Content-Type": "application/json"
            },
            data: JSON.stringify(requestData),
            success: function(response) {
                // 현재 Access 토큰이 유효한 경우 본래 요청의 성공 콜백 함수 호출
                successCallback(response);
            },
            error: function(xhr, status, error) {
                console.log("엑세스 토큰 만료");
                console.log(xhr.status);
                // Access 토큰이 만료된 경우 401 응답
                if(xhr.status === 401) {
                    console.log("401 에러 응답 확인");
                    console.log("리프레시 토큰:" + sessionStorage.getItem("refreshToken"));
                    refreshToken(sessionStorage.getItem("refreshToken"))
                    // 체이닝
                    .done(function(tokenResponse) {
                        // 새로 발급 받은 Access 토큰을 세션 스토리지에 저장
                        console.log("발급 받은 토큰:" + tokenResponse.accessToken);
                        sessionStorage.setItem("accessToken", tokenResponse.accessToken);
                        // 본래 요청을 재요청
                        $.ajax({
                            url: url,
                            method: method,
                            headers: {
                                "Authorization": "Bearer " + sessionStorage.getItem("accessToken"),
                                "Content-Type": "application/json"
                            },
                            data: JSON.stringify(requestData),
                            success: function(newResponse) {
                                successCallback(newResponse);
                            },
                            error: function(newError) {
                                errorCallback(newError);
                            }
                        });
                    })
                    .fail(function(error) {
                        errorCallback(error);
                    });
                } else {
                    // 401 응답이 아닌 경우
                    errorCallback(xhr, status, error);
                }
            }
        });
    }

    $("#nav-user-info-group").click(function() {
        $("#user-info-dropdown").toggle();
    });

    $("#nav-logo").on("click", function () {
        window.location.href = "/";
    });

	$(".allPaths").on("mouseover", function() {
		$(window).on("mousemove", function(event) {
			let x = event.clientX;
			let y = event.clientY;
			$("#main-country-name").css({
				"top": y - 60 + "px",
				"left": x + 10 + "px"
			});
		});
		$("#main-country-name").css("opacity", 1);
		$("#country-name-area").text(this.id);
	});

	$(".allPaths").on("mouseleave", function() {
		$("#main-country-name").css("opacity", 0);
	});

    let currentMemberId;
	$(".allPaths").on("click", function() {
		$("#video-container").empty();
		$("#news-container").empty();
		$("#post-content-container").empty();

		const countryName = this.id;
		$(".country-name").text(countryName);

		const regionCode = $(this).data("regioncode");
		console.log("토클 국가 코드:" + regionCode);
		currentRegionCode = regionCode;
		getPopularVideos(regionCode);
		getNews(regionCode);
		getPosts(regionCode, 0);
		event.stopPropagation();
		toggleDetail();
	});

	$("#close-sidebar-btn").on("click", function() {
		toggleDetail();
	});

	// GET NEWS
	function getNews(regionCode) {
		$.ajax({
			url: "/api/news/test",
			type: "GET",
			data: {
				country: regionCode,
			},
            success: function (data) {
                let newsHtml = "";
                data.forEach(function (news) {
                    newsHtml += "<a href='" + news.url + "' target='_blank' class='news-link'>";
                    newsHtml += "<div class='news-title'>" + news.title + "</div>";
                    newsHtml += "<div class='news-contents'>";
                    if (news.urlToImage !== null) {
                        newsHtml += "<div class='news-image'><img src='" + news.urlToImage + "' ></div>";
                    }
                    if (news.description !== null) {
                        newsHtml += "<div class='news-content'>" + news.description + "</div>";
                    }
                    newsHtml += "</div>";
                    newsHtml += "</a>";
                });
                $("#news-container").html(newsHtml);
            },
			error: function(xhr, status, error) {
				console.log("FAILED TO CALL NEWS API: " + error);
			}
		});
	}

	// GET VIDEOS
	function getPopularVideos(regionCode) {
		$.ajax({
			url: "/api/popular-videos",
			type: "GET",
			data: { regionCode: regionCode },
			dataType: "json",
			success: function(response) {
				$("#video-container").empty();
				response.forEach((video) => {
					var iframeTag = $("<iframe>", {
						id: "ytplayer",
						type: "text/html",
						src: 'https://www.youtube.com/embed/' + video.id,
						frameborder: "0",
						allowfullscreen: true
					});
					$("#video-container").append(iframeTag);
				});
			},
			error: function(xhr, status, error) {
				console.error("FAILED TO CALL YOUTUBE API: ", error);
			}
		});
	}

    $('#nav-signin-btn').on('click', openLoginModal);
    function openLoginModal() {
        $('#login-modal-overlay').css('display', 'flex').hide().fadeIn();
        $('#login-modal input').val("");
        $('#login-modal').css('display', 'none').fadeIn();
    }

        $('#login-modal-overlay').on('click', function(e) {
            if (e.target === this) {
                $('#login-modal-overlay').fadeOut(function() {
                    $(this).css('display', 'none');
                });
                $('#login-modal').fadeOut();
            }
        });

    $('#nav-signup-btn').on('click', function() {
        $(".signup-form input").val("");
        $('#signup-modal-overlay').css('display', 'flex').hide().fadeIn();
        $('#signup-modal').css('display', 'none').fadeIn();
    });

    $('#signup-modal-overlay').on('click', function(e) {
        if (e.target === this) {
            $('#signup-modal-overlay').fadeOut(function() {
                $(this).css('display', 'none');
            });
            $('#signup-modal').fadeOut();
        }
    });

    $("#login-submit-btn").click(function () {
        $.ajax({
            type: "POST",
            url: "/auth/login",
            contentType: "application/json",
            data: JSON.stringify({
                email: $("#login-email").val(),
                password: $("#login-password").val()
            }),
            success: function (tokenPair) {
                sessionStorage.setItem("accessToken", tokenPair.accessToken);
                sessionStorage.setItem("refreshToken", tokenPair.refreshToken);
                window.parent.location.reload();
            },
            error: function (xhr, status, error) {
                console.error("LOGIN FAIL" + error);
            }
        });
    });

    $("#logout-btn").click(function () {
        let data = {
            accessToken: sessionStorage.getItem("accessToken"),
            refreshToken: sessionStorage.getItem("refreshToken")
        };
        sendRequest("/auth/logout", "POST", data,
        function() {
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");
            accessToken = null;
            window.parent.location.reload();
        },
        function() {
            console.error("로그인한 유저가 아닙니다.");
        });
    });

    function getPosts(regionCode, page) {
        console.log("GET POST ID:" + currentMemberId);
        $.ajax({
            url: "/posts",
            type: "GET",
            data: {
                country: regionCode,
                page: page,
            },
            dataType: "json",
            success: function (data) {
                $("#post-content-container").empty();
                if (data.content.length === 0) {
                    $("#post-container").text("게시글이 존재하지 않습니다.");
                    return;
                }

        $("#post-container").show();
        data.content.forEach(function (post) {
            let postHtml = "<div class='post' data-post-id='" + post.id + "'>";
            postHtml += "<div class='post-profile-img-container'>";
            postHtml += "<div class='post-profile-img'><i class='fa-solid fa-circle-user'></i></div></div>";
            postHtml += "<div class='post-contents-container'>";
            postHtml += "<div class='post-title-wrap'>";
            postHtml += "<div class='post-author'>" + post.authorNickname + "</div>";
            postHtml += "<div class='post-date'>" + formatDate(post.createdAt) + "</div>";

            if(post.authorId === currentMemberId) {
                postHtml += "<div class='post-modify-btn'>수정</div>";
                postHtml += "<div class='post-delete-btn'>삭제</div>";
            }

            postHtml += "</div><div class='post-title'>" + post.content + "</div>";
            postHtml += "<div class='post-footer-container'>"
            postHtml += "<div class='post-footer-likes'><i class='fa-solid fa-heart'></i>0</div>";
            postHtml += "<div class='post-footer-comments'><i class='fa-regular fa-comment-dots'></i>" + post.commentCount + "</div>";
            postHtml += "<div class='comment-form-btn'>댓글</div></div>";
            postHtml += "<div class='comment-form-container'>";
            postHtml += "<div class='comment-input' contenteditable='true' data-parent-post-id='" + post.id + "'></div>";
            postHtml += "<div class='comment-btn-group'>"
            postHtml += "<div class='comment-submit-btn'>작성</div>";
            postHtml += "<div class='comment-cancel-btn'>취소</div>";
            postHtml += "</div></div></div></div>";
            $("#post-content-container").append(postHtml);

            if (post.comments && post.comments.length > 0) {
                let commentHtml = "<div class='post-comments-container'>";
                commentHtml += "<div class='comment-dropdown'><div class='comment-toggle'>댓글 " + post.commentCount + "개 보기</div></div>";
                commentHtml += "<div class='post-comments'>";
                post.comments.forEach(function(comment) {
                    commentHtml += "<div class='comment data-comment-id='" + comment.id + "'>";
                    commentHtml += "<div class='comment-profile-img'><i class='fa-solid fa-circle-user'></i></div>";
                    commentHtml += "<div class='comment-contents-container'>";
                    commentHtml += "<div class='comment-title-wrap'><div class='comment-author'>" + comment.authorNickname + "</div>";
                    commentHtml += "<div class='comment-date'>" + formatDate(comment.createdAt) + "</div></div>";
                    commentHtml += "<div class='comment-content'>" + comment.content + "</div>";
                    commentHtml += "</div></div>";
                });
                commentHtml += "</div>";
                $("#post-content-container").append(commentHtml);
            }
        });
        updatePagination(data);
            },
            error: function (xhr, status, error) {
                console.error("FAILED TO CALL POSTS API: " + error);
            }
        });
    }

    function refreshPage() {
        getPosts(currentRegionCode, currentPage);
    }

    function loadPage(currentRegionCode, page) {
        currentPage = page;
        getPosts(currentRegionCode, page);
    }

    function updatePagination(data) {
        const pagesPerSheet = 5;
        const startPage = Math.max(0, currentPage - Math.floor(pagesPerSheet / 2));
        const endPage = Math.min(data.totalPages - 1, startPage + pagesPerSheet - 1);

        let paginationHtml = "<div class='pagination'>";

        const isFirstPage = currentPage === 0;
        if (isFirstPage) {
            paginationHtml += "<div id='goBackTenPages' style='color:grey'><i class='fa-solid fa-backward'></i></div>";
            paginationHtml += "<div style='color:grey'><i class='fa-solid fa-caret-left'></i></div>";
        } else {
            paginationHtml += "<div id='goBackTenPages'><i class='fa-solid fa-backward'></i></div>";
            paginationHtml += "<div data-page=" + (currentPage - 1) + "><i class='fa-solid fa-caret-left'></i></div>";
        }

        for (let i = startPage; i <= endPage; i++) {
            if (i === currentPage) {
                paginationHtml += "<div style='color: red; font-weight:bold;'>" + (i + 1) + "</div>";
            } else {
                paginationHtml += "<div data-page='" + i + "'>" + (i + 1) + "</div>";
            }
        }

        const isLastPage = currentPage === data.totalPages - 1;
        if (isLastPage) {
            paginationHtml += "<div style='color:grey'><i class='fa-solid fa-caret-right'></i></div>";
            paginationHtml += "<div id='goForwardTenPages' style='color:grey'><i class='fa-solid fa-forward'></i></div>";
        } else {
            paginationHtml += "<div data-page=" + (currentPage + 1) + "><i class='fa-solid fa-caret-right'></i></div>";
            paginationHtml += "<div id='goForwardTenPages'><i class='fa-solid fa-forward'></i></div>";
        }

        paginationHtml += "</div>";
        $("#pagination").html(paginationHtml);

        // 10장 앞으로
        $("#goBackTenPages").click(() => {
            if (currentPage - 10 >= 0) {
                currentPage -= 10;
            } else {
                currentPage = 0;
            }
            loadPage(currentRegionCode, currentPage);
        });

        // 10장 뒤로
        $("#goForwardTenPages").click(() => {
            if (currentPage + 10 <= data.totalPages - 1) {
                currentPage += 10;
            } else {
                currentPage = data.totalPages - 1;
            }
            loadPage(currentRegionCode, currentPage);
        });
    }

    $("#pagination").on("click", "div[data-page]", function () {
        let page = parseInt($(this).attr("data-page"));
        loadPage(currentRegionCode, page);
    });

    function formatDate(localDateTime) {
        const currentDate = new Date();
        const targetDate = new Date(localDateTime);

        const timeDifference = currentDate - targetDate;
        const minutes = timeDifference / 60000;
        const hours = minutes / 60;
        const days = hours / 24;
        const months = days / 30;
        const years = days / 365;

        if (minutes < 5) {
            return "방금 전";
        } else if (minutes < 60) {
            return Math.floor(minutes) + "분 전";
        } else if (hours < 24) {
            return Math.floor(hours) + "시간 전";
        } else if (days < 30.44) {
            return Math.floor(days) + "일 전";
        } else if (months < 12) {
            return Math.floor(months) + "달 전";
        } else {
            return Math.floor(years) + "년 전";
        }
    }

    $("#post-write-btn").on("click", function() {
        if (!sessionStorage.getItem("accessToken")) {
            openLoginModal();
            return;
        }

        sendRequest("/check-auth", "POST", "",
        function(response) {
            $("#post-write-nickname").text(response.nickname);
        },
        function(error) {
            console.log(error);
        });

        $("#post-write-textarea").text("");
        $("#post-write-container").show();
    });

    $("#post-write-cancel-btn").on("click", function() {
        $("#post-write-container").hide();
    });

    $("#post-write-submit-btn").click(function () {
        let postData = {
            country: currentRegionCode,
            content: $("#post-write-textarea").text()
        };

        sendRequest("/posts", "POST", postData,
            function (response) {
                loadPage(currentRegionCode, 0);
                $("#post-write-container").hide();
            },
            function (error) {
                alert("글 작성 실패");
            }
        );
    });

    // 게시글 수정 버튼 이벤트
    $("#post-content-container").on("click", ".post-modify-btn", function(event) {
        clearModifyMode();
        let content = $(this).parent().siblings(".post-title");
        content.data("previous-content", content.text());
        content.addClass("post-modify-mode");
        content.attr("contenteditable", "true");

        let modifyBtnContainer = $("<div class='post-modify-btn-container'></div>");
        modifyBtnContainer.append($("<div class='post-modify-mode-btn' id='post-modify-submit-btn'>수정</div>"));
        modifyBtnContainer.append($("<div class='post-modify-mode-btn' id='post-modify-cancel-btn'>취소</div>"));
        content.after(modifyBtnContainer);

        $(this).closest(".post").data("post-modify-mode-active", true);
        content.focus();
    });

    // 수정 취소 버튼 이벤트
    $("#post-content-container").on("click", "#post-modify-cancel-btn", function(event) {
        let content = $(this).closest(".post").find(".post-title.post-modify-mode");
        content.text(content.data("previous-content"));
        clearModifyMode();
        event.stopPropagation();
    });

    function clearModifyMode() {
        $(".post-title").removeClass("post-modify-mode");
        $(".post-title").attr("contenteditable", "false");
        $(".post").data("post-modify-mode-active", false);
        $(".post-modify-mode-btn").remove();
        $(".post-modify-btn-container").remove();
    }

    // 게시글 수정 전송
    $("#post-content-container").on("click", "#post-modify-submit-btn", function(event) {
        let content = $(this).closest(".post").find(".post-title.post-modify-mode");
        let postId = $(this).closest(".post").data("post-id");

        let modifyData = {
            content: content.text()
        };

        sendRequest("/posts/" + postId, "PUT", modifyData,
        function(response) {
            refreshPage();
        },
        function(error) {
            alert("게시글이 수정에 실패했습니다.");
        });
    });

    // 게시글 삭제
    $("#post-content-container").on("click", ".post-delete-btn",function(event) {
        let deleteConfirmation = confirm("정말로 삭제하시겠습니까?");

        if(deleteConfirmation) {
            let postId = $(this).closest(".post").data("post-id");
            sendRequest("/posts/" + postId, "DELETE", null,
            function(response) {
                refreshPage();
            },
            function(error){
                alert("게시글이 삭제에 실패했습니다.");
            });
        }

    });

    // 댓글 작성 폼
    $("#post-content-container").on("click", ".comment-form-btn", function () {
        let commentFormContainer = $(this).closest(".post").find(".comment-form-container");
        $(".comment-form-container").not(commentFormContainer).hide();
        commentFormContainer.show();
    });

    // 댓글 작성 폼 취소
    $("#post-content-container").on("click", ".comment-cancel-btn", function () {
        let commentFormContainer = $(this).closest(".comment-form-container");
        let commentInput = commentFormContainer.find(".comment-input");
        commentInput.html("");
        commentFormContainer.hide();
    });

    // 댓글 작성
    $("#post-content-container").on("click", ".comment-submit-btn", function () {
        console.log("RESULT:" + $(this).closest(".post").next(".post-comments-container").find(".comment-dropdown").attr("class"));
        let parentPostId = $(this).closest(".post").find(".comment-input").data("parent-post-id");
        let commentContent = $(this).closest(".post").find(".comment-input").text();
        let dropdown = $(this).closest(".post").next(".post-comments-container").find(".comment-dropdown").attr("class");
        let commentData = { content: commentContent }
        sendRequest("/posts/" + parentPostId + "/comments", "POST", commentData,
            function (response) {
                loadPage(currentRegionCode, currentPage);
                console.log($(this).attr("class"));
            },
            function (error) { console.log(error); });
    });

    $("#post-content-container").on("click", ".comment-dropdown", function () {
        showComments($(this));
    });

    function showComments(element) {
        let commentContainer = element.siblings(".post-comments");

        if (commentContainer.css("display") === "none") {
            commentContainer.css("display", "block");
            element.css("background-color", "grey");
            element.css("color", "white");
        } else {
            commentContainer.css("display", "none");
            element.css("background-color", "rgb(42, 169, 224)");
            element.css("color", "white");
        }

        let commentToggle = element.find(".comment-toggle");
        let isVisible = commentContainer.css("display") !== "none";
        commentToggle.text(isVisible ? "댓글 숨기기" : "댓글 " + commentContainer.find(".comment").length + "개 보기");
    }

});