<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref, onMounted } from 'vue'
import { userListService, userDeleteService, resetPasswordService, DisableOrOpenUsers } from '@/api/admin.js'
//文章列表数据模型
const users = ref([
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    userList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    userList();
}
//获取文章列表数据
//查询分类数据
const userList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
    }
    let result = await userListService(params);
    console.log("这是result的返回值:", result);
    //渲染视图
    users.value = result.data.data.pageBean.items;
    console.log("这是user", users.value)
    total.value = result.data.data.pageBean.total;
}
onMounted(() => {
    userList();
});
import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const userModel = ref({
    nickname: '',
    password: '',
    email: '',
    userPic: '',
    phonenum: '',
    role: '',
    state: ''
})
import { useTokenStore } from '../../stores/token';
const tokenStore = useTokenStore()
import { ElMessage } from 'element-plus'
import { ElMessageBox } from 'element-plus'
const deleteUser = (row) => {
    //提示用户  确认框

    ElMessageBox.confirm(
        '你确认要删除该分类信息吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //调用接口
            console.log("这是删除方法")
            let result = await userDeleteService(row.id);
            console.log("这是删除后的result:" + result)
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //刷新列表
            userList()
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了删除',
            })
        })
}
const resetPassword = (row) => {
    let result = resetPasswordService(row.id);
    userList();
}
const DisableOrOpen = async () => {
    if (selectedUsers.value.length === 0) {  // 检查是否选中用户
        ElMessage.warning('请先选择用户');
        return;
    }

    try {
        // 调用禁用或启用用户的 API
        await DisableOrOpenUsers(selectedUsers.value);
        ElMessage.success('操作成功');
        userList();  // 请求成功后刷新用户列表
    } catch (error) {
        console.error('禁用或启用用户失败:', error);  // 打印错误信息到控制台
        ElMessage.error('禁用或启用用户失败');
    }
};

const selectedUsers = ref([]);  // 存储选中的用户ID

const onSelectionChange = (val) => {
    selectedUsers.value = val.map(user => user.id);  // 只存储用户ID
    console.log("选中的用户ID:", selectedUsers.value);
};

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>用户管理</span>
                <div class="extra">
                    <el-button type="primary" @click="DisableOrOpen()">禁用或启用</el-button>
                </div>
            </div>
        </template>

        <!-- 用户列表 -->
        <el-table :data="users" style="width: 100%" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>

            <el-table-column label="头像" width="200">
                <template #default="{ row }">
                    <img v-if="row.userPic" :src="row.userPic" alt="头像" class="cover-img" />
                    <span v-else>暂无头像</span>
                </template>
            </el-table-column>

            <el-table-column label="昵称" prop="nickname"></el-table-column>
            <el-table-column label="邮箱" prop="email"></el-table-column>
            <el-table-column label="手机号" prop="phonenum"></el-table-column>
            <el-table-column label="角色" prop="role"></el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>

            <el-table-column label="操作(重置密码为123456)" width="100">
                
                <template #default="{ row }">
                    <!-- <el-button :icon="Edit" circle plain type="primary" @click="resetPassword(row)"></el-button> -->
                    <el-button :icon="Edit" circle plain type="primary" @click="resetPassword(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteUser(row)"></el-button>
                </template>
            </el-table-column>

            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
</template>



<style lang="scss" scoped>
.cover-img {
    width: 100%;
    /* 图片宽度自适应 */
    height: 130px;
    /* 设置固定的高度 */
    object-fit: cover;
    /* 保持图片比例，不会拉伸，填充整个容器 */
    border-radius: 8px;
    /* 可选：设置圆角 */
    overflow: hidden;
    /* 确保图片不溢出容器 */
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}

.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>