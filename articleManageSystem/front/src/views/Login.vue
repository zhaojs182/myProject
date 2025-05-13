<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
const registerData = ref({
    username: '',
    password: '',
    rePassword: '',
    phonenum:'',
    captchanum: ''
})

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次确认密码'))
    } else if (value !== registerData.value.password) {
        callback(new Error('请确保两次输入的密码一样'))
    } else {
        callback()
    }
}

//定义表单校验规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' }
    ],
    phonenum:[
    { required: true, message: '请输入手机验证码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ]
}
import { userRegisterService, userLoginService } from '@/api/user.js'
const register = async () => {
    let result = await userRegisterService(registerData.value);
        console.log("打印完整的返回对象",result);  // 打印完整的返回对象
    console.log(result.code);  // 打印 code 的值

    if (result.data.code === 200) {
        // alert(result.msg?result.msg:'注册成功');
        ElMessage.success(result.msg ? result.msg : '注册成功')

    }else {
        ElMessage.error('注册失败')
    }
}
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token.js'
const router = useRouter()
const tokenStore = useTokenStore();
import { defineToken } from '../stores/tokenString';
import useUserInfoStore from '../stores/userInfo';
const userInfoStore = useUserInfoStore();
const login = async () => {
    if(registerData.value.captchanum!==captchaText.value){
        ElMessage.error('验证码错误')
        return;
    }
    console.log("发送给后端的数据:", registerData.value);
    let result = await userLoginService(registerData.value);
    if (result.data.code === 200) {
        // alert(result.msg?result.msg:'登录成功');
        ElMessage.success(result.msg ? result.msg : '登录成功')
        let tokenString = defineToken()
        tokenString.TokenString = result.data.data.token
        console.log("这是tokenstring:" + result.data.data.token)

        tokenStore.setToken(result.data.data.token)
        // 打印存储的 id
        if (tokenStore.token) {
            console.log('登录成功, token:', tokenStore.token);
            //调用接口
            let result1 = registerData.value
            //数据存储到pinia中
            userInfoStore.setInfo(result1);
            console.log("这是userInfoStore里面的东西", userInfoStore.info)
        } else {
            console.log('未成功保存 token');
        }
        router.push('/layout')
    } else if(result.data.code === 501){
        ElMessage.error('登录失败.')
    }else {
        ElMessage.error('登录失败.')
    }

}
//定义函数,清空数据模型的数据
const clearRegisterData = () => {
    registerData.value = {
        username: '',
        password: '',
        rePassword: '',
        captchanum: '',
        phonenum:''
    }
}



import { onMounted } from 'vue';
import { loginCaptcha } from '@/api/user.js';

const captchaImgUrl = ref('');  // 用于存储验证码图像的 Base64 数据
const captchaText = ref('');  // 用于存储验证码文本

// 获取验证码图片和文本
const getCaptchaImageAndText = async () => {
    const result = await loginCaptcha();  // 请求验证码的 Base64 编码图像和文本
    captchaImgUrl.value = result.captchaImage;  // 设置验证码图像的 Base64 数据
    captchaText.value = result.captchanum;  // 设置验证码文本
};

// 刷新验证码
const refreshCaptcha = async () => {
    try {
        await getCaptchaImageAndText();  // 获取新的验证码图片和文本
        registerData.value.captchanum = captchaText.value;  // 将验证码文本填入表单
    } catch (error) {
        console.error('刷新验证码失败:', error);
    }
};
// 页面加载时获取验证码图片和文本
onMounted(() => {
    getCaptchaImageAndText()
});
//发送以及刷新手机验证码
// import {getPhonenumCaptcha} from '@/api/user.js'
// const phonenumCaptcha=async()=>{
//     let result=await getPhonenumCaptcha(registerData.value.phonenum);
//     console.log("如你所愿,发送了手机验证码",result)
//     if (result.data.code === 507) {
//         // 处理手机号已注册的情况
//         ElMessage.error('手机号已经注册')
//     }
//     //记得写看看是否正常发送，然后要加一个时间等待间隔
// }
import { getPhonenumCaptcha } from '@/api/user.js';

const phonenumCaptcha = async () => {
    try {
        // 发送手机验证码请求
        let result = await getPhonenumCaptcha(registerData.value.phonenum);
        console.log("如你所愿,发送了手机验证码", result);

        // 检查返回的 code 是否为 507（手机号已注册）
        if(result.code === 507){
            ElMessage.error('手机号已经注册');
        }
        if (result.data.code === 507) {
            ElMessage.error('手机号已经注册');
        } else if (result.data.code === 200) {
            ElMessage.success('验证码发送成功');
        } else {
            ElMessage.error('发送验证码失败');
        }
    } catch (error) {
        // 捕获并处理请求中的任何错误
        ElMessage.error('手机号已经注册');
    }
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <el-form-item prop="phonenum">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入手机号"
                        v-model="registerData.phonenum"></el-input>
                        <el-button class="button" type="primary" auto-insert-space @click="phonenumCaptcha()">发送验证码</el-button>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="Lock" type="text" placeholder="请输入验证码"
                        v-model="registerData.captchanum"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item>
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>


                <!-- 显示验证码图片 -->
                <el-form-item>
                    <div>
                        <!-- 使用 Base64 编码的图像显示验证码 -->
                        <img :src="'data:image/jpeg;base64,' + captchaImgUrl" alt="验证码" @click="refreshCaptcha"
                            style="cursor: pointer;">
                    </div>
                </el-form-item>
                <!-- 输入验证码 -->
                <el-form-item>
                    <el-input :prefix-icon="Lock" type="text" placeholder="请输入验证码"
                        v-model="registerData.captchanum"></el-input>
                </el-form-item>

                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    // .bg {
    //     background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
    //         url('@/assets/login_bg.jpg') no-repeat center / cover;
    //     border-radius: 0 20px 20px 0;
    // }
    .bg {
    background: url('@/assets/loginpic.jpg') no-repeat center / cover;
    border-radius: 0 20px 20px 0;
    }


    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>