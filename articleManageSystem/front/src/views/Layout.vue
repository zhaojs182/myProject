<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import { userInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
const getUserInfo = async () => {
    //调用接口
    let result = await userInfoService();
    role.value = result.data.data.user.role
    console.log("这是用户的详细信息", result.data.data)
    console.log("这是role里面存进去的信息", role.value)
    //数据存储到pinia中
    userInfoStore.setInfo(result.data.data.user);
    console.log("存进去的信息", userInfoStore.info)
}
import { ref, onMounted, reactive } from 'vue';
onMounted(() => {
    getUserInfo();
});
const role = ref()
//条目被点击后,调用的函数
import { useRouter } from 'vue-router'
const router = useRouter();
import { ElMessage, ElMessageBox } from 'element-plus'
const handleCommand = (command) => {
    //判断指令
    if (command === 'logout') {
        //退出登录
        ElMessageBox.confirm(
            '您确认要退出吗?',
            '温馨提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(async () => {
                //退出登录
                //1.清空pinia中存储的token以及个人信息
                tokenStore.removeToken()
                userInfoStore.removeInfo()

                //2.跳转到登录页面
                router.push('/login')
                ElMessage({
                    type: 'success',
                    message: '退出登录成功',
                })

            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '用户取消了退出登录',
                })
            })
    } else {
        //路由
        router.push('/user/' + command)
    }
}
</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <el-menu-item v-if="role === 1" index="/article/category">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-menu-item index="/article/tagcategory">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章tag分类</span>
                </el-menu-item>
                <el-menu-item index="/article/tagmanage">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章tag详情</span>
                </el-menu-item>
                <el-menu-item index="/others/chart">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>图表查看</span>
                </el-menu-item>
                <el-menu-item v-if="role !== 0" index="/others/openaidialogue">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>AI问答</span>
                </el-menu-item>
                <el-menu-item v-if="role === 1" index="/admin/controller">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>管理员面板</span>
                </el-menu-item>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>用户名：<strong>{{ userInfoStore.info.nickname }}</strong></div>
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.userPic ? userInfoStore.info.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    <router-view></router-view>
                </div>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>文章管理系统 ©2024 Created by sevetar</el-footer>
        </el-container>
    </el-container>
</template>


<!-- <style lang="scss" scoped>
.layout-container {
    height: 100vh;
    position: relative; /* 确保背景图片不会被其他元素遮挡 */

    /* 背景图片加透明度 */
    background-image: url('@/assets/bg.png'); /* 请替换为你的背景图片路径 */
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    opacity: 0.8; /* 设置背景不透明度为30% */
    z-index: -1; /* 保证背景图片在其他内容之下 */

    .el-aside {
        background-color: #0e4cd2; /* 可以保持这个颜色，也可以为其添加透明度 */
        /* 这里可以修改整体的背景透明度，不会影响背景图片的显示 */
        background-color: rgba(14, 76, 210, 0.6); /* 修改为RGBA，添加透明度 */
        
        &__logo {
            height: 120px;
            background: url('@/assets/relogo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #a4bd13;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #bd0606;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #bb0c29;
    }
}
</style> -->

<style lang="scss" scoped>
.layout-container {
    height: 100vh;
    position: relative;
    /* 确保背景图片不会被其他元素遮挡 */

    background-image: url('@/assets/bg.png');
    /* 请替换为你的背景图片路径 */
    background-size: cover;
    background-position: center;

    .el-main {
        flex: 1;
        /* 使 main 内容区域填满剩余空间 */
        overflow: auto;
        /* 确保内容溢出时可以滚动 */
        padding: 20px;
        box-sizing: border-box;
    }

    .chart-wrapper {
        max-width: 100%;
        /* 确保宽度不超出父容器 */
        overflow: hidden;
        /* 防止内容溢出 */
    }

    .el-aside {
        background-color: #0e4cd2;

        &__logo {
            height: 120px;
            background: url('@/assets/relogo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #a4bd13;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #bd0606;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #bb0c29;
    }
}
</style>