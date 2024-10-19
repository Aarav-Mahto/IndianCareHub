$(document).ready(async function () {

    //============= Google Tag Manager ========================
    try {
        if (location.hostname === 'localhost') {
            console.log("Mocking GTM load on localhost");
        } else {
            $('<script/>', {
                text: "(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':" +
                    "new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0]," +
                    "j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=" +
                    "'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);" +
                    "})(window,document,'script','dataLayer','GTM-PVMW46WQ');"
            }).prependTo('head');
        }
    } catch (error) {
        console.log("Error while prepending the google tag manager tag into head: ", error);
    }
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
    //-----------------------------------------------------------------------------
    //============================ Load Adsense On Scroll =========================
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
        console.error("An error occurred while loading the script:", error);
    }
    //---------------------------------------------------------------------

    /*!!!!!!!!!!!!!!!!!!!!!!!!!!!--- Adsense Container ----!!!!!!!!!!!!!!!!!!!!!!!!!!*/

    //=================Adsense Unit - 1 ===========================
    // try {
    //     var adsElement = $('<ins/>', {
    //         class: 'adsbygoogle',
    //         style: 'display:block; height: 200px !important; margin-bottom: 30px;',
    //         'data-ad-client': 'ca-pub-2630992286399451',
    //         'data-ad-slot': '2903953482',
    //         'data-ad-format': 'auto',
    //         'data-full-width-responsive': 'true'
    //     });
    //     var scriptElement = $('<script/>', {
    //         text: '(adsbygoogle = window.adsbygoogle || []).push({});'
    //     });
    //     $('#ad-container-1').append(adsElement).append(scriptElement);
    // } catch (error) {
    //     console.log("Error while appending the Adsense ad to #ad-container-1: ", error);
    // }



































    /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */










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
        console.log("Error While Adding Links");
    }


    try {
        await $.getScript("https://cdn.jsdelivr.net/npm/@emailjs/browser@3/dist/email.min.js");
        emailjs.init("xLkhpASd62DGUbmZ_");
        $("#sentMail").on("click", sentMail);
    } catch (error) {
        console.log("Error while preparing for email.js: ", error);
    }

    try {
        $('#cookies-box').fadeIn();
        if (getCookie('cookiesAccepted') === 'true' || getCookie('cookiesAccepted') === 'false') {
            $('#cookies-box').hide();
        }
        $('#accept-cookies').click(function () {
            $('#cookies-box').fadeOut();
            setCookie('cookiesAccepted', 'true', 7);
        })
        $('#reject-cookies').click(function () {
            $('#cookies-box').fadeOut();
            setCookie('cookiesAccepted', 'false', 7);
        })
    } catch (error) {
        console.log("Cookies Related Issues: ", error);
    }
    function setCookie(name, value, days) {
        let expires = "";
        if (days) {
            let date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    }
    function getCookie(name) {
        let nameEQ = name + "=";
        let ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i].trim();
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }









    try {
        await jsonLD();
    } catch (error) {
        console.error("Error generating JSON-LD:", error);
    }

    try {
        $('.company_logo div img').on('contextmenu', function (e) {
            e.preventDefault(); // Prevent the default right-click menu
        });
        $('.td-3num, .td-num, .td-email').click(function () {
            var textToCopy = $(this).find('span').text().trim();
            copyNow(textToCopy);
        })

        $('#ok-close').click(() => {
            $('#popup').fadeOut();
        })
    } catch (error) {
        console.log("Error while copying text: ", error)
    }



});
//================================ Comments & Feedback=======================================================

function sentMail() {
    $("#sending").text("Sending...").css('color', 'green');

    var currentUrl = window.location.href;
    var url = new URL(currentUrl);
    var pathParts = url.pathname.split('/');
    //var wabPageId  = "contact/"+pathParts[pathParts.length - 1];
    var wabPageIdOnly = pathParts[pathParts.length - 1];

    let emailId = document.getElementById("mail_id").value;
    let message = document.getElementById("message").value;

    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (wabPageIdOnly !== '' && emailPattern.test(emailId) && message !== '') {
        let params = {
            pageIdOnly: wabPageIdOnly,
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
    } else {
        $("#sending").text("All fields and a valid email address are mandatory!").css('color', '#f95c11');
    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Copy Number And Email~~~~~~~~~~~~~~~ */
function popup(txt) {
    $('#popup').find('.copy-msg').text(txt);
    $('#popup').css('display', 'block');
    setTimeout(function () {
        $('#popup').fadeOut();
    }, 5000);
}
function copyNow(copyIt) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(copyIt).then(function () {
            popup(copyIt);
        }).catch(function (err) {
            cmdLineCopy(copyIt);
        });
    } else {
        cmdLineCopy(copyIt);
    }
}

function cmdLineCopy(copyIt) {
    var $temp = $('<textarea>').val(copyIt).appendTo('body').select();
    var copied = document.execCommand('copy');
    if (copied) {
        popup(copyIt);
    } else {
        popup("Error!: Can't Copy");
    }
    $temp.remove();
}

/* ~~~~~~~~~~~~~~~~~~~~~~ JSON-LD Formater~~~~~~~~~~~~~~~~~ */
async function jsonLD() {
    var type = $('#type-jsonLD').text().trim().toLowerCase();
    var type_secondry = (type === 'person') ? "Person" : "Organization";

    const countryName = "India";
    const language = ['English', 'Hindi'];
    const url = $('#url-jsonLD').text().trim();
    const orgName = $('#org-name').text();
    const lastUpdated = $('#lastUpdatedDate-jsonLD').text();
    const orgLogo = window.location.origin + $('#logo-jsonLD img').attr('src');

    let allNumbers = [];
    $('.no-jsonLD').each(function () {
        allNumbers.push({
            "@type": "ContactPoint",
            "contactType": $(this).find('.numType_jsonLD').text().trim(),
            "telephone": $(this).find('.numbers_jsonLD').text().trim(),
            "areaServed": countryName,
            "availableLanguage": language
        });
    });
    let emailMessages = [];
    $('.email-jsonLD').each(function () {
        emailMessages.push({
            "@type": "ContactPoint",
            "contactType": $(this).find('.emailType_jsonLD').text().trim(),
            "email": $(this).find('.emailsId_jsonLD').text().trim(),
            "areaServed": countryName,
            "availableLanguage": language
        });
    });
    const qnAnsArray = [];
    $('.qn-ans-jsonld').each(function () {
        qnAnsArray.push({
            "@type": "Question",
            "name": $(this).find('.qn-jsonld').text().trim(),
            "acceptedAnswer": {
                "@type": "Answer",
                "text": $(this).find('.ans-jsonld').text().trim()
            }
        });
    });

    const orgAddress = $('.address-jsonLD').text();
    const orgOverview = $('.overview-jsonLD').text();



    const jsonLD = {
        "@context": "https://schema.org",
        "@type": "WebPage",
        "name": orgName,
        "url": url,
        "description": orgOverview,
        "mainEntity": {
            "@type": type_secondry, // Organization details
            "name": orgName,
            "url": url,
            "logo": orgLogo,
            "address": {
                "@type": "PostalAddress",
                "streetAddress": orgAddress
            },
            "contactPoint": allNumbers.concat(emailMessages),
            "additionalType": "https://schema.org/ContactInformation",
            "areaServed": countryName
        },
        "mainEntityOfPage": {
            "@type": "FAQPage", // FAQ section
            "mainEntity": qnAnsArray
        }
    };
    $('head').append(`<script type="application/ld+json">${JSON.stringify(jsonLD)}</script>`);
}









