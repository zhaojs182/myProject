// import request from '@/utils/request.js'
// import { useTokenStore } from '../stores/token';
// export const articleCategoryListService = ()=>{
//     const tokenStore = useTokenStore();
//     //在pinia中定义的响应式数据,都不需要.value
//     return request.get('/category/getInfo',{headers:{'Authorization':tokenStore.token}});
//     // return request.get('/api/category/getInfo', { headers: { 'Authorization': tokenStore.token } });

//     // return request.get('/category')
// }



// import request from '@/utils/request'; // 确保路径正确
// import { useTokenStore } from '../stores/token';

// export const articleCategoryListService = () => {
//     const tokenStore = useTokenStore();
//     // return request.get('http://localhost:8080/category/getInfo', {  // 添加 '/api'
//     //     headers: {
//     //         Authorization: tokenStore.token,
//     //     },
//     // });
//     // let {data} = await request.get("schedule/findAllSchedule",{params:{"uid":sysUser.uid}})
//     return request.get('/category/getInfo', { params:{"token":tokenStore.token}})
// };
import request from '@/utils/request'; // 确保路径正确
import { useTokenStore } from '../stores/token';

//查询所有分类
export const articleCategoryListService = () => {
    // const tokenStore = useTokenStore();
    // 将 token 作为 URL 参数传递
    // return request.get('/category/getInfo', { params:{"token":tokenStore.token}})
    
    return request.get('/category/getInfo')
};
//添加文章分类
export const articleCategoryAddService = (categoryModel) => {
    return request.post('/category/addInfo', categoryModel)
}
//修改分类
export const articleCategoryUpdateService = (categoryModel)=>{
    return request.put('/category/updateInfo',categoryModel)
}
//删除分类
export const articleCategoryDeleteService = (id) => {
    return request.get('/category/deleteInfo?id='+id)
}
//文章列表查询
export const articleListService = (params)=>{
    return  request.get('/article/getInfoList',{params:params})
}
//文章添加及修改
export const articleAddService = (articleData)=>{
    return request.post('/article/addArticle',articleData);

}
export const articleDeleteService = (id) => {
    return request.get('/article/deleteInfo?id='+id)
}
export const articleDetailService = (params)=>{
    return  request.get('/article/getDetailInfo',{params:params})
}