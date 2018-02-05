var App = function () {

    var isMainPage = false;
    var isMapPage = false;
    var isIE8 = false;


    var handlePortletSortable = function () {
        if (!jQuery().sortable) {
            return;
        }
        $(".sortable").sortable({
            connectWith: '.sortable',
            iframeFix: false,
            items: 'div.widget',
            opacity: 0.8,
            helper: 'original',
            revert: true,
            forceHelperSize: true,
            placeholder: 'sortable-box-placeholder round-all',
            forcePlaceholderSize: true,
            tolerance: 'pointer'
        });

    }

    var handleWidgetTools = function () {
        jQuery('.widget .tools .icon-remove').click(function () {
        	if(window.confirm("确定在视图上移除此模块吗？")){
        		jQuery(this).parents(".widget").parent().remove();
        	}
        });

        jQuery('.widget .tools .icon-refresh').click(function () {
            var el = jQuery(this).parents(".widget");
            App.blockUI(el);
            window.setTimeout(function () {
                App.unblockUI(el);
            }, 1000);
        });

        jQuery('.widget .tools .icon-chevron-down, .widget .tools .icon-chevron-up').click(function () {
            var el = jQuery(this).parents(".widget").children(".widget-body");
            if (jQuery(this).hasClass("icon-chevron-down")) {
                jQuery(this).removeClass("icon-chevron-down").addClass("icon-chevron-up");
                el.slideUp(200);
            } else {
                jQuery(this).removeClass("icon-chevron-up").addClass("icon-chevron-down");
                el.slideDown(200);
            }
        });
    }


    var handleDeviceWidth = function () {
        function fixWidth(e) {
            var winHeight = $(window).height();
            var winWidth = $(window).width();
            //alert(winWidth);
            //for tablet and small desktops
            if (winWidth < 1125 && winWidth > 767) {
                $(".responsive").each(function () {
                    var forTablet = $(this).attr('data-tablet');
                    var forDesktop = $(this).attr('data-desktop');
                    if (forTablet) {
                        $(this).removeClass(forDesktop);
                        $(this).addClass(forTablet);
                    }

                });
            } else {
                $(".responsive").each(function () {
                    var forTablet = $(this).attr('data-tablet');
                    var forDesktop = $(this).attr('data-desktop');
                    if (forTablet) {
                        $(this).removeClass(forTablet);
                        $(this).addClass(forDesktop);
                    }
                });
            }
        }

        fixWidth();

        running = false;
        jQuery(window).resize(function () {
            if (running == false) {
                running = true;
                setTimeout(function () {
                    // fix layout width
                    fixWidth();
                    // fix calendar width by just reinitializing
                    handleDashboardCalendar();
                    if (isMainPage) {
                        handleDashboardCalendar(); // handles full calendar for main page
                    } else {
                        handleCalendar(); // handles full calendars
                    }
                    // fix vector maps width
                    if (isMainPage) {
                        jQuery('.vmaps').each(function () {
                            var map = jQuery(this);
                            map.width(map.parent().parent().width());
                        });
                    }
                    if (isMapPage) {
                        jQuery('.vmaps').each(function () {
                            var map = jQuery(this);
                            map.width(map.parent().width());
                        });
                    }
                    // fix event form chosen dropdowns
                    $('#event_priority_chzn').width($('#event_title').width() + 15);
                    $('#event_priority_chzn .chzn-drop').width($('#event_title').width() + 13);

                    $(".chzn-select").val('').trigger("liszt:updated");
                    //finish
                    running = false;
                }, 200); // wait for 200ms on resize event           
            }
        });
    }



    
	
    var handleDateTimePickers = function () {

        if (!jQuery().daterangepicker) {
            return;
        }

        
        $('.date-picker').datepicker();

    }
	
	var handleUniform = function () {
        if (!jQuery().uniform) {
            return;
        }
        if (test = $("input[type=checkbox]:not(.toggle), input[type=radio]:not(.toggle)")) {
            test.uniform();
        }
    }
	



    var handleGoTop = function () {
        /* set variables locally for increased performance */
        jQuery('#footer .go-top').click(function () {
            App.scrollTo();
        });

    }

    return {

        //main function to initiate template pages
        init: function () {

           

            //handleDeviceWidth(); // handles proper responsive features of the page
            //handleChoosenSelect(); // handles bootstrap chosen dropdowns

            //handleScrollers(); // handles slim scrolling contents
            handleUniform(); // handles uniform elements
            //handleClockfaceTimePickers(); //handles form clockface timepickers
            //handleTagsInput() // handles tag input elements
            //handleTables(); // handles data tables
            //handleCharts(); // handles plot charts
            handleWidgetTools(); // handles portlet action bar functionality(refresh, configure, toggle, remove)
            //handlePulsate(); // handles pulsate functionality on page elements
            //handlePeity(); // handles pierty bar and line charts
            //handleGritterNotifications(); // handles gritter notifications
           //handleTooltip(); // handles bootstrap tooltips
            //handlePopover(); // handles bootstrap popovers
            //handleToggleButtons(); // handles form toogle buttons
            //handleWysihtml5(); //handles WYSIWYG Editor 
            handleDateTimePickers(); //handles form timepickers
            //handleColorPicker(); // handles form color pickers
            //handleFancyBox(); // handles fancy box image previews
            //handleStyler(); // handles style customer tool
            //handleMainMenu(); // handles main menu
            //handleFixInputPlaceholderForIE(); // fixes/enables html5 placeholder attribute for IE9, IE8
            handleGoTop(); //handles scroll to top functionality in the footer
            //handleAccordions();
            //handleFormWizards();
            //handleSidebarToggler();
        },

        // wrapper function to scroll to an element
        scrollTo: function (el) {
            pos = el ? el.offset().top : 0;
            jQuery('html,body').animate({
                scrollTop: pos
            }, 'slow');
        }


    };

}();


function mcdDialog(target,options){
	return $("#"+target).dialog(options);
}