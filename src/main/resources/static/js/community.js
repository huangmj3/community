/*提交回复*/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    //值打印，验证数据获取是否成功
    // console.log(questionId);
    // console.log(comment);
    //前端回复内容是否为空校验
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }
    //ajax POST方法
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        //JSON通常用于与服务端交换数据
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        //通过JSON.stringify将JavaScript对象转换为字符串
        dataType: "json",
        //向服务器发送数据时一般是字符串
        success: function (response) {
            // 回复成功，局部界面进行关闭刷新
            //回复之前是否进行了登陆，未登陆则跳转至登陆界面
            if (response.code == 200) {
                // $("#comment_section").hide();
                //window.location.reload方法自动刷新界面，显示回复已提交，但是其实刷新了整个界面
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    //确定要进行登陆，打开登陆界面
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=9b4fbc7b1ae29bd26829&redirect_uri=http://localhost:8000/callback&scope=user&state=1")
                        //localStorage本地存储，用于长久保存某个网站的数据
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
    });
}

//二级评论功能
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

//展开二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    // //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        //评论资源已加载过，无需重新加载
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            //资源有更新，重新加载
            //JSON路径要正确，在id路径前加/
            $.getJSON("/comment/" + id, function (data) {
                //循环遍历，展示二级评论,同时使用reverse方法使回复按照时间倒叙插入
                $.each(data.data.reverse(), function (index, comment) {
                    //子标签
                    //头像信息左体
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    //头像信息右体，注意内嵌关系
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class":"menu"
                    }).append($("<span/>", {
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    //头像信息整合
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    //二级评论
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });

        }

        // var commentBody = $("comment-body"+id);
        // var items = [];
        //
        // $.each( data.data, function(comment) {
        //     //子标签
        //     var c = $("<div/>",{
        //         "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
        //         html: comment.content
        //     });
        //     items.push(c);
        // });
        //
        // commentBody.append( $("<div/>",{
        //     "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
        //     "id":"comment-"+id,
        //     //作用为写入上述的html元素
        //     html: items.join("")
        // }));
        //
        // $("<div/>",{
        //     "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
        //     "id":"comment-"+id,
        //     //作用为写入上述的html元素
        //     html: items.join("")
        // }).appendTo(commentBody);

    }

    // e.hasClass("in");
    // //标记二级评论展开状态
    // e.setAttribute("data-collapse","in");
    // 评论已被展开,移除class
    // if (e.hasClass("in")) {
    //     comments.removeClass("in");
    //     //评论被启动的状态css移除
    //     e.classList.remove("active");
    // } else {
    //     //添加html class
    //     comments.addClass("in");
    //     e.classList.add("active");
    // }
}