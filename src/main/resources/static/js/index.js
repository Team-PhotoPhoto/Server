var main = {
    init : function () {
        let _this = this;
        $('#btn-profile-update-signup').on('click', function () {
            _this.profileupdate();
        });

        $('#btn-profile-update').on('click', function () {
            _this.profileupdate();
        });

        $('#btn-image-upload').on('click', function () {
            _this.imageupload();
        });

        $('#btn-posts-save').on('click', function () {
            _this.postssave();
        });
    },
    profileupdate : function () {
        let data = {
            imageUrl: 'https://www.xexymix.com/shopimages/xexymix/0080030003422.jpg?1670981028',
            nickname: $('#nickname').val(),
            frameType: 'BROWN',
            wallType: 'STRIPE',
            emailNoti: 'trashgeun@gmail.com',
            noti: false
        };
        $.ajax({
            type: 'PUT',
            url: '/profile',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('닉네임 설정 완료.');
            window.location.href = '/gallery/me';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    imageupload : function () {

        $.ajax({
            type: 'POST',
            url: '/posts/image',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: {}
        }).done(function(data, textStatus, xhr){

            let postId = xhr.responseText;
            console.log(postId)

            alert('이미지 업로드 완료');
            window.location.href = '/posts/' + postId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    postssave : function () {
        let data = {
            postId: 1,
            senderName: $('#nickname').val(),
            title: 'BROWN',
            comment: 'STRIPE',
            imageUrl: '',
            receiverUserId: 1,
            read: true,
            open: true
        };

        $.ajax({
            type: 'POST',
            url: '/posts/'+ 1,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 저장되었습니다.');
            window.location.href = '/gallery/me';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();