<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko=kr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>부트스트랩 채팅화면</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<style>
div.container{
	margin-top:30px;
}

div.chat-content{
	height:400px;
	overflow:auto;
}

div#yourMsg{
	display: none;
}

.chat-content .me {
	color: red;
	text-align: right;
}

.chat-content .others {
	color: blue;
	text-align: left;
}
</style>
</head>
<body>

<div class="container">
	<h1>채팅</h1>
	<input type="text" id="sessionId" value="">
	<hr>
	
	  <div class="panel panel-warning">
        <div class="panel-heading">
        	<h3 class="panel-title">
        		<span class="glyphicon glyphicon-commnets"></span>채팅 제목
        	</h3>
      	</div>
      <div class="panel-body chat-content"></div>
      <div class="panel-footer">
      	<div class="row form-group" id="yourName">
      		<div class="col-md-6">
      			<input type="text" id="userName" class="form-control" placeholder="체팅에서 사용할 이름을 입력하세요">
      		</div>
      		<div class="col-md-3">
      			<button id="btnOpen" class="btn btn-success btn-block">웹소켓 연결(채팅 참여하기)</button>
      		</div>
      		<div class="col-md-3">
	  			<button id="btnQuit" class="btn btn-danger btn-block" disabled>웹소켓 연결 종료(채팅 종료하기)</button>
      		</div>
      	</div>
      	<div class="row form-group" id="yourMsg">
      		<div class="col-md-1">
      			<label for="chatMsg">메세지</label>
      		</div>
      		<div class="col-md-8">
      			<input type="text" id="chatMsg" class="form-control" placeholder="보내실 메시지를 입력해주세요">
      		</div>
      		<div class="col-md-3">
	  			<button id="btnSend" class="btn btn-primary btn-block">보내기</button>
      		</div>
      	</div>
      </div>
    </div>
	
</div>

<script src="/script/jquery-3.5.1.js"></script>
<script src="/script/bootstrap.js"></script>
<script>
	var ws; //웹소켓 객체를 저장할 변수 선언

	function wsOpen(){
		//브라우저가 웹소켓서버에 연결 요청하기
		ws = new WebSocket('ws://' + location.host + '/chatting');

		wsEvt(); //웹소켓 객체에 이벤트 콜백메소드 등록하기
		
	}//wsOpen()
	
	function wsEvt(){

		// 소켓서버와 연결이 되면 호출됨
		ws.onopen = function () {
			alert('연결됨');

			$('#btnOpen').prop('disabled', true);
			$('#btnQuit').prop('disabled', false);
			$('#userName').prop('readonly', true);
			
			$('#yourMsg').show();
			$('#chatMsg').focus();
		};

		// 소켓서버로부터 데이터를 받으면 호출됨
		ws.onmessage = function (event) {
			var msg = event.data;

			if (msg != null && msg.trim() != '') {

				var obj = JSON.parse(msg); // json문자열을 json객체로 변환해서 리턴함

				if (obj.type == 'getId') {
					$('#sessionId').val(obj.sessionId);
				}else if(obj.type == 'message'){
					if (obj.sessionId == $('#sessionId').val()) {
						$('.chat-content').append('<p class="me">나 : ' + obj.msg + '</p>');
					} else {
						$('.chat-content').append('<p class="others">' + obj.userName + ' : ' + obj.msg + '</p>');
					}
				}
				
				
				//$('.chat-content').append('<p>' + msg + '</p>');
			}

			var top = $('.chat-content').prop('scrollHeight');
			$('.chat-content').prop('scrollTop', top);
		};

		// 소켓서버와 연결이 끊기면 호출됨
		ws.onclose = function () {
			alert('끊어짐');
			
			$('#btnOpen').prop('disabled', false);
			$('#btnQuit').prop('disabled', true);
			$('#userName').prop('readonly', false);
			
			$('#yourMsg').hide();
			$('#userName').focus();
		};

		// 소켓서버와 통신오류가 있을때 호출됨
		ws.onerror = function () {
			alert('웹소켓 통신 오류 발생...');
		};
	}//wsEvt()
	
	function send(){
		var obj = {
				type: 'message',
				sessionId: $('#sessionId').val(),
				userName:$('#userName').val(),
				msg:$('#chatMsg').val()
		};

		var jsonStr = JSON.stringify(obj); //json객체를 json문자열로 변환해서 리턴함

		ws.send(jsonStr); //웹소켓 서버에 json문자열 전송

		$('#chatMsg').val('');
	}//send()
	
		$('#btnOpen').on('click', function () {
			
			var userName = $('#userName').val();

			if (userName == '') {
				alert('사용자 이름을 입력해주세요.');
				$('#userName').focus();
			} else {
				wsOpen(); // 웹소켓서버에 연결하기
			}
		});

		$('#btnQuit').on('click', function () {
			ws.close(); // 웹소켓 서버 연결을 끊기
		});

		$('#btnSend').on('click', function () {
			send();
		});

		$(document).on('keydown', function (event) {
			if (event.keyCode == 13) { // 엔터키를 눌렸을때
				send();
			}
		});
		
	// 채팅이름 입력상자에 포커스 주기
	$('#userName').focus();
</script>
</body>
</html>