function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    //值打印，验证数据获取是否成功
    // console.log(questionId);
    // console.log(comment);
    //前端回复内容是否为空校验
    if(!content){
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
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        //通过JSON.stringify将JavaScript对象转换为字符串
        dataType: "json",
        //向服务器发送数据时一般是字符串
        success: function (response) {
            // 回复成功，局部界面进行关闭刷新
            if (response.code == 200) {
                // $("#comment_section").hide();
                //window.location.reload方法自动刷新界面，显示回复已提交，但是其实刷新了整个界面
                window.location.reload();
            } else {
                //回复之前是否进行了登陆，未登陆则跳转至登陆界面
                if (response.code == 2003) {
                    //确认框
                    var isAccepted = confirm(response.message);
                    //确定要进行登陆，打开登陆界面
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=9b4fbc7b1ae29bd26829&redirect_uri=http://localhost:8000/callback&scope=user&state=1")
                        //localStorage本地存储，用于长久保存某个网站的数据
                        window.localStorage.setItem("closable","true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        //出错处理方案
        error: function (response) {
            //回复成功，局部界面进行关闭刷新
            // if (response.code == 200) {
            //     $("#comment_section").hide();
            // } else {
            //     //回复之前是否进行了登陆，未登陆则跳转至登陆界面
            //     if (response.code == 2003) {
            //         //确认框
            //         var isAccepted = confirm(response.message);
            //         //确定要进行登陆，打开登陆界面
            //         if(isAccepted){
            //             window.open("https://github.com/login/oauth/authorize?client_id=9b4fbc7b1ae29bd26829&redirect_uri=http://localhost:8000/callback&scope=user&state=1")
            //             //localStorage本地存储，用于长久保存某个网站的数据
            //             windows.localStorage.setItem("closable","true");
            //         }
            //     } else {
            //         alert(response.message);
            //     }
            // }
            console.log(response);
        }
    });
    // console.log(1);
}