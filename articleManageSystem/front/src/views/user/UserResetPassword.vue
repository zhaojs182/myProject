<script setup>
import { onMounted, ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js';
import {useOrderStore} from '@/stores/order.js'
import { userInfoUpdateService, promoteRoleService, promoteRoleEndService } from '@/api/user.js'
import { ElMessage, ElDialog } from 'element-plus'

const userInfoStore = useUserInfoStore()
const userInfo = ref({ ...userInfoStore.info })
const orderStore=useOrderStore()
// 弹窗相关的状态
const dialogVisible = ref(false);
const dialogMessage = ref('');
const loading = ref(false);

// 修改用户信息
const updateUserInfo = async () => {
    console.log("修改个人的信息", userInfo.value)
    let result = await userInfoUpdateService(userInfo.value);
    ElMessage.success(result.msg ? result.msg : '修改成功');

    // 更新 Pinia 状态
    userInfoStore.setInfo(userInfo.value)
}

// 升级权限
const promoteRole = async () => {
    loading.value = true;
    const traceNo = Math.floor(Math.random() * 1000000); // 生成一个 6 位随机数字
    orderStore.setOrder(traceNo)
        // 调用 promoteRoleService 打开支付页面
        await promoteRoleService(traceNo);
}
const promoteRoleEnd=async()=>{
    console.log("这是要验证的订单号和用户id",orderStore.order,userInfo.value.id)
    const result=await promoteRoleEndService(orderStore.order,userInfo.value.id)
    console.log("这是验证支付的返回信息",result )
}
onMounted(() => {
    console.log("这是要验证的订单号和用户id",orderStore.order,userInfo.value.id)
});

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="修改密码">
                        <el-input v-model="userInfo.password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>

        <!-- 权限升级按钮 -->
        <el-button type="primary" @click="promoteRole" :loading="loading">升级权限</el-button>
        <el-button type="primary" @click="promoteRoleEnd" >验证支付</el-button>
       
    </el-card>
</template>
