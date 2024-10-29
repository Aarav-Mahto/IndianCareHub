$(document).ready(async function () {

    //====================Append DMCA Badge=============
    try {

        $('<a/>', {
            href: '//www.dmca.com/Protection/Status.aspx?ID=2fa696c2-3093-4f4e-a7e7-e3907994ab34',
            title: 'DMCA.com Protection Status',
            class: 'dmca-badge',
            html: '<img src="https://images.dmca.com/Badges/dmca-badge-w150-5x1-02.png?ID=2fa696c2-3093-4f4e-a7e7-e3907994ab34" alt="DMCA.com Protection Status" />'
        }).appendTo('#dmca-container');

        $('<script/>', {
            src: 'https://images.dmca.com/Badges/DMCABadgeHelper.min.js',
            async: true
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
//-------------- Cookies------------------------------
    // try {
    //     $('#cookies-box').fadeIn();
    //     if (getCookie('cookiesAccepted') === 'true' || getCookie('cookiesAccepted') === 'false') {
    //         $('#cookies-box').hide();
    //     }
    //     $('#accept-cookies').click(function () {
    //         $('#cookies-box').fadeOut();
    //         setCookie('cookiesAccepted', 'true', 7);
    //     })
    //     $('#reject-cookies').click(function () {
    //         $('#cookies-box').fadeOut();
    //         setCookie('cookiesAccepted', 'false', 7);
    //     })
    // } catch (error) {
    //     console.log("Cookies Related Issues: ", error);
    // }
    // function setCookie(name, value, days) {
    //     let expires = "";
    //     if (days) {
    //         let date = new Date();
    //         date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    //         expires = "; expires=" + date.toUTCString();
    //     }
    //     document.cookie = name + "=" + (value || "") + expires + "; path=/";
    // }
    // function getCookie(name) {
    //     let nameEQ = name + "=";
    //     let ca = document.cookie.split(';');
    //     for (let i = 0; i < ca.length; i++) {
    //         let c = ca[i].trim();
    //         if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    //     }
    //     return null;
    // }









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
    function isArrayNullOrEmpty(arr) {
        return !Array.isArray(arr) || arr.length === 0; // Returns true if arr is not an array or if it is empty
    }
    var type = $('#type-jsonLD').text().trim().toLowerCase();
    var type_secondry = (type === 'person' || type === 'stand_alone') ? "Person" : "Organization";

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
            "contactPoint": (!isArrayNullOrEmpty(allNumbers) || !isArrayNullOrEmpty(emailMessages)) ? 
            allNumbers.concat(emailMessages).filter(Boolean) : undefined,
            "additionalType": "https://schema.org/ContactInformation",
            "areaServed": countryName
        },
        "mainEntityOfPage": {
            "@type": "FAQPage", // FAQ section
            "mainEntity": !isArrayNullOrEmpty(qnAnsArray) ? qnAnsArray : undefined
        }
    };
    for (const key in jsonLD) {
        if (jsonLD[key] === undefined) {
            delete jsonLD[key]; // Remove properties with undefined values
        }
    }
    $('head').append(`<script type="application/ld+json">${JSON.stringify(jsonLD)}</script>`);
}









