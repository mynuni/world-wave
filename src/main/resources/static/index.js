    // GET NEWS
    function getNews() {
        // DUMMY
        let country = "us";
        let pageSize = 5;

        $.ajax({
            url: "/api/news",
            type: "GET",
            data: {
                country: country,
                pageSize: pageSize
            },
            success: function (data) {
                let newsHtml = "";
                data.forEach(function (news) {
                    newsHtml += "<div>" + news.title + "</div>";
                    newsHtml += "<p>" + news.description + "</p>";
                    newsHtml += "<img src='" + news.urlToImage + "' alt='News Image'>";
                    newsHtml += "<a href='" + news.url + "' target='_blank'>more..</a>";
                });
                $("#news").html(newsHtml);
            },
            error: function (xhr, status, error) {
                console.log("FAILED TO CALL NEWS API: " + error);
            }
        });
    }

    // GET VIDEOS
	function getPopularVideos() {
        // DUMMY
        let regionCode = "KR";

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
