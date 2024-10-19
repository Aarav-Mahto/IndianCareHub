let csrfToken = '';
let csrfHeader = '';
var searched_history = '';

$(document).ready(async function () {
    try {
        // Append font preconnect and stylesheet links
        $('<link/>', {
            rel: 'preconnect',
            href: 'https://fonts.googleapis.com'
        }).appendTo('head');

        $('<link/>', {
            rel: 'preconnect',
            href: 'https://fonts.gstatic.com',
            crossorigin: 'anonymous'
        }).appendTo('head');

        $('<link/>', {
            rel: 'stylesheet',
            href: 'https://fonts.googleapis.com/css2?family=Ubuntu:wght@300;400&display=swap'
        }).appendTo('head');

    } catch (error) {
        console.log("Append Font Error: ", error);
    }


    try {
        $('<link/>', {
            rel: 'stylesheet',
            type: 'text/css',
            href: 'https://cdnjs.cloudflare.com/ajax/libs/remixicon/4.2.0/remixicon.css',
            referrerpolicy: 'no-referrer'
        }).appendTo('head');
        $('<link/>', {
            rel: 'stylesheet',
            type: 'text/css',
            href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css'
        }).appendTo('head');

    } catch (error) {
        console.log("Error While Adding Links: ", error);
    }
    //============= Google Tag Manager ========================
    // try {
    //     if (location.hostname === 'localhost') {
    //         console.log("Mocking GTM load on localhost");
    //     } else {
    //         $('<script/>', {
    //             text: "(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
    //                 "new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
    //                 "j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
    //                 "'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
    //                 "})(window,document,'script','dataLayer','GTM-PVMW46WQ');"
    //         }).prependTo('head');
    //     }
    // } catch (error) {
    //     console.log("Error while prepending the google tag manager tag into head: ", error);
    // }
    //===============================================
    //============= Adsense Tag ========================
    try {
        $('<meta/>', {
            name: 'google-adsense-account',
            content: 'ca-pub-2630992286399451'
        }).prependTo('head');

        if (location.hostname === 'localhost') {
            console.log("Mocking Adsense load on localhost");
        } else {
            $('<script/>', {
                async: true,
                src: 'https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-2630992286399451',
                crossorigin: 'anonymous'
            }).prependTo('head');
        }
    } catch (error) {
        console.log("Error while prepending the adsense tag into head: ", error);
    }
    //-------------Load Adsense On Scroll --------
    try {
        var la = false;
        await $(window).on("scroll", function () {
            if ((!$(document).scrollTop() && la === false) || (!$(window).scrollTop() && la === false)) {
                (function () {
                    var e = document.createElement("script");
                    e.type = "text/javascript";
                    e.async = true;
                    e.src = "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-2630992286399451";
                    var a = $("script")[0];
                    a.parentNode.insertBefore(e, a);
                })();
                la = true;
            }
        });
    } catch (error) {
        console.error("Error Occured while loading the 'Show Ads On Scroll': ", error);
    }








    //========================== Cookies===============================
    try {
        // Existing GTM implementation
        if (location.hostname === 'localhost') {
            console.log("Mocking GTM load on localhost");
        } else {
            await new Promise((resolve, reject) => {
                try {
                    $('<script/>', {
                        text: "(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
                            "new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
                            "j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
                            "'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
                            "})(window,document,'script','dataLayer','GTM-PVMW46WQ');"
                    }).prependTo('head');
                    resolve();
                } catch (error) {
                    console.error("Error loading GTM script: ", error);
                    reject(error);
                }
            });
        }

        // Add Google Analytics Measurement ID
        try {
            $('<script async src="https://www.googletagmanager.com/gtag/js?id=G-1GFV57QT55"></script>').prependTo('head');
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());
            gtag('config', 'G-1GFV57QT55', {
                'anonymize_ip': true,
                'ad_storage': 'denied', // Default to denied
                'analytics_storage': 'denied' // Default to denied
            });
        } catch (error) {
            console.error("Error initializing Google Analytics: ", error);
        }

        // Function to handle cookies consent
        $('#cookies-box').fadeIn();
        if (getCookie('cookiesAccepted') === 'true' || getCookie('cookiesAccepted') === 'false') {
            $('#cookies-box').hide();
        }

        $('#accept-cookies').click(async function () {
            try {
                $('#cookies-box').fadeOut();
                setCookie('cookiesAccepted', 'true', 7);
                // Push consent initialization event
                dataLayer.push({
                    event: "gtm.init_consent",
                    'gtm.uniqueEventId': -1 // Unique identifier for consent initialization
                });
            } catch (error) {
                console.error("Error handling cookie acceptance: ", error);
            }
        });

        $('#reject-cookies').click(async function () {
            try {
                $('#cookies-box').fadeOut();
                setCookie('cookiesAccepted', 'false', 7);
                // Optionally push consent denial event if needed
                dataLayer.push({
                    event: "gtm.consent_denied",
                    'gtm.uniqueEventId': -1 // Unique identifier for consent denial
                });
            } catch (error) {
                console.error("Error handling cookie rejection: ", error);
            }
        });
    } catch (error) {
        console.error("Error in document ready function: ", error);
    }

    function setCookie(name, value, days) {
        try {
            let expires = "";
            if (days) {
                let date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toUTCString();
            }
            document.cookie = name + "=" + (value || "") + expires + "; path=/";
        } catch (error) {
            console.error("Error setting cookie: ", error);
        }
    }

    function getCookie(name) {
        try {
            let nameEQ = name + "=";
            let ca = document.cookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i].trim();
                if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
            }
            return null;
        } catch (error) {
            console.error("Error getting cookie: ", error);
        }
    }




    //====================Append DMCA Badge=============
    try {

        $('<a/>', {
            href: '//www.dmca.com/Protection/Status.aspx?ID=2fa696c2-3093-4f4e-a7e7-e3907994ab34',
            title: 'DMCA.com Protection Status',
            class: 'dmca-badge',
            html: '<img src="https://images.dmca.com/Badges/dmca-badge-w150-5x1-02.png?ID=2fa696c2-3093-4f4e-a7e7-e3907994ab34" alt="DMCA.com Protection Status" />'
        }).appendTo('#dmca-container');

        $('<script/>', {
            src: 'https://images.dmca.com/Badges/DMCABadgeHelper.min.js'
        }).appendTo('#dmca-container');
    } catch (error) {
        console.log("error While Appending DMCA Baadge: ", error);
    }
    //--------------------------------------------------------------------------

    try {
        await $.getScript("https://cdn.jsdelivr.net/npm/@emailjs/browser@3/dist/email.min.js");
        emailjs.init("xLkhpASd62DGUbmZ_");
        await $("#sendFeedback").on("click", sentMail);
    } catch (error) {
        console.log("Error while preparing for email.js: ", error);
    }

    try {
        if (window.location.hash === '#search-input') {
            await $.get('/csrf', function (data) {
                csrfToken = data.token;
                csrfHeader = data.headerName;
            });
            setTimeout(function () {
                $('#search-input').focus();
            }, 100);
        }
    } catch (error) {
        console.log("Error is about Focusing the Input Box: ", error);
    }


    try {
        $('#logo-box img').on('contextmenu', function (e) {
            e.preventDefault();
        });
    } catch (error) {
        console.log("Error on disabling right click on images: ", error);
    }

    //=============Scroll To Top====================
    try {
        await $(window).on('scroll', function () {
            var scrollToTopButton = $('#scroll-to-top');
            if ($(window).scrollTop() > 300) {
                scrollToTopButton.show();
            } else {
                scrollToTopButton.hide();
            }
        });

        $('#scroll-to-top').on('click', function () {
            $('html, body').animate({ scrollTop: 0 }, 'smooth');
        });
    } catch (error) {
        console.log("Error while scroll to top event: ", error);
    }


    try {
        $('#search-input, #search-icon').on('focus click', () => {
            $('#search-slogan').css('display', 'none');
            $('#search-box').css('position', 'fixed');
            $('#search-box').addClass('start-searching');
            $('#search-input').addClass('placeholder-gray')
            $('#search-icon').fadeOut();
            $('#close-icon1').fadeIn();
            $('#search-div, #search-suggession').css('width', '85%');
            $('#search-suggession').fadeIn();
            $('#search-input').val(searched_history);

        });
        $('#search-input, #search-icon').on('blur', () => {
            searched_history = $('#search-input').val();
        });
        $('#close-icon, #close-icon1').click(() => {
            $('#search-suggession').css('display', 'none');
            $('#search-box').removeClass('start-searching');
            $('#search-input').removeClass('placeholder-gray')
            $('#close-icon1').fadeOut();
            $('#search-icon').fadeIn();
            $('#search-div, #search-suggession').css('width', '');
            $('#search-slogan').fadeIn();
            $('#search-box').css('position', 'absolute');
            searched_history = $('#search-input').val();
            $('#search-input').val("");
        });
    } catch (error) {
        console.log("Error while implementing search suggestion focus event: ", error);
    }

    try {
        await $.get('/csrf', function (data) {
            csrfToken = data.token;
            csrfHeader = data.headerName;
        });

        await $('#search-input').keyup(() => {
            search_Now();
        });
    } catch (error) {
        console.log("Error while implementing search suggestion keyup event: ", error);
    }

    try {
        const hideKeyboard = () => {
            $('#search-input').blur();
        };
        await $('#search-suggession').on('scroll', hideKeyboard);
    } catch (error) {
        console.log("Error while hidding keyboard: ", error);
    }

});

/* ============================= Search Suggessions ================ */

async function search_Now() {
    let search_text = $("#search-input").val().trim();
    var flag = true;
    if (search_text === '') {
        $('#addSuggession').empty();
        $('#search-msg').text("Empty!");
        $('#search-msg').fadeIn();
        flag = false;
    }
    else {
        $.ajax({
            url: '/searchSuggession',
            type: 'POST',
            data: { character: search_text },
            dataType: 'json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                $('#addSuggession').empty();
                try {
                    if (Object.keys(response).length === 0) {
                        if ($('#addSuggession').children().length === 0) {
                            $('#search-msg').html("Not Found!");
                            $('#search-msg').fadeIn();
                        }
                    } else {
                        $('#addSuggession').empty();
                        $('#addSuggession').html("");
                        $('#addSuggession').text("");
                        $('#search-msg').fadeOut();
                        for (let key in response) {
                            if (response.hasOwnProperty(key)) {
                                $('#search-msg').html("");
                                var pageLink = response[key].pageLink;
                                var pageTitle = response[key].PageTitle;
                                var isUrlAvailable = response[key].isUrlAvailable;
                                var tabs = "<li><a href='" + pageLink + "'" + isUrlAvailable + ">" + pageTitle + "</a></li>";
                                $('#addSuggession').append(tabs);
                            }
                        }
                    }
                } catch (error) {
                    console.error("Error processing JSON:", error);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error: ", error);
            }
        })
    }
}
$(document).on('click', '.disable-url', function (event) {
    event.preventDefault();
    alert("This page is temporarily unavailable. Please check back later.");
});
//================================ Send Email ===================================
async function sentMail() {
    let emailId = $('#mail_id').val().trim();
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailId !== '' && !emailPattern.test(emailId)) {
        $('#mail_id').css('border', '1px solid red');
        $("#sending").text("Invalid Email Id!").css('color', 'red');
        return;
    }
    let message = $('#message').val().trim();
    if (message === '') {
        $("#sending").text("Write Feedback!").css('color', 'red');
        $('#message').css('border', '1px solid red');
        return;
    }
    if (emailId !== '' && emailPattern.test(emailId) && message != '') {
        $("#sending").text("Sending...").css('color', 'green');
        let params = {
            pageIdOnly: "enquirylist.com",
            email_id: emailId,
            message: message
        };
        emailjs.send("service_2jnmm6w", "template_ou9hsny", params)
            .then(function (response) {
                $("#sending").text("Sent Successfully! Thanks We will get back to you soon.").css('color', 'darkgreen');
                $("#mail_id").val("");
                $("#message").val("");
            })
            .catch(function (error) {
                $("#sending").text("Failled!").css('color', 'darkred');
            });
    }
}




