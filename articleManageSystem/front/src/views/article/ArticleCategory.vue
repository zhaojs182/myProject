<script setup>
import { ref, onMounted, reactive } from 'vue';
import { Edit, Delete } from '@element-plus/icons-vue';
import { articleCategoryListService,articleCategoryAddService,articleCategoryUpdateService,articleCategoryDeleteService } from '@/api/article.js';
import { useTokenStore } from '../../stores/token';
import { ElMessage } from 'element-plus'
// 定义响应式数据
const category = reactive({
    categories: []
});

// 获取分类数据的方法
const articleCategoryList = async () => {
    let { data } = await articleCategoryListService(); // 调用服务获取数据
    // console.log("从后端返回的数据:", data);
    // 确保返回的数据结构正确
    category.categories = data.data.categories
    // console.log("这是更新后的数据:", category.categories); 
};
const tokenStore = useTokenStore();
// 页面加载时调用数据加载方法
onMounted(() => {
    // console.log("这是要发送的token:", tokenStore.token);
    articleCategoryList();
});
//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}

//访问后台，添加文章分类
const addCategory = async ()=>{
    let result = await articleCategoryAddService(categoryModel.value);
    ElMessage.success(result.message? result.message:'添加成功')
    //隐藏弹窗
    dialogVisible.value = false
    //再次访问后台接口，查询所有分类
    articleCategoryList();
}
const title=ref('')
//清空模型的数据
const clearData = () => {
    categoryModel.value.categoryName = '';
    categoryModel.value.categoryAlias = '';
}
const showDialog = (row) => {
    dialogVisible.value = true; title.value = '编辑分类'
    //数据拷贝
    categoryModel.value.categoryName = row.categoryName;
    categoryModel.value.categoryAlias = row.categoryAlias;
    //扩展id属性,将来需要传递给后台,完成分类的修改
    categoryModel.value.id = row.id
}
//修改分类
const updateCategory=async ()=>{
    let result = await articleCategoryUpdateService(categoryModel.value)
    ElMessage.success(result.message? result.message:'修改成功')
    //隐藏弹窗
    dialogVisible.value=false
    articleCategoryList();
}
//删除分类
import {ElMessageBox} from 'element-plus'
const deleteCategory = (row) => {
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
            let result = await articleCategoryDeleteService(row.id);
            console.log("这是删除后的result:"+result)
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //刷新列表
            articleCategoryList();
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
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true;title='添加分类'">添加分类</el-button>
                </div>
            </div>
        </template>

        <!-- 仅在数据加载完成并且有数据时才渲染表格 -->
        <el-table :data="category.categories" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <!-- <el-button :icon="Edit" circle plain type="primary" @click="dialogVisible=true,title='编辑分类'"></el-button>
                    <el-button :icon="Delete" circle plain type="danger"></el-button> -->
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <!-- 当数据为空时显示占位 -->
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <!-- <el-button type="primary" @click="addCategory"> 确认 </el-button> -->
                    <el-button type="primary" @click="title == '添加分类' ? addCategory() : updateCategory()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>




<!-- <script setup>
import { ref } from 'vue';
import { Edit, Delete } from '@element-plus/icons-vue';
import { articleCategoryListService } from '@/api/article.js';

// 定义响应式数据
const categorys = ref([]);

// 获取分类数据的方法
const articleCategoryList = async () => {
    try {
        const result = await articleCategoryListService(); // 调用服务获取数据
        console.log("从后端返回的数据:", result);
        if (result && result.data) {
            categorys.value = result.data.categories; 
            console.log("这是更新后的数据"+categorys.value);
        } else {
            console.error('分类数据为空或结构错误', result);
        }
    } catch (error) {
        console.error('获取分类数据失败', error);
    }
};
import { useTokenStore } from '../../stores/token';
const tokenStore = useTokenStore();
// 页面加载时调用数据加载方法
console.log("这是要发送的token:"+tokenStore.token)
articleCategoryList();

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary">添加分类</el-button>
                </div>
            </div>
        </template>
        
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary"></el-button>
                    <el-button :icon="Delete" circle plain type="danger"></el-button>
                </template>
            </el-table-column>
            
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>-->
