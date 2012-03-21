(function ($) {

    $.fn.propertyAutoComplete = function (type) {
        return this.each(function () {
            var $this = $(this);
            var selected;
            var suggestions;
            var timer;

            var loader = $('<img src="/static/ajax-loader.gif" style="visibility:hidden"></img>')
            var suggestionsContainer = $("<div class='suggestions'></div>");

            $this.after(loader);
            loader.after(suggestionsContainer);

            $this.keydown(function(event) {
              if (event.keyCode == 13 && suggestionsContainer.css("visibility") == "visible") {
                  event.preventDefault();
              }
            });

            $this.keyup(function (event) {
                if (selected && event.keyCode == 38 ) {
                    selectASuggestion(selected.prev());
                } else if (event.keyCode == 40 ) {
                    if (selected) {
                        selectASuggestion(selected.next())
                    } else {
                        selectASuggestion(suggestionsContainer.children().first())
                    }

                } else if (event.keyCode != 37 && event.keyCode != 38 && event.keyCode != 39 && event.keyCode != 40) {
                    clearTimeout(timer);
                    timer = setTimeout(showSuggestions, 500);
                }
            });

            var showSuggestions = function() {
                // Clear the container
                suggestionsContainer.empty();
                var searchFor = $this.val();
                suggestions = new Array();
                jQuery.ajaxSetup({
                    'beforeSend': function (xhr) {
                        xhr.setRequestHeader("Accept", "text/plain");
                    }
                });
                searchFor = (searchFor == "" ? "" : '' + searchFor);
                showLoader(true);

                jQuery.get("filenames", {
                    q: searchFor
                }, function (data) {
                    if (data != "") {
                        suggestions = data.split(",");
                        suggestionsContainer.css("visibility", "visible");
                        setSuggestions();
                    } else {
                        suggestionsContainer.css("visibility", "hidden");
                    }
                    showLoader(false);
                });
            }

            var showLoader = function(display) {
                if (display) {
                    loader.css("visibility", "visible");
                } else {
                    loader.css("visibility", "hidden");
                }

            }

            var setSuggestions = function() {
                var stringTemplate = "";
                for (var i in suggestions) {
                    var suggestion = $("<div class='suggestion'>" + suggestions[i] + "</div>")
                    suggestion.click(function() {
                    if (type.autoSubmit) {
                        $this.closest("form").submit();
                    } else {
                        suggestionsContainer.hide();
                    }
                    });
                    suggestion.mouseover(function(event) {
                        selectASuggestionFromEvent(event);
                    });
                    suggestionsContainer.append(suggestion);
                }
            }

            var selectASuggestionFromEvent = function(event) {
                var suggestion = $(event.target)
                 selectASuggestion(suggestion);
            }

            var selectASuggestion = function(toSelect) {
                if(toSelect.length < 1) {
                    return;
                }
                selected = toSelect;
                $this.val(selected.html());
                suggestionsContainer.find(".suggestion").css("background", "white");
                selected.css("background", "#E5FFE5");

            }
        });
    };
})(jQuery);

jQuery(document).ready(function() {
   jQuery('.property-autocomplete').propertyAutoComplete({autoSubmit: false});
});
