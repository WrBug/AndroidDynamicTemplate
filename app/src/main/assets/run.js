function save(){
       JSExecutor.getFormDataCallback(function(failed,formData,msg){
        if(failed){
            JSExecutor.showToast(msg);
            return
        }
        var username=JSExecutor.getVal('2_0')+'';
        var password=JSExecutor.getVal('2_1')+'';
        if(username!='123456'||password!='Q1111111'){
            JSExecutor.showToast('用户名或密码有误');
            return
        }
        JSExecutor.submit(formData);
       });
}

function toggleMoreInfoAction(){
    var isChecked=JSExecutor.getVal('3')==1;
    if(isChecked){
        JSExecutor.setProp('3_1','hide',0);
    }else{
         JSExecutor.setProp('3_1','hide',1);
   }
}