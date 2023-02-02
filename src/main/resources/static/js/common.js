function sleep(n) {
	var start = new Date().getTime();
//  console.log('休眠前：' + start);
	while (true) {
		if (new Date().getTime() - start > n) {
			break;
        }
    }
       //console.log('休眠后：' + new Date().getTime());
}

function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}
function isIntEmpty(obj){
    if(typeof obj == "undefined" || obj == null || (obj != 0 && obj == "")){
        return true;
    }else{
        return false;
    }
}

//校验是否为空+提示
function checkIsOrNull(type,name){
	if(undefined===type||null===type||""===type){
		dorado.MessageBox.alert("请选择"+name);
		return true;
	}
}

//关闭当前tab-来自消息中心
function closeCurrentTab(){
	var id = "${param.id}";
	if(!isEmpty(id)){ 
		top.viewMain.id("tabControl").get("currentTab").close()
	} 
}

//查询条件输入框有输入值时才展示叉号
function autoAddTrigger(autofrom){
    autofrom.get("elements").each(function(element) {

    function createClearTrigger() {
        return new dorado.widget.Trigger({
            exClassName: "d-trigger-clear",
            iconClass: "d-trigger-icon-clear",
            onExecute: function(self, arg) {
                arg.editor.set("text", "");
                arg.editor.post();
            }
        })
    }

    var editor = element.get("editor");

    if (!(editor instanceof dorado.widget.TextEditor)) {
        return;
    }
	//获取trigger,非空=非输入框,不应用此trigger
	//console.log(editor.get("trigger"));
	 if (!isEmpty(editor.get("trigger"))) {
        return;
    }
	//获取trigger,非空=非输入框,不应用此trigger
	//console.log(editor.get("trigger"));
	 if (!isEmpty(editor.get("trigger"))) {
        return;
    }
	
    editor.$orgiTrigger = editor.get("trigger");

	
    //console.log("editable", editor.get("editable"));
    //console.log("readOnly", editor.get("readOnly"));

    if (editor.get("readOnly") || editor._readOnly2) {
        return;
    }

    editor.bind("onTextEdit", function(self, arg){
        if (self.get("text")){
          addClearTrigger(self);
        } else {
          resetTrigger(self);
        }
    });
    var clearTrigger = createClearTrigger();
    var orgiTrigger;

    function addClearTrigger(editor){
        if (editor.get("text") && !editor.$clearTrigger) {
          editor.$clearTrigger = true;
              if (!editor.$orgiTrigger) {
                editor.set("trigger", clearTrigger);
              } else if ( editor.$orgiTrigger.each) {
                    editor.set("trigger", [clearTrigger, ...editor.$orgiTrigger]);
                } else {
                    editor.set("trigger", [clearTrigger, editor.$orgiTrigger]);
                }
        }
    }

    function resetTrigger(editor) {
        editor.set("trigger", editor.$orgiTrigger);
        editor.$clearTrigger = false;
    }

    jQuery(element.getDom())
        .mouseenter(function() {
            //console.log(element.get("property"), "mouseenter");
            orgiTrigger = editor.get("trigger");
            //console.log("orgiTrigger", orgiTrigger);
            //console.log("text", editor.get("text"));
            //console.log("editor", editor);
            addClearTrigger(editor);
        })
        .mouseleave(function() {
            resetTrigger(editor);
        });
});
}