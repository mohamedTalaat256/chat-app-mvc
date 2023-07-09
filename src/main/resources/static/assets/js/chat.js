"use strict";


var activeUsers = [];

var base_url = window.location.origin;
var KTAppChat = function () {
    var _chatAsideEl;
    var _chatAsideOffcanvasObj;
    var _chatContentEl;

    // Private functions
    var _initAside = function () {
        // Mobile offcanvas for mobile mode
        _chatAsideOffcanvasObj = new KTOffcanvas(_chatAsideEl, {
            overlay: true,
            baseClass: 'offcanvas-mobile',
            //closeBy: 'kt_chat_aside_close',
            toggleBy: 'kt_app_chat_toggle'
        });

        // User listing
        var cardScrollEl = KTUtil.find(_chatAsideEl, '.scroll');
        var cardBodyEl = KTUtil.find(_chatAsideEl, '.card-body');
        var searchEl = KTUtil.find(_chatAsideEl, '.input-group');

        if (cardScrollEl) {
            // Initialize perfect scrollbar(see:  https://github.com/utatti/perfect-scrollbar)
            KTUtil.scrollInit(cardScrollEl, {
                mobileNativeScroll: true,  // Enable native scroll for mobile
                desktopNativeScroll: false, // Disable native scroll and use custom scroll for desktop
                resetHeightOnDestroy: true,  // Reset css height on scroll feature destroyed
                handleWindowResize: true, // Recalculate hight on window resize
                rememberPosition: true, // Remember scroll position in cookie
                height: function () {  // Calculate height
                    var height;

                    if (KTUtil.isBreakpointUp('lg')) {
                        height = KTLayoutContent.getHeight();
                    } else {
                        height = KTUtil.getViewPort().height;
                    }

                    if (_chatAsideEl) {
                        height = height - parseInt(KTUtil.css(_chatAsideEl, 'margin-top')) - parseInt(KTUtil.css(_chatAsideEl, 'margin-bottom'));
                        height = height - parseInt(KTUtil.css(_chatAsideEl, 'padding-top')) - parseInt(KTUtil.css(_chatAsideEl, 'padding-bottom'));
                    }

                    if (cardScrollEl) {
                        height = height - parseInt(KTUtil.css(cardScrollEl, 'margin-top')) - parseInt(KTUtil.css(cardScrollEl, 'margin-bottom'));
                    }

                    if (cardBodyEl) {
                        height = height - parseInt(KTUtil.css(cardBodyEl, 'padding-top')) - parseInt(KTUtil.css(cardBodyEl, 'padding-bottom'));
                    }

                    if (searchEl) {
                        height = height - parseInt(KTUtil.css(searchEl, 'height'));
                        height = height - parseInt(KTUtil.css(searchEl, 'margin-top')) - parseInt(KTUtil.css(searchEl, 'margin-bottom'));
                    }

                    // Remove additional space
                    height = height - 2;

                    return height ;  ///////
                }
            });
        }
    }

    return {
        // Public functions
        init: function () {
            // Elements
            _chatAsideEl = KTUtil.getById('kt_chat_aside');
            _chatContentEl = KTUtil.getById('kt_chat_content');

            // Init aside and user list
            _initAside();

            // Init inline chat example
            KTLayoutChat.setup(KTUtil.getById('kt_chat_content'));

            // Trigger click to show popup modal chat on page load
            if (KTUtil.getById('kt_app_chat_toggle')) {
                setTimeout(function () {
                    KTUtil.getById('kt_app_chat_toggle').click();
                }, 1000);
            }
        }
    };
}();

var KTLayoutChat = function () {

    var _init = function (element) {
        var scrollEl = KTUtil.find(element, '.scroll');
        var cardBodyEl = KTUtil.find(element, '.card-body');
        var cardHeaderEl = KTUtil.find(element, '.card-header');
        var cardFooterEl = KTUtil.find(element, '.card-footer');

        if (!scrollEl) {
            return;
        }

        // initialize perfect scrollbar(see:  https://github.com/utatti/perfect-scrollbar)
        KTUtil.scrollInit(scrollEl, {
            windowScroll: false, // allow browser scroll when the scroll reaches the end of the side
            mobileNativeScroll: true,  // enable native scroll for mobile
            desktopNativeScroll: false, // disable native scroll and use custom scroll for desktop
            resetHeightOnDestroy: true,  // reset css height on scroll feature destroyed
            handleWindowResize: true, // recalculate hight on window resize
            rememberPosition: true, // remember scroll position in cookie
            height: function () {  // calculate height
                var height;

                if (KTUtil.isBreakpointDown('lg')) { // Mobile mode
                    return KTUtil.hasAttr(scrollEl, 'data-mobile-height') ? parseInt(KTUtil.attr(scrollEl, 'data-mobile-height')) : 400;
                } else if (KTUtil.isBreakpointUp('lg') && KTUtil.hasAttr(scrollEl, 'data-height')) { // Desktop Mode
                    return parseInt(KTUtil.attr(scrollEl, 'data-height'));
                } else {
                    height = KTLayoutContent.getHeight();

                    if (scrollEl) {
                        height = height - parseInt(KTUtil.css(scrollEl, 'margin-top')) - parseInt(KTUtil.css(scrollEl, 'margin-bottom'));
                    }

                    if (cardHeaderEl) {
                        height = height - parseInt(KTUtil.css(cardHeaderEl, 'height'));
                        height = height - parseInt(KTUtil.css(cardHeaderEl, 'margin-top')) - parseInt(KTUtil.css(cardHeaderEl, 'margin-bottom'));
                    }

                    if (cardBodyEl) {
                        height = height - parseInt(KTUtil.css(cardBodyEl, 'padding-top')) - parseInt(KTUtil.css(cardBodyEl, 'padding-bottom'));
                    }

                    if (cardFooterEl) {
                        height = height - parseInt(KTUtil.css(cardFooterEl, 'height'));
                        height = height - parseInt(KTUtil.css(cardFooterEl, 'margin-top')) - parseInt(KTUtil.css(cardFooterEl, 'margin-bottom'));
                    }
                }

                // Remove additional space
                height = height - 2;

                return height ;
            }
        });
    }

    return {
        init: function () {
            // init modal chat example
            _init(KTUtil.getById('kt_chat_modal'));

            // trigger click to show popup modal chat on page load
            if (encodeURI(window.location.hostname) == 'keenthemes.com' || encodeURI(window.location.hostname) == 'www.keenthemes.com') {
                setTimeout(function () {
                    if (!KTCookie.getCookie('kt_app_chat_shown')) {
                        var expires = new Date(new Date().getTime() + 60 * 60 * 1000); // expire in 60 minutes from now

                        KTCookie.setCookie('kt_app_chat_shown', 1, { expires: expires });

                        if (KTUtil.getById('kt_app_chat_launch_btn')) {
                            KTUtil.getById('kt_app_chat_launch_btn').click();
                        }
                    }
                }, 2000);
            }
        },

        setup: function (element) {
            _init(element);
        }
    };
}();


const dates = new Set();
const renderDate = (dateNum) => {
    dates.add(dateNum);
    return '<div class="d-flex flex-column mb-2 align-items-center">'
        + '<div class="mt-2 rounded p-2 bg-light-success text-dark-50 font-weight-bold  text-center">'
        + dateNum
        + '</div>'
        + '</div>';

};

const dateFormatter = (date) => {

    //here we were subtracting our date from current time which will be in milliseconds
    const dateDifferenceInTime =  new Date().getTime() - new Date(date).getTime();

    // conerting milli seconds to days
    // (1000 milliseconds * (60 seconds * 60 minutes) * 24 hours)
    const dateDifferenceInDays =
        dateDifferenceInTime / (1000 * 60 * 60 * 24);

    //After returning in particular formats as of our convinent
    if (dateDifferenceInDays < 1) {
        return moment(date).format("LT");// 10:04 am
    } else if (dateDifferenceInDays < 2) {
        return "Yesterday"; // just YesterDay
    } else if (dateDifferenceInDays <= 7) {
        return moment(date).format("dddd") + " at " + moment(date).format("LT");//like monday , tuesday , wednesday ....
    } else {
        return moment(date).format("l");// if it was more than a week before it will returns as like 05/23/2022
    }
};

function getChatMessages(secondPersonId) {
    var url = base_url + '/chat/get_chat_messages/' + secondPersonId;

    jQuery.ajax({
        url: url,
        type: 'get',
        success: function (data) {

            $('#selected_user_id').val(data.user_details.id);
            $('#selected_user_name').html(data.user_details.fullName);

            $('#selected_user_image').html('<img alt="Pic" src="' + base_url + '/photos/' + data.user_details.image + '" />');
            $('#un_read_for_user_' + secondPersonId).html('');

            if ($('#user_status_' + secondPersonId).val() == 1) {
                $('#selected_user_status').html('online');
            } else {
                $('#selected_user_status').html('offline');
            }



            var html = '';
            data.messages.reverse();
            data.messages.map((message) => {

                const chatDate = message.createdDate.substring(0, 10);

                if (!dates.has(chatDate)) {
                    html = html + renderDate(chatDate);
                }
                //if message in
                if (message.senderId === secondPersonId) {
                    html = html + renderMessageIn(message);

                } else {
                    html = html + renderMessageOut(message);
                }

                if(message.isRead == 0){
                    stompClient.send("/app/status-message", {}, JSON.stringify(message));
                }



            });
            $('#private_messages_box').html(html);
            $('#unread_lable_for_user_'+secondPersonId).html(0);
            $('#unread_lable_for_user_'+secondPersonId).hide();
            $('#type_message_footer').show();
            
            var element = KTUtil.getById('kt_chat_content')
            var messagesEl = KTUtil.find(element, '.messages');
            var scrollEl = KTUtil.find(element, '.scroll');

            scrollEl.scrollTop = parseInt(KTUtil.css(messagesEl, 'height'));
            var ps;
            if (ps = KTUtil.data(scrollEl).get('ps')) {
                ps.update();
            }
            $('.user-item').css(
                {
                    "background-color": "#ffffff",
                });
            $('#user_item_'+secondPersonId).css(
                {
                    "background-color": "#e7e7e7",
                    "border-radius": "12px"
                });

                updateAllToSeen(secondPersonId);

            
            
        },
        error: function (error) {
            alert(error);
        }
    });
}

var stompClient = null;

const connect = () => {
    let Sock = new SockJS(base_url + '/ws');
    stompClient = Stomp.over(Sock);
    stompClient.connect({}, onConnected, onError);
}

const onConnected = () => {
    var userId = $('#auth_user_id').val();

    stompClient.subscribe('/user/' + userId + '/private',
        //on receive message
        (payload) => {
            const newMessage = JSON.parse(payload.body);
            var selectedUserId = $('#selected_user_id').val();
            if (newMessage.senderId == selectedUserId) {
                
                $('#private_messages_box').append(renderMessageIn(newMessage));
                var objDiv = document.getElementById("scrollable_div");
                objDiv.scrollTop = objDiv.scrollHeight;
                //send request to update message isRead to 1
                updateMessageIsReadToSeen(newMessage);

            } else {        
                //notify
              
                $.notify(RendernotificationContent(newMessage),
                    {
                        allow_dismiss: true,
                        newest_on_top: true,
                        delay: 15000,
                        type: 'warning',
                        
                        offset: 50,
                        animate: { enter: 'slideInRight', exit: 'slideInRight' }
                    }
                );

                
                
                
            }

            //update side list


            //move to the top
            $('#user_item_'+newMessage.senderId).prependTo('#users_chat');

            //change message in side chats
            $('#message_for_user_'+newMessage.senderId).html(newMessage.message);
            //increment unread messages count
            var currentCount = Number($('#unread_lable_for_user_'+newMessage.senderId).html());

            $('#unread_lable_for_user_'+newMessage.senderId).html(Number(currentCount+1));
            $('#unread_lable_for_user_'+newMessage.senderId).show();


            getUsersChats();
        });




    stompClient.subscribe('/user/' + userId + '/private-status',
        //on receive seen
        (payload) => {
            const newMessage = JSON.parse(payload.body);
            $('#message_status_' + newMessage.id).removeClass(["fa", "flaticon2-checkmark"]).addClass(["fas", "flaticon2-check-mark","text-primary"]);
        });
}

const onError = (err) => {
    console.log(err);
}

const renderMessageIn = (message) => {
    var html = '';

    html += ' <div class="d-flex flex-column mb-5 align-items-start">';
    html += '<div class="d-flex align-items-center">';
    html += '<div class="symbol symbol-circle symbol-40 mr-3">';
    html += $('#selected_user_image').html();
    html += '</div>';
    html += '<div>';
    html += '<a href="#"  class="text-dark-75 text-hover-primary font-weight-bold font-size-h6"> ' + $('#selected_user_name').html() + ' </a><br>';
    html += '<span class=" font-size-sm font-italic"> ' + dateFormatter(message.createdDate) + ' </span>';
    html += '</div>';
    html += '</div>';
    html += '<div class="mt-2 rounded p-3 bg-light-primary text-dark app_font_md text-right max-w-400px message_item">';


    if (message.media != null) {
        html += '<img class="image-input image-input-outline message_media_image" src="' + base_url + '/media/' + message.media + '"/>';
        html += '<br>';
    }
    html += message.message;


    //  html +=  '<span class="font-weight-bold font-size-sm">';
    //   html +=  dateFormatter(message.createdDate);
    //   html += '</span>';


    html += '</div>';
    html += '</div>';

    return html;
}

const renderMessageOut = (message) => {
    var html = '';

    html += ' <div class="d-flex flex-column mb-5 align-items-end">';
    html += '<div class="d-flex align-items-center">';
    html += '<div class="align-items-end text-right">';
    html += '<a href="#"  class="text-dark-75 text-hover-primary font-weight-bold font-size-h6"> You </a><br>';
    html += '<span class="font-size-sm font-italic"> ' + dateFormatter(message.createdDate) + ' </span>';
    html += '</div>';
    html += '<div class="symbol symbol-circle symbol-40 ml-3">';
    html += '<img alt="Pic" src="' + base_url + '/photos/' + $('#auth_user_image').val() + '" />';
    html += '</div>';
    html += '</div>';
    html += '<div class="mt-2 rounded p-3 bg-light-success text-dark app_font_md text-left max-w-400px">';


    if (message.media != null) {
        html += '<img class="image-input image-input-outline message_media_image mb-2" src="' + base_url + '/media/' + message.media + '"/>';
        html += '<br>';
    }
    html += message.message;
    //   html +=  '<span class="font-weight-bold font-size-sm">';
    //   html +=  dateFormatter(message.createdDate);

    if (message.isRead == 0) {
        html += '<i id="message_status_' + message.id + '" class="fa fa-regular flaticon2-checkmark ml-2 float-right"></i>';
    } else {
        html += '<i id="message_status_' + message.id + '" class="fas text-primary flaticon2-check-mark ml-2 float-right"></i>';
    }

    //   html +=  '</span>';
    html += '</div>';
    html += '</div>';

    return html;


}

function updateMessageIsReadToSeen(message) {
    var url = base_url + '/chat/update_message_is_read_to_seen?id=' + message.id;
    jQuery.ajax({
        url: url,
        type: 'post',
        success: function (data) {
            console.log('message with id : ' + message.id + ' has been seen');
            //send message tells that is the message has been seen

            //send notification to user of type message status and update message icon to seen
            stompClient.send("/app/status-message", {}, JSON.stringify(message));
        },
        error: function (error) {
            alert(error);
        }
    });
}


function updateCollectionsMessagesIsReadToSeen(secondPersonId) {
    var url = base_url + '/chat/update_collections_messages_is_read_to_seen?secondPersonId=' + secondPersonId;
    jQuery.ajax({
        url: url,
        type: 'post',
        success: function (data) {
            console.log('message with id : ' + message.id + ' has been seen');
            //send message tells that is the message has been seen

            //send notification to user of type message status and update message icon to seen
            stompClient.send("/app/status-message", {}, JSON.stringify(message));
        },
        error: function (error) {
            alert(error);
        }
    });
}




var avatar4 = new KTImageInput('kt_image_4');

avatar4.on('cancel', function (imageInput) {
    $('#kt_image_4').hide();
});

avatar4.on('change', function (imageInput) {

});
avatar4.on('remove', function (imageInput) { $('#kt_image_4').hide(); });


$('#toggle_upload_image').on('click', function () {
    $('#kt_image_4').toggle();
});

$('#btn_send_message').on('click', function (e) {


    var url = base_url + '/chat/send_chat_messages';

    var myFormData = new FormData();
    var message = $('#message_text_area').val();
    var receiverId = Number($('#selected_user_id').val());


    myFormData.append('message', message);
    myFormData.append('receiverId', receiverId);
    myFormData.append('media', $('#media').prop('files')[0]);


    var data = myFormData;
    jQuery.ajax({
        url: url,
        type: 'post',
        enctype: 'multipart/form-data',
        data: data,
        contentType: false,
        cache: false,
        processData: false,
        success: function (data) {
            $('#kt_image_4').hide();
            stompClient.send("/app/private-message", {}, JSON.stringify(data));
            var html = '';

            html = renderMessageOut(data);
            $('#private_messages_box').append(html);
            $('#message_text_area').val('');
            $('#media').val(null);
            $('#message_for_user_' + receiverId).html(data.message);
            $('#message_date_for_user_' + receiverId).html(dateFormatter(data.message.createdDate));
            var element = KTUtil.getById('kt_chat_content')
            var messagesEl = KTUtil.find(element, '.messages');
            var scrollEl = KTUtil.find(element, '.scroll');


            scrollEl.scrollTop = parseInt(KTUtil.css(messagesEl, 'height'));
            var ps;
            if (ps = KTUtil.data(scrollEl).get('ps')) {
                ps.update();
            }

            $('#user_item_'+receiverId).prependTo('#users_chat');
        },
        error: function (error) {
            alert(error);
        }
    });
}
);

jQuery(document).ready(function () {

    connect();
    KTAppChat.init();
    KTLayoutChat.init();
    getActiveUsers();
    getUsersChats();
});

function getActiveUsers() {
    var url = base_url + '/chat/active_users';
    jQuery.ajax({
        url: url,
        type: 'get',
        success: function (data) {
            activeUsers = data.active_users;

            console.log(activeUsers);
        },
        error: function (error) {
            alert(error);
        }
    });
}

function getUsersChats() {
    var url = base_url + '/chat/get_users_chats';
    jQuery.ajax({
        url: url,
        type: 'get',
        success: function (data) {

            const actives = activeUsers;

            console.log(data);
            var html = '';

            data.users.map((user) => {


                html += ' <div id="user_item_'+user.id+'" onclick="getChatMessages(' + user.id + ')" class="user-item d-flex align-items-center justify-content-between mb-2 p-3">';


                html += '  <div class="d-flex align-items-center">';
                html += '   <div class="symbol symbol-circle symbol-50 mr-3">';
                html += '    <img alt="Pic" src="/photos/' + user.image + '" />';

                html += '   </div>';
                html += '   <div class="d-flex flex-column">';
                html += '   <a href="#"  class="text-dark-75 text-hover-primary app_font">' + user.name + '</a>';
                html += '    <span id="message_for_user_' + user.id + '" class="app_font_md">' + user.lastMessage + '</span>';


                if (activeUsers.includes(user.email)) {
                    console.log(activeUsers.includes(user.email));
                    html += '   <span class="label label-lg label-dot label-success user_status_dot"></span>';
                    html += ' <input type="hidden" id="user_status_' + user.id + '" value="1" />';
                }

                html += '    </div>';
                html += '    </div>';
                html += '   <div class="d-flex flex-column align-items-end">';

                if(user.lastMessageDate != ''){
                html += '    <span id="message_date_for_user_' + user.id + '" class="text-muted font-weight-bold font-size-sm mt-5">' +  dateFormatter(user.lastMessageDate) + '</span>';
                }
                if (user.unread != 0) {
                    html += ' <span class="label label-lg label-warning" id="unread_lable_for_user_'+user.id+'">' + user.unread + '</span>';
                }
                html += ' </div>';
                html += ' </div>';

            });

            $('#users_chat').html(html);
        },
        error: function (error) {
            alert(error);
        }
    });

}


function updateAllToSeen(secondPersonId){
    var url = base_url + '/chat/update_all_to_seen?secondPersonId=' + secondPersonId;
    jQuery.ajax({
        url: url,
        type: 'post',
        success: function (data) {
            console.log(data);
        },
        error: function (error) {
            alert(error);
        }
    });
}


function RendernotificationContent(notification){
    var html='';
    html += '<div class="d-flex align-items-center justify-content-between" >';
    html += '   <div class="d-flex align-items-center">';
    html += '       <div class="symbol symbol-circle symbol-50 mr-3">';
    html += '           <img alt="Pic" src="/photos/' + notification.senderImage + '" />';
    html += '       </div>';
    html += '       <div class="d-flex flex-column" >';
    html += '           <a href="#"  class="text-hover-primary" style="color: #ffffff; font-size: 18px">' + notification.senderName + '</a>';
    html += '           <span class="">' + notification.message + '</span>';
    html += '       </div>';
    html += '   </div>';
    html += '</div>';


    return html;


}