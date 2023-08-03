$(function() {
	let isDetailVisible = false;

	function toggleDetail() {
		if (isDetailVisible) {
			$("#detail-container").fadeOut();
		} else {
			$("#detail-container").fadeIn();
		}
		isDetailVisible = !isDetailVisible;
	}

	$(document).on("click", function() {
		if (isDetailVisible) {
			toggleDetail();
		}
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

	$(".allPaths").on("click", function() {
		$("#video-container").empty();
		$("#news-container").empty();
		$("#post-container").empty();

		const countryName = this.id;
		$(".country-name").text(countryName);

		const regionCode = $(this).data("regioncode");
		getPopularVideos(regionCode);
		getNews(regionCode);
		getPosts(regionCode);
		event.stopPropagation();
		toggleDetail();
	});

	$("#detail-container").on("click", function(event) {
		event.stopPropagation();
	});

	$("#close-sidebar-btn").on("click", function() {
		toggleDetail();
	});

	// GET NEWS
	function getNews(regionCode) {
		$.ajax({
			url: "/api/news",
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

    // GET POSTS
    function getPosts(regionCode) {
        $.ajax({
            url: "/posts",
            type: "GET",
            data: { country: regionCode,
            },
            dataType: "json",
            success: function(data) {
                data.content.forEach(function(post) {
                    let postHtml = "<div class='post'>";
                    postHtml += "<div class='post-title'>" + post.title + "</div>";
                    postHtml += "<div class='post-author'>" + post.author + "</div>";
                    postHtml += "<div class='post-date'>" + post.createdAt + "</div>";
                    postHtml += "</div>";
                    $("#post-container").append(postHtml);
                });
            },
            error: function(xhr, status, error) {
                console.error("FAILED TO CALL POSTS API: " + error);
            }
        });
    }

});