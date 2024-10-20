//================Timeout=================
const sessionTimeout = 30 * 60;
const keepAliveInterval = 25 * 60; 
let timeRemaining = sessionTimeout;
let countdownInterval;

function updateCountdownDisplay() {
    let hours = Math.floor(timeRemaining / 3600);
    let minutes = Math.floor((timeRemaining % 3600) / 60);
    let seconds = timeRemaining % 60;
    $('#time').text(`${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`);
}

function startCountdown() {
    updateCountdownDisplay();
    countdownInterval = setInterval(() => {
        if (timeRemaining <= 0) {
            clearInterval(countdownInterval);
            sendKeepAliveRequest();
            $('#time').text("Session Expired");
        } else {
            timeRemaining--;
            updateCountdownDisplay();
        }
    }, 1000);
}

function sendKeepAliveRequest() {
    var token = $('meta[name="_csrf"]').attr('content');
    var header = $('meta[name="_csrf_header"]').attr('content');
    $.ajax({
        url: '/keep-alive',
        method: 'POST',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            console.log('Session kept alive');
            timeRemaining = sessionTimeout;
            clearInterval(countdownInterval);
            startCountdown();
        },
        error: function () {
            console.error('Failed to keep session alive');
        }
    });
}
startCountdown();




var dialogInstance;
function customAlert(dialogContent, title) {
	if (dialogInstance && dialogInstance.dialog("isOpen")) {
		dialogInstance.dialog("close");
	}

	dialogInstance = dialogContent.dialog({
		title: title, // Set the dialog title
		modal: true,           // Make the dialog modal
		resizable: false,      // Disable resizing
		width: 400,                 // Set the width of the dialog
		height: 400,
		position: {
			my: 'center top',      // Dialog's alignment point
			at: 'center top+50',   // Alignment point of the target (center-top position with 50px margin-top)
			of: window             // Element to position against (in this case, the window)
		},
		buttons: {
			"✖": function () {
				$(this).dialog("close"); // Close the dialog when OK button is clicked
			}
		},
		open: function (event, ui) {
			$(this).css('overflow-y', 'auto');  // Apply overflow-y: auto to the dialog content area
			$(this).css('border', '2px solid chocolate');
			$(this).css('borderTop', 'none');
			$(this).css('borderBottom', '5px solid chocolate');
		}
	});
}
function showAbout(postid) {

	var token = $('meta[name="_csrf"]').attr('content');
	var header = $('meta[name="_csrf_header"]').attr('content');

	var title = "About Company (of id " + postid + ")";
	var dialogContent = $("<div>Loading...</div>");
	$.ajax({
		url: '/showAbout',
		type: 'POST',
		data: { postId: postid },
		beforeSend: function (xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function (response) {
			dialogContent.html("<div>" + response + "</div>");
			customAlert(dialogContent, title)
		},
		error: function (xhr, status, error) {
			console.error("Error : ", error);
			dialogContent.html("<div>Error loading contact details.</div>");
			customAlert(dialogContent, title)
		}
	})
}

function showContacts(postid) {

	var token = $('meta[name="_csrf"]').attr('content');
	var header = $('meta[name="_csrf_header"]').attr('content');

	var title = "Contacts Are (of id " + postid + ")";
	var dialogContent = $("<div>Loading...</div>");
	$.ajax({
		url: '/showContacts',
		type: 'POST',
		data: { postId: postid },
		beforeSend: function (xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function (response) {
			if (response != null) {
				var htmlContent = "<div>";
				htmlContent += "<ul>";
				for (var key in response) {
					if (response.hasOwnProperty(key)) {
						var list = response[key];
						htmlContent += "<p><span class='inline-item'>" + list[2] + " : </span><span class='inline-item'>" + list[3] + "</span></p>";
					}
				}
				htmlContent += "</ul>";
				htmlContent += "</div>";
				dialogContent.html(htmlContent);
			} else {
				dialogContent.html("<div>No data available</div>");
			}
			customAlert(dialogContent, title);
		},
		error: function (xhr, status, error) {
			console.error("Error : ", error);
			dialogContent.html("<div>Error loading contact details.</div>");
			customAlert(dialogContent, title)
		}
	})
}



function btnNavigate(view) {
	window.location.href = "/btnNavigate/" + view;
}

$('#socialBtn-add').click(function (event) {

})
$('#socialBtn-remove').click(function (event) {

})
function add_more(event) {
	event.preventDefault();
	var contactNoDiv = $('#contact-no');
	var newContactNumberField = `
		<div class="name_number">
			<div><input type="text" name="Department_Name" placeholder="Department Name" /></div>
			<div><input type="text" name="PhoneEmail" placeholder="0123456789" /></div>
		</div>
	`;
	contactNoDiv.find('.name_number:last').after(newContactNumberField);

}
function remove_more(event) {
	event.preventDefault();
	var contactNoDiv = $('#contact-no');
	if (contactNoDiv.find('.name_number').length > 1) {
		contactNoDiv.find('.name_number:last').remove();
	}
}
//============Add More Contact In Update ==================
$('#add-more-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.contact-no');
	var moreContactNumberField = `
		<div class="name_number">
			<div><input type="text" name="record_no" value="empty" hidden /></div>
			<div><input type="text" name="company_id" value="empty" hidden /></div>
			<div><input type="text" name="Department_Name" placeholder="General Enquiry" /></div>
			<div><input type="text" name="PhoneEmail" placeholder="0123456789" /></div>
		</div>
	`;
	moreContactDiv.append(moreContactNumberField);
});
$('#remove-more-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.contact-no');
	if (moreContactDiv.find('.name_number').length > 0) {
		moreContactDiv.find('.name_number:last').remove();
	}
})

//============Add Question And Answer ==================
$('#add-qa, #add-qa-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.QA');
	//var moreContactNumberField = $("<div class='QA-box'><div><input type='text' name='question' placeholder='Question? (<h3>...</h3>)' required></div><div><textarea name='answer' placeholder='Answer? (<p>...</p>)' cols='15' row='2' required></textarea></div></div>");
	var moreContactNumberField = `
		<div class="QA-box">
			<div><input type="text" name="qaSno" value="empty" hidden /></div>
			<div><input type="text" Name="qaCompanyId" value="empty" hidden /></div>
			<div><input type="text" name="question" class="question" placeholder="Question?" /></div>
			<div><textarea name="answer" class="answer" placeholder="Answer?" cols="15" rows="2"></textarea></div>
		</div>
	`;
	if (moreContactDiv.find('.QA-box').length === 0) {
		moreContactDiv.prepend(moreContactNumberField);
	} else {
		moreContactDiv.find('.QA-box:last').after(moreContactNumberField);
	}
})
$('#remove-qa, #remove-qa-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.QA');
	if (moreContactDiv.find('.QA-box').length > 0) {
		moreContactDiv.find('.QA-box:last').remove();
	}
})
//============Add Anchor Links ==================
$('#add-a').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.Add-AnchorLinks');
	//var moreContactNumberField = $("<div class='AnchorLinks-box'><div><input type='text' name='anchorName' placeholder='Anchor Name' required></div><div><input type='text' name='anchorLink' placeholder='Anchor Link (<a href=...></a>)' required></div></div>");
	var moreContactNumberField = `
		<div class="AnchorLinks-box">
			<div><input type="text" name="anchorSno" value="empty" hidden /></div>
			<div><input type="text" name="anchorCompanyId" value="empty" hidden /></div>
			<div><input type="text" name="anchorName" placeholder="Link Name" /></div>
			<div><input type="text" name="anchorLink" placeholder="Link" /></div>
		</div>
	`;
	if (moreContactDiv.find('.AnchorLinks-box').length === 0) {
		moreContactDiv.prepend(moreContactNumberField);
	} else {
		moreContactDiv.find('.AnchorLinks-box:last').after(moreContactNumberField);
	}
})
$('#remove-a').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.Add-AnchorLinks');
	if (moreContactDiv.find('.AnchorLinks-box').length > 0) {
		moreContactDiv.find('.AnchorLinks-box:last').remove();
	}
})
//============Add Anchor Links ==================
$('#add-a-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.Add-AnchorLinks');
	//var moreContactNumberField = $("<div class='AnchorLinks-box'><div><input type='text' name='anchorName' placeholder='Anchor Name' required></div><div><input type='text' name='anchorLink' placeholder='Anchor Link (<a href=...></a>)' required></div></div>");
	const moreContactNumberField = `
		<div class="AnchorLinks-box">
			<div><input type="text" name="anchorSno" value="empty" hidden /></div>
			<div><input type="text" Name="anchorCompanyId" value="empty" hidden /></div>
			<div><input type="text" name="anchorName" placeholder="Link Name" /></div>
			<div><input type="text" name="anchorLink" placeholder="Link" /></div>
		</div>
	`;
	if (moreContactDiv.find('.AnchorLinks-box').length === 0) {
		moreContactDiv.prepend(moreContactNumberField);
	} else {
		moreContactDiv.find('.AnchorLinks-box:last').after(moreContactNumberField);
	}
})
$('#remove-a-update').click(function (event) {
	event.preventDefault();
	var moreContactDiv = $('.Add-AnchorLinks');
	if (moreContactDiv.find('.AnchorLinks-box').length > 0) {
		moreContactDiv.find('.AnchorLinks-box:last').remove();
	}
})
//================== Adding Social Links=================

// Array to keep track of selected options
var selectedOptions = [];
function add_link(event) {
	event.preventDefault();
	var contactNoDiv = $('#social-media');
	if (contactNoDiv.find('.social-btn').length < 10) {
		updateSelectedOptions();
		var selectOptions = [
			{ value: 'faq', text: 'FAQ' },
			{ value: 'whatsapp', text: 'Whatsapp' },
			{ value: 'facebook', text: 'Facebook' },
			{ value: 'instagram', text: 'Instagram' },
			{ value: 'twitter', text: 'Twitter' },
			{ value: 'youtube', text: 'Youtube' },
			{ value: 'linkedin', text: 'Linkedin' },
			{ value: 'threads', text: 'Threads' },
			{ value: 'telegram', text: 'Telegram' }
		];
		var $select = $('<select></select>', {
			name: 'socialMediaName'
		});
		$.each(selectOptions, function (index, option) {
			if (!selectedOptions.includes(option.text)) {
				$select.append($('<option></option>').val(option.value).text(option.text));
			}
		});
		var $inputs = `
			<input type="text" placeholder="Paste Link Here" name="socialMediaLinks"/>
			<input type="text" value="empty" name="socialSno" hidden/>
			<input type="text" value="empty" name="socialCompanyId" hidden/>
		`;
		var newContactNumberField = $('<div>', { class: 'social-btn' })
			.append($select)
			.append($inputs);
		contactNoDiv.append(newContactNumberField);

	}
	else {
		alert("No More Social Media Links Supported!")
	}
}

function updateSelectedOptions() {
	selectedOptions = [];
	$('#social-media select').each(function () {
		var selectedText = $(this).find('option:selected').text();
		if (selectedText) {
			selectedOptions.push(selectedText);
		}
	});
}

function remove_link(event) {
	event.preventDefault();
	var contactNoDiv = $('#social-media');
	if (contactNoDiv.find('.social-btn').length > 0) {
		contactNoDiv.find('.social-btn:last').remove();
		updateSelectedOptions();
	}
}

$(document).ready(function () {
	// Scroll To Top ===============================================================================
	$(window).on('scroll', function () {
		var $scrollToTopButton = $('#scroll-to-top');
		if ($(window).scrollTop() > 0) {
			$scrollToTopButton.show();
		} else {
			//$scrollToTopButton.hide();
		}
	});

	$('#scroll-to-top').on('click', function () {
		$('html, body').animate({ scrollTop: 0 }, 'smooth');
	});

	// Scroll To Bottom ==================================================================================
	$(window).on('scroll', function () {
		var $scrollToBottomButton = $('#scroll-to-bottom');
		if ($(window).scrollTop() < $(document).height() - $(window).height() - 0) {
			$scrollToBottomButton.show();
		} else {
			//$scrollToBottomButton.hide();
		}
	});

	$('#scroll-to-bottom').on('click', function () {
		$('html, body').animate({ scrollTop: $(document).height() }, 'smooth');
	});
	$('#scroll-to-bottom').click(() => {
		window.scrollTo({
			bottom: 0,
			behavior: 'smooth'
		});
	});


	$('.pageUrl input').on('keyup', function () {
		var url = $(this).val();
		var cleanedUrl = url.replace(/[^a-z0-9-_]/g, ''); // only allows -, _, A-Z, a-z, 0-9

		if (url !== cleanedUrl) {
			$(this).val(cleanedUrl.toLowerCase());
			$('#urlError').text("Use only a-z, 0-9, - , _").show();
		} else {
			$('#urlError').text("").hide();
		}
	});

	$('.pageUrl input').on('blur', function () {
		if ($(this).val().length < 1) {
			$('#urlError').text("Page Url").fadeIn();
		} else {
			$('#urlError').text("").fadeOut();
		}
	});

	// ~~~~~~~Industries Options ~~~~~~

	var IndustryOptions = [
		"Finance",
		"E_Comm_Sites",
		"All_Devices",
		"Internet_Services",
		"Educational_Institute",
		"Private_Institute",
		"Hospital",
		"Private_Clinic",
		"Transportation_Services",
		"Hotel_Restaurant",
		"People",
		"Stand_Alone"
	];
	var industrySTD = {
		"Finance": "Finance",
		"E_Comm_Sites": "E_Comm_Sites",
		"All_Devices": "All_Devices",
		"Internet_Services": "Internet_Services",
		"Educational_Institute": "Educational_Institute",
		"Private_Institute": "Private_Institute",
		"Hospital": "Hospital",
		"Private_Clinic": "Private_Clinic",
		"Transportation_Services": "Transportation_Services",
		"Hotel_Restaurant": "Hotel_Restaurant",
		"People": "People",
		"Stand_Alone": "Stand_Alone"
	};

	// Add options to select
	var $select = $('#industry_select');
	IndustryOptions.forEach(function (industry) {
		var selectedIndustry = industry.toLowerCase();
		var IndustryKey = Object.keys(industrySTD).find(key => key.toLowerCase() === selectedIndustry);
		$select.append('<option value="' + industrySTD[IndustryKey] + '">' + industry + '</option>');
	});
});

