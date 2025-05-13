<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()

//用户头像地址
// const imgUrl= avatar
import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();
const imgUrl = ref(userInfoStore.info.userPic ? userInfoStore.info.userPic : avatar)
//读取token信息
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
//图片上传成功的回调
const uploadSuccess = (result) => {
    console.log("这是图片上传成功的回调",result.data)
    //回显图片
    imgUrl.value = result.data.url
}
import { ElMessage } from 'element-plus'
import { userAvatarUpdateService } from '../../api/user';
const updateAvatar = async () => {
    let result = await userAvatarUpdateService(imgUrl.value);
    ElMessage.success(result.msg ? result.msg : '添加成功');
    
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload class="avatar-uploader" :show-file-list="false" :auto-upload="true"  ref="uploadRef"
                    action="http://localhost:8080/file/upload"
                    name="file" :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSuccess">
                    <img v-if="imgUrl" :src="imgUrl" class="file" />
                    <img v-else src="@/assets/avatar.jpg" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large"
                    @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
                    上传头像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        /* 固定头像上传区域为方形 */
        .el-upload {
            width: 150px;         /* 设置上传区域的宽度 */
            height: 150px;        /* 设置上传区域的高度 */
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;   /* 可根据需要保留圆角 */
            cursor: pointer;
            position: relative;
            overflow: hidden;     /* 隐藏溢出的部分 */
            transition: var(--el-transition-duration-fast);
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        /* 处理图标区域，保持与上传区域一致 */
        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 150px;         /* 设置图标区域宽度 */
            height: 150px;        /* 设置图标区域高度 */
            text-align: center;
        }

        /* 设置上传的头像为自适应填充，保持方形区域 */
        .file {
            width: 100%;           /* 设置图片宽度为 100% */
            height: 100%;          /* 设置图片高度为 100% */
            object-fit: cover;     /* 确保图像自适应填充方形区域，并裁剪多余部分 */
            border-radius: 6px;    /* 可选择性添加圆角 */
        }
    }
}
</style>