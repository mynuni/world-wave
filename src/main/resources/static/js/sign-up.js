    $(function() {
        let isValidEmail = false;
        let isValidNickname = false;

        const countryMap = {
            "Australia": "AU",
            "Brazil(Brasil)": "BR",
            "Canada": "CA",
            "France": "FR",
            "Germany(Deutschland)": "DE",
            "India(भारत)": "IN",
            "Italy": "IT",
            "Japan(日本)": "JP",
            "Mexico": "MX",
            "Republic of Korea(대한민국, 한국)": "KR",
            "Russia(Россия)": "RU",
            "Saudi Arabia(المملكة العربية السعودية)": "SA",
            "Spain(España)": "ES",
            "United States of America(USA)": "US"
        };

        const countryNames = Object.keys(countryMap);
        $("#signup-country").autocomplete({
            source: function(request, response) {
                const term = request.term.toLowerCase();
                const matchedCountries = countryNames.filter(function(country) {
                    return country.toLowerCase().indexOf(term) !== -1;
                });
                response(matchedCountries);
            },
            select: function(event, ui) {
                const selectedCountry = ui.item.value;
                const countryCode = countryMap[selectedCountry];
                $('#signup-country').data("country-code", countryCode);
            }
        });

        $("#signup-email").on("keyup", function() {
            const email = $(this).val();
            if(!isEmailFormat(email)) {
                $('#email-valid-check').text("이메일 형식이 올바르지 않습니다.").css("color", "red");
                return;
            }
            $('#email-valid-check').text("");
            checkEmailDuplicate(email);
        });

        function isEmailFormat(email) {
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
            return emailPattern.test(email);
        }

        function checkEmailDuplicate(email) {
            console.log("이메일 중복 검사");
            $.ajax({
                type: "GET",
                url: "/auth/check-email",
                data: { email: email },
                success: function(exist) {
                    if (exist) {
                        $('#email-valid-check').text("사용 불가능한 이메일입니다.").css("color", "red");
                        isValidEmail = false;
                    } else {
                        $('#email-valid-check').text("");
                        isValidEmail = true;
                    }
                },
                error: function() {
                    console.error("DUPLICATE CHECK ERROR");
                }
            });
        }

        $("#signup-nickname").on("keyup", function() {
            const nickname = $(this).val();
            checkNicknameDuplicate(nickname);
        });

        function checkNicknameDuplicate(nickname) {
            $.ajax({
                type: "GET",
                url: "/auth/check-nickname",
                data: { nickname: nickname },
                success: function(exist) {
                    if (exist) {
                        $('#nickname-valid-check').text("사용 불가능한 닉네임입니다.").css("color", "red");
                        isValidNickname = false;
                    } else {
                        $('#nickname-valid-check').text("");
                        isValidNickname = true;
                    }
                },
                error: function() {
                    console.error("DUPLICATE CHECK ERROR");
                }
            });
        }

        $('#signup-password, #signup-password-check').on('keyup', validatePassword);
        function validatePassword() {
            const password = $('#signup-password').val();
            const passwordCheck = $('#signup-password-check').val();
            const num = password.search(/[0-9]/g);
            const eng = password.search(/[a-z]/ig);

            if (password.length < 8 || password.length > 20 || num < 0 || eng < 0){
                $('#password-regulation').text("비밀번호는 영문+숫자 조합 8자~20자입니다.").css("color", "red");
                return false;
            } else if (password.search(/\s/) != -1){
                $('#password-regulation').text("공백은 사용 불가능합니다.").css("color", "red");
                return false;
            } else if (password !== passwordCheck && passwordCheck !== "") {
                $('#password-match').text("비밀번호가 일치하지 않습니다.").css("color", "red");
                return false;
            } else {
                $('#password-regulation, #password-match').text("");
                return true;
            }
        }

        function validateForm() {
            if(!isValidEmail) {
                alert("사용할 수 없는 이메일입니다.");
                return false;
            }

            if (!validatePassword()) {
                alert("비밀번호를 확인해주세요.");
                return false;
            }

            if(!isValidNickname) {
                alert("사용할 수 없는 닉네임입니다.");
                return false;
            }
            return true;
        }

        $(".signup-form").submit(function(event) {
            event.preventDefault();

            if (!validateForm()) {
                return;
            }

            $.ajax({
                type: "POST",
                url: "/auth/sign-up",
                contentType: "application/json",
                data: JSON.stringify({
                    email: $('#signup-email').val(),
                    password: $('#signup-password').val(),
                    passwordCheck: $('#signup-password-check').val(),
                    nickname: $('#signup-nickname').val(),
                    country: $('#signup-country').data("country-code")
                }),
                success: function(response) {
                    alert("회원가입이 완료되었습니다.");
                    window.parent.location.reload();
                },
                error: function(jqXHR) {
                    // 내용에 따른 에러 상황 처리할 것
                    console.error("SIGN UP ERROR");
                }
            });
        });

    });