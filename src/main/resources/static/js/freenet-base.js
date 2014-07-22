$(document).ready(function() {

	/*
	If there is an app open, clone and paste the app menu in the central position on the navbar
	*/
	if ($("#open-app"))  {
		// Get the app name
		var selected_menu = $("#open-app").data("openappname");
		var open_app  = $("#"+selected_menu+"-menu").parents("li").clone();
		//Tansform the subdropdown menu into a normal dropdown
		$(open_app).find("a").addClass("dropdown-toggle");
		$(open_app).find("a").attr("data-toggle","dropdown");
		$(open_app).find("a").append("<b class=\"caret\"></b>");
		$(open_app).find("ul li a").removeClass("dropdown-toggle").attr("data-toggle","");
		$(open_app).find("ul li a b").remove();
		//Print the menu
		$("#open-app").html($(open_app).html());
	}
});
