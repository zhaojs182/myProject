<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { onMounted, ref } from 'vue'

//文章分类数据模型
const categorys = ref([])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([])
const title=ref('')
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList();
}
import { articleCategoryListService, articleListService, articleAddService ,articleDeleteService} from '@/api/article.js'
//查询所有分类
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data.data.categories;
    categorys.value.unshift({
        id: '', // 设置为空值或你希望的默认值
        categoryName: '空标签',
        categoryAlias: '',
        createTime: '',
        updateTime: ''
    });
    //unshift方法是向开头添加一个默认项
}
onMounted(() => {
   
    articleCategoryList();
    articleList();
    console.log("这是articles的值",articles)
    console.log("这是用户信息",userInfo.info.id)
});
//获取文章列表数据
//查询分类数据
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params);
    // console.log("这是result的返回值:", result);
    //渲染视图
    articles.value = result.data.data.pageBean.items;
    total.value = result.data.data.pageBean.total;
    // console.log("这是total的值", total);
    // console.log("这是articles的值", articles);
    //处理数据,给数据模型扩展一个属性categoryName,分类名称
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i];
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId == categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName;
            }
        }
    }
}


import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})
import { useTokenStore } from '../../stores/token';
const tokenStore = useTokenStore()
import useUserInfoStore from '../../stores/userInfo'
const userInfo=useUserInfoStore()
//上传成功的回调函数
const uploadSuccess = (result) => {
    articleModel.value.coverImg = result.data.url;
    console.log("上传成功", articleModel.value.coverImg);
}
//添加文章
import { ElMessage } from 'element-plus'
const addArticle = async (clickState) => {
    //把发布状态赋值给数据模型
    articleModel.value.state = clickState;
    // console.log("提交的数据：", articleModel.value);
    //调用接口
    let result = await articleAddService(articleModel.value);

    ElMessage.success(result.msg ? result.msg : '添加成功');

    //让抽屉消失
    visibleDrawer.value = false;

    //刷新当前列表
    articleList()
}
const showDialog = (row) => {
    // 打开抽屉
    visibleDrawer.value = true;
    // 设置抽屉标题为“编辑文章”
    title.value = "编辑文章";
    
    // 将选中的文章数据回显到表单
    articleModel.value = {
        title: row.title,
        categoryId: row.categoryId,
        coverImg: row.coverImg,
        content: row.content,
        state: row.state
        
    };
    articleModel.value.id = row.id
};
import {ElMessageBox} from 'element-plus'
const deleteArticle = (row) => {
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
            let result = await articleDeleteService(row.id);
            console.log("这是删除后的result:"+result)
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //刷新列表
            articleList();
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
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true;title='添加文章'">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title">
            <template #default="{ row }">
                    <router-link :to="{ name: 'article-detail', params: { id: row.id } }">
                     {{ row.title }}
            </router-link>
            </template>
        </el-table-column>
            <el-table-column label="封面" width="200">
                <template #default="{ row }">
                    <img v-if="row.coverImg" :src="row.coverImg" alt="封面图" class="cover-img" />
                    <span v-else>暂无封面</span>
                </template>
            </el-table-column>
            <el-table-column label="分类" prop="categoryId"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }" >
                    <el-button :icon="Edit" circle plain type="primary" v-if="row.createUser === userInfo.info.id" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger"v-if="row.createUser === userInfo.info.id"  @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column>
            <!-- <el-table-column label="操作" width="100" v-if="row.createUser === userInfo.info.id">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column> -->
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
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">

                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                        action="http://localhost:8080/file/upload" name="file"
                        :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSuccess">
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="addArticle('草稿')">草稿</el-button>
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