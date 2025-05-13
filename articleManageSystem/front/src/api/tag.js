import request from '@/utils/request'; // 确保路径正确
import { useTokenStore } from '../stores/token';

//查询所有分类
export const tagCategoryListService = (tagsearchtest) => {
    console.log("前段tag分类的传参", tagsearchtest);
    return request.get('/tagCategory/getInfo', {
        params: {
            tagsearchtest: tagsearchtest 
        }
    });
};

//添加文章分类
export const tagCategoryAddService = (categoryModel) => {
    return request.post('/tagCategory/addInfo', categoryModel)
}
//修改分类
export const tagCategoryUpdateService = (categoryModel)=>{
    return request.put('/tagCategory/updateInfo',categoryModel)
}
//删除分类
export const tagCategoryDeleteService = (id) => {
    return request.get('/tagCategory/deleteInfo?id='+id)
}






//tag列表查询
export const tagListService = (params)=>{
    console.log("这是tag列表查询的值",params)
    return  request.get('/tag/getInfoList',{params:params})
}
//文章tag添加及修改
export const tagAddService = (articleData)=>{
    // console.log("这是我发送的要添加的tag数据",articleData)
    return request.post('/tag/addInfo',articleData);

}
export const tagDeleteService = (id) => {
    return request.get('/tag/deleteInfo?id='+id)
}
//查询这个用户的全部tag
export const getAlltagsService =()=>{
   let result=request.post('/tag/getInfo')
 
   return result
}
//文章详情的tag查询
export const getArticleTagService=(articleId)=>{
    console.log("这是articleid",articleId)
    return request.post('/tag/getArticleTag', null, {
        params: { articleId }  // 这里将 articleId 放在 params 中
      });
}
export const addtagsService=(tag)=>{
return request.post('/tag/addTag',{tag})
}



