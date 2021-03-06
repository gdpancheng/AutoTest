define(function(require, exports, module) {
	// ********************************************************************************************************************************************
	// 变量区                **************************************************************************************************************************
	//*********************************************************************************************************************************************

	var myForm =null;
	// ********************************************************************************************************************************************
	// 对外方法            **************************************************************************************************************************
	//*********************************************************************************************************************************************
	/**-------------------------------------form表单初始化------------------------------------*/
	exports.init = function() {
        var formData = [
            {type: "settings", position: "label-left", labelWidth: 130, inputWidth: 120},
            {type: "fieldset", label: "欢迎登录", inputWidth: 340, list:[
                    {type: "input", name:"account" , label: "用户名", value: ""},
                    {type: "password", name:"pwd" , label: "密码", value: ""},
                    {type: "label", label: "",list:[
                                        {type: "button", name:"Submit" ,value: "登录", offsetLeft: 50, offsetTop: 10, inputWidth: 50},
                    					{type: "newcolumn"},
                    					{type: "button", name:"Reset" ,value: "取消", offsetLeft: 8, offsetTop: 10}
                    ]},
            ]}
        ];
        myForm = new dhtmlXForm("login_div", formData);
        myForm.attachEvent("onButtonClick", function(name){login(name);});
        myForm.attachEvent("onEnter",function(){login("Submit");});
        myForm.setItemFocus("account");
	};
	// ********************************************************************************************************************************************
	// 内部方法            **************************************************************************************************************************
	//*********************************************************************************************************************************************
	/**-------------------------------------登录---------------------------------------*/
    function login(name){
        if(name=="Submit"){
            var account = myForm.getInput("account");
            var pwd = myForm.getInput("pwd");
            var req = {jsonContent:'{"function":"F100030","user":{"id":"'+account.value+'","password":"'+pwd.value+'"},"content":{}}'};
            $.ajax({
                type: "POST",
                url: "/action/lfs/action/FunctionAction",
                data: req,
                dataType: "json",
                success: function (message) {
                    if(message.head.errorNo==""){
                        Cookies.set('_is_login', "true");
                        window.location.href="/main.html";
                    }else{
                        dhtmlx.confirm({
                            title:"登录错误！",
                            type:"confirm-warning",
                            text:message.head.errorNo+":"+message.head.errorInfo,
                            callback:function(result){ }
                        });
                    }
                },
                error: function (message) {
                    dhtmlx.confirm({
                        title:"登录错误！",
                        type:"confirm-warning",
                        text:"网络错误！",
                        callback:function(result){ }
                    });
                }
            });
        }
        if(name=="Reset"){
            var account = myForm.getInput("account");
            var pwd = myForm.getInput("pwd");
            account.value="";
            pwd.value="";
        }
    }
});