
function insertValidation(event){
	let isValid = true;

    // Clear previous error messages and styles
    $("#show-error").text("");
    $("input, textarea").css("border", "");

    // Get form fields by name
    const orgFullName = $("input[name='org_full_name']").val().trim();
    const orgAliasName = $("input[name='org_alias_name']").val().trim();
    const orgTitle = $("input[name='org_title']").val().trim();
    const orgLogo = $("input[name='org_logo']").val().trim();
    const aboutCompany = $("textarea[name='About_Company']").val().trim();
    const orgAddress = $("input[name='org_address']").val().trim();
    const websiteLink = $("input[name='Web_Link']").val().trim();
    const departNames = $("input[name='Department_Name']");
    const departNumbers = $("input[name='PhoneEmail']");

    // Check if fields are not empty
    if (!orgFullName) {
        $("#show-error").html("Organization Full Name is required.<br>");
        $("input[name='org_full_name']").css("border", "1px solid red");
        return false;
    }
    if (!orgAliasName) {
        $("#show-error").html("Organization Alias Name is required.<br>");
        $("input[name='org_alias_name']").css("border", "1px solid red");
        return false;
    }
    if (!orgTitle) {
        $("#show-error").html("Organization Title is required.<br>");
        $("input[name='org_title']").css("border", "1px solid red");
        return false;
    }
    if (!orgLogo) {
        $("#show-error").html("Organization Logo is required.<br>");
        $("input[name='org_logo']").css("border", "1px solid red");
        return false;
    }
    if (!aboutCompany) {
        $("#show-error").html("About Company is required.<br>");
        $("textarea[name='About_Company']").css("border", "1px solid red");
        return false;
    }
    if (!orgAddress) {
        $("#show-error").html("Organization Address is required.<br>");
        $("input[name='org_address']").css("border", "1px solid red");
        return false;
    }
    if (!websiteLink) {
		$("#show-error").html("Website Link is required.<br>");
        $("input[name='Web_Link']").css("border", "1px solid red");
        return false;
    }
    departNames.each(function() {
        if (!$(this).val().trim()) {
			$("#show-error").html("Department Name is required.<br>");
            $(this).css("border", "1px solid red");
            isValid = false;
            return false; // break out of loop
        }
    });
    departNumbers.each(function() {
        if (!$(this).val().trim()) {
			$("#show-error").html("Phone/Email is required.<br>");
            $(this).css("border", "1px solid red");
            isValid = false;
            return false; // break out of loop
        }
    });
    return isValid;
}


function searchForUpdate(event){
	if(!($('.search_id').val().trim()) && $('.search_id').val() !=0){
		$('#search-msg').text("Enter Search Id");
		event.preventDefault();
		return false;
	}
	else{
		return true;
	}
}

function updateValidation(event){
	let isValid = true;
    // Clear previous error messages and styles
    $("#show-error").text("");
    $("input, textarea").css("border", "");

    // Get form fields by name
    const orgFullName = $("input[name='update_org_name']").val().trim();
    const orgAliasName = $("input[name='update_org_alias']").val().trim();
    const orgTitle = $("input[name='update_org_title']").val().trim();
    const aboutCompany = $("textarea[name='update_About_Company']").val().trim();
    const orgAddress = $("input[name='update_org_address']").val().trim();
    const websiteLink = $("input[name='update_Web_Link']").val().trim();
    const departNames = $("input[name='Department_Name']");
    const departNumbers = $("input[name='PhoneEmail']");

    // Check if fields are not empty
    if (!orgFullName) {
        $("#show-error").html("Organization Full Name is required.<br>");
        $("input[name='org_full_name']").css("border", "1px solid red");
        return false;
    }
    if (!orgAliasName) {
        $("#show-error").html("Organization Alias Name is required.<br>");
        $("input[name='org_alias_name']").css("border", "1px solid red");
        return false;
    }
    if (!orgTitle) {
        $("#show-error").html("Organization Title is required.<br>");
        $("input[name='org_title']").css("border", "1px solid red");
        return false;
    }

    if (!aboutCompany) {
        $("#show-error").html("About Company is required.<br>");
        $("textarea[name='About_Company']").css("border", "1px solid red");
        return false;
    }
    if (!orgAddress) {
        $("#show-error").html("Organization Address is required.<br>");
        $("input[name='org_address']").css("border", "1px solid red");
        return false;
    }
    if (!websiteLink) {
		$("#show-error").html("Website Link is required.<br>");
        $("input[name='Web_Link']").css("border", "1px solid red");
        return false;
    }
    departNames.each(function() {
        if (!$(this).val().trim()) {
			$("#show-error").html("Department Name is required.<br>");
            $(this).css("border", "1px solid red");
            isValid = false;
            return false; // break out of loop
        }
    });
    departNumbers.each(function() {
        if (!$(this).val().trim()) {
			$("#show-error").html("Phone/Email is required.<br>");
            $(this).css("border", "1px solid red");
            isValid = false;
            return false; // break out of loop
        }
    });
    return isValid;
}


function searchForDelete(event){
	if(!($("input[name='deleteId']").val().trim()) && $("input[name='deleteId']").val() >0){
		$('#delete-msg').text("Enter Search Id");
		event.preventDefault();
		return false;
	}
	else{
		return true;
	}
}



function registerValidation(){
	let isValid = true;
    // Clear previous error messages and styles
    $("#show-error").text("");

    // Get form fields by name
    const unique_id = $(".unique_id").val().trim();
    const Fullname = $(".Fullname").val().trim();
    const Emailid = $(".Emailid").val().trim();
    const Mobile = $(".Mobile").val().trim();
    const Address = $(".Address").val().trim();
    const user_type = $(".user_type").val().trim();
    const Password = $(".Password").val().trim();;
    const C_password = $(".C_password").val().trim();;

    // Check if fields are not empty
    if (!unique_id) {
        $("#show-error").html("unique_id is required.<br>");
        $(".unique_id").css("border", "1px solid red");
        return false;
    }
    if (!Fullname) {
        $("#show-error").html("Fullname is required.<br>");
        $(".Fullname").css("border", "1px solid red");
        return false;
    }
    if (!Emailid) {
        $("#show-error").html("Email id is required.<br>");
        $(".Emailid").css("border", "1px solid red");
        return false;
    }

    if (!Mobile) {
        $("#show-error").html("Mobile is required.<br>");
        $(".Mobile").css("border", "1px solid red");
        return false;
    }
    if (!Address) {
        $("#show-error").html("Address is required.<br>");
        $(".Address").css("border", "1px solid red");
        return false;
    }
    if (!user_type) {
		$("#show-error").html("User_Type is required.<br>");
        $(".user_type").css("border", "1px solid red");
        return false;
    }
    if (!Password) {
        $("#show-error").html("Password is required.<br>");
        $(".Password").css("border", "1px solid red");
        return false;
    }
    if (!C_password) {
		$("#show-error").html("Confirm password is required.<br>");
        $(".C_password").css("border", "1px solid red");
        return false;
    }
    if(!(Password === C_password)){
		$("#show-error").html("Password and Confirm password did not matched.<br>");
		$(".Password").css("border", "1px solid red");
		$(".C_password").css("border", "1px solid red");
		return false;
	}
	if(Password === C_password){
		var pattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=])[A-Za-z0-9@#$%^&+=]*$/;
		if (pattern.test(Password)) {
			return true;
        } else {
            $("#show-error").html("Password should be in: UPPERCASE(ABC), lowercase(abc),special character(@,!,#,$...etc), symbol, number (0-9).<br>");
			$(".Password").css("border", "1px solid red");
			$(".C_password").css("border", "1px solid red");
			return false;
        }
	}
	var count = unique_id.length;
	if(count<=5){
		$("#show-error").html("unique_id Must contains 6 or more character.<br>");
        $(".unique_id").css("border", "1px solid red");
        return false;
	}
}

$('#uniqueId').on('input',function(){
	var count = $('#uniqueId').val().length;
	var unique = $('#uniqueId').val().trim();
	$(".unique_id").val(unique);
	if(count<=5){
		$("#show-error").html("unique_id Must contains 6 or more character (no spaces).<br>");
        $(".unique_id").css("border", "1px solid red");
        return true;
	}
	else{
		$("#show-error").html("unique_id Must contains 6 or more character.<br>");
        $(".unique_id").css("border", "1px solid green");
        return true;
	}
});

