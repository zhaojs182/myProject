<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
import {tagCategoryListService,tagListService,tagAddService,tagDeleteService} from '@/api/tag.js'
//tag分类数据模型
const categorys = ref([ ])
//用户搜索时选中的分类id
const tagcategoryId = ref('')
//文章列表数据模型
const tags = ref([])
const title=ref('')
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数
import useUserInfoStore from '../../stores/userInfo'
const userInfoStore=useUserInfoStore()
//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    tagList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    tagList();
}
const tagCatagoryList = async () => {
    let result = await tagCategoryListService();
    console.log("这是传回来的查询所有tag分类",result)
    categorys.value = result.data.data.list;
    // console.log("这是存到categorys里面的值",categorys.value)
}
import { onMounted } from 'vue'
onMounted(() => {
    console.log("这是userInfo的信息",userInfoStore.info)
    tagCatagoryList();
    tagList();
});
// articleCategoryList();
// articleList();
//获取tag列表数据
const tagList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        tagcategoryId: tagcategoryId.value ? tagcategoryId.value : null,
        userid:userInfoStore.info.id
    }
    let result = await tagListService(params);
    console.log("这是tag列表数据result的返回值:", result);
    //渲染视图
    tags.value = result.data.data.pageBean.items;
    total.value = result.data.data.pageBean.total;
    tags.value.forEach(tag => {
        // 查找分类对应的名称
        const category = categorys.value.find(c => c.id === tag.cid);
        if (category) {
            // 添加分类名称
            tag.categoryName = category.tagcategory;
        }
    });
    // console.log("这是articles的值", articles);
}
import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const tagModel = ref({
    id: '',
    cid: '',
    tname:''
})
import { useTokenStore } from '../../stores/token';
const tokenStore = useTokenStore()
//添加文章
import { ElMessage } from 'element-plus'
const addTag = async (clickState) => {
    //把发布状态赋值给数据模型
    // console.log("提交的数据：", articleModel.value);
    //调用接口
    let result = await tagAddService(tagModel.value);
    ElMessage.success(result.msg ? result.msg : '添加成功');

    //让抽屉消失
    visibleDrawer.value = false;

    //刷新当前列表
    tagList()
}
const showTag = (row) => {
    // 打开抽屉
    visibleDrawer.value = true;
    // 设置抽屉标题为“编辑文章”
    title.value = "编辑文章";
    
    // 将选中的文章数据回显到表单
    tagModel.value={
        id: row.id,
        cid: row.cid,
        tname:row.tname

    }
    // articleModel.value = {
    //     title: row.title,
    //     categoryId: row.categoryId,
    //     coverImg: row.coverImg,
    //     content: row.content,
    //     state: row.state
        
    // };
    tagModel.value.id = row.id
};
import {ElMessageBox} from 'element-plus'
const deleteTag = (row) => {
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
            let result = await tagDeleteService(row.id);
            console.log("这是删除后的result:"+result)
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //刷新列表
            tagList()
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了删除',
            })
        })
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章tag管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true;title='添加文章tag'">添加文章tag</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章tag分类:">
                <el-select placeholder="请选择" v-model="tagcategoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.tagcategory" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="tagList()">搜索</el-button>
                <el-button @click="tagcategoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="tags" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title">
            <template #default="{ row }">
                     {{ row.tname }}
            
            </template>
        </el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showTag(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteTag(row)"></el-button>
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




        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="tagModel" label-width="100px">
                <el-form-item label="文章tag名字">
                    <el-input v-model="tagModel.tname" placeholder="请输入tag名字"></el-input>
                </el-form-item>
                <el-form-item label="文章tag分类">
                    <el-select placeholder="请选择" v-model="tagModel.cid">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.tagcategory" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="info" @click="addTag()">提交</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>



    </el-card>
</template>
<style lang="scss" scoped>
.cover-img {
    width: 100%;                /* 图片宽度自适应 */
    height: 130px;              /* 设置固定的高度 */
    object-fit: cover;          /* 保持图片比例，不会拉伸，填充整个容器 */
    border-radius: 8px;         /* 可选：设置圆角 */
    overflow: hidden;           /* 确保图片不溢出容器 */
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