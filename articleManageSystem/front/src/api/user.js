import request from '@/utils/request.js'


export const userRegisterService=(registerData)=>{
    const params=new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key]);
    }
    return request.post('/user/register',params);
    
}
export const userLoginService=(registerData)=>{
    const params=new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key]);}
    
    return request.post('/user/login',params);
}
//获取用户详细信息
export const userInfoService = ()=>{
    return request.get('/user/getUserInfo')
}

//修改个人信息
export const userInfoUpdateService = (userInfoData)=>{
    console.log("这是往后面传的修改数据",userInfoData)
   return request.put('/user/update',userInfoData)
}

//修改头像
// export const userAvatarUpdateService = ()=>{
//     const params = new URLSearchParams();
//     // params.append('avatarUrl',avatarUrl)
//     return request.patch('/user/updateAvatar')
// }
export const userAvatarUpdateService = (imgUrl) => {
    const params = new URLSearchParams();
    params.append('imgUrl', imgUrl);  // 添加图片地址到请求参数

    return request.post('/user/updataAvatar', params)
}
// 请求验证码图片和验证码文本
export const loginCaptcha = () => {
    return request.get('/user/captcha')  // 请求验证码图像和文本
        .then((response) => {
            const { captchaImage, captchanum } = response.data.data;
            // console.log("返回的验证码图像 Base64 数据：", captchaImage);
            // console.log("返回的验证码文本：", captchanum);
            return { captchaImage, captchanum };  // 返回图像和文本
        });
}
export const getPhonenumCaptcha=(phonenum)=>{
    console.log("这是发出的手机号：",phonenum)
    return request.post('/user/getPhonenumCaptcha', { phonenum })
}




export const promoteRoleService = (traceNo) => {
  
    
    const payUrl = `http://localhost:8080/alipay/pay?subject=权限升级&traceNo=${traceNo}&totalAmount=10`;

    // 使用 window.open 打开一个新窗口
    window.open(payUrl, '_blank');
    // request.get(`/alipay/pay?subject=权限升级&traceNo=${traceNo}&totalAmount=10`)
     
   
};

export const promoteRoleEndService=(tradeNo, userId)=>{
    
    return request.post("/alipay/qualify", {
        tradeNo: tradeNo,   // 订单号
        userId: userId      // 用户 ID
    });
}

  
