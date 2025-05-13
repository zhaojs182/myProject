<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { articleDetailService } from '@/api/article.js';
import { getAlltagsService, getArticleTagService,addtagsService} from '@/api/tag.js';

const route = useRoute();
const article = ref([]);
const tags = ref([]);
const articletags = ref([]);
const addTagModel = ref({
  aid: '',
  tid: ''
});
//添加文章tag
const addtags = async () => {
  console.log("这是要发送的模型",addTagModel.value)
  const result=await addtagsService(addTagModel.value);
  closeDialog()
  getArticleTag()

};
// 获取文章详情
const getArticleDetail = async () => {
  const result = await articleDetailService({ id: route.params.id });
  addTagModel.value.aid=result.data.data.article.id
  // console.log("文章具体详情返回值", addTagModel.value.aid);
  article.value = result.data.data.article;
  // console.log("article的值", article.value);

  // 在 article 更新后调用获取文章标签
  getArticleTag();
};

// 获取所有标签
const getAlltags = async () => {
  const result = await getAlltagsService();
  // console.log("这是拿到的tag的result", result);
  tags.value = result.data.data.tags;
  // console.log("这是拿到的用户的全部tag", tags.value);
};

// 获取文章标签
const getArticleTag = async () => {
  // 确保 article.value.id 存在
  if (article.value && article.value.id) {
    const result = await getArticleTagService(article.value.id);
    // console.log("这是获取到的文章的tag", result);
    articletags.value = result.data.data.tagList; // 假设返回的数据格式正确
    // console.log("这是最后的articletags", articletags.value);
  } else {
    console.error("文章 ID 未找到");
  }
};

// 在组件挂载后调用 getArticleDetail 获取文章详情
onMounted(() => {
  getArticleDetail();
  getAlltags();  // 调用获取所有标签
});

// 用来控制弹窗的显隐
const dialogVisible = ref(false);

// 控制弹窗显示
const openDialog = () => {
  dialogVisible.value = true;
};

// 关闭弹窗
const closeDialog = () => {
  dialogVisible.value = false;
};

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章详情</span>
        <div class="extra">
          <el-button @click="$router.go(-1)" type="primary">返回</el-button>
          <el-button type="primary" @click="openDialog">添加tag</el-button>
        </div>
      </div>
    </template>

    <div v-if="article">
      <el-row>
        <el-col :span="24">
          <h3>{{ article.title }}</h3>
          <p>分类: {{ article.categoryName }}</p>
          <p>状态: {{ article.state }}</p>
          <p>发布时间: {{ article.createTime }}</p>
          <div v-if="article.coverImg">
            <img :src="article.coverImg" alt="封面图" class="cover-img" />
          </div>
          <div v-html="article.content"></div>
        </el-col>
      </el-row>
    </div>
    
    <div v-if="articletags">
      <el-col :span="24">
        <p>标签：</p>
        <div>
          <el-tag v-for="(tag, index) in articletags" :key="index" :style="{ margin: '5px' }">
            {{ tag.tname }} <!-- Correctly displaying the tag name -->
          </el-tag>
        </div>
      </el-col>
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加tag" width="30%">
      <el-form :model="addTagModel" label-width="100px" style="padding-right: 30px">
        <el-form-item label="标签" prop="tid">
          <el-select v-model="addTagModel.tid" placeholder="请选择标签">
            <el-option
              v-for="(tag, index) in tags"
              :key="tag.id"
              :label="tag.tname"
              :value="tag.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="addtags()">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.cover-img {
  width: 100%;
  height: 130px;
  object-fit: cover;
  border-radius: 8px;
}
</style>
