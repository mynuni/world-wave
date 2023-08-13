    $(function () {
        $(".login-form").on("submit", function (event) {
            event.preventDefault();
            let email = $("#email").val();
            let password = $("#password").val();

            $.ajax({
                type: "POST",
                url: "/auth/login",
                contentType: "application/json",
                data: JSON.stringify({
                    email: email,
                    password: password
                }),
                success: function (response) {
                    window.parent.location.reload();
                },
                error: function (xhr, status, error) {
                    console.error("LOGIN FAIL" + error);
                }
            });
        });
    });