var websocket;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost/answer");
}
else {
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    setMessageInnerHTML("error");
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
    setMessageInnerHTML("open");
}

var inited = false;
//接收到消息的回调方法
websocket.onmessage = function (event) {
    console.log(event.data);
    var json = JSON.parse(event.data);
    if (json.method == 'init') {
        if (json.errorCode == 0) {
            inited = true;
            $('#div1').css('display', 'none');
            var time = json.message;
            if (time > new Date().getTime()) {
                $('#wait').css('display', 'block');
            }
        } else {
            alert(json.message);
        }
    } else if (json.method = 'question') {
        if (json.errorCode == 0) {
            isCommit = false;
            $('#div1').css('display', 'none');
            var que = json.rows[0];
                refreshForm(que);
            if (inited) {
                $('#comit').css('display', 'block');
            } else {
                $('#comit').css('display', 'none');

            }
            $('#wait').css('display', 'none');
            $('#div2').css('display', 'block');

        } else {
            alert(json.message);
            rank();
        }

    } else if (json.method = 'answer') {
        if (json.errorCode == 0) {
            var que = json.message;
            $('#answer'+que).css('color','green');
        } else {
            alert(json.message);
            rank();
        }
    } else {
        alert(event.data);
    }

}

//连接关闭的回调方法
websocket.onclose = function () {
    setMessageInnerHTML("close");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    // document.getElementById('message').innerHTML += innerHTML + '<br/>';
    alert(innerHTML);
}

//关闭连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function send(message) {
    websocket.send(message);
}