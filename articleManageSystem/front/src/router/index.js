
import {createRouter,createWebHashHistory} from 'vue-router'
//导入组件
import LoginVue from '@/views/Login.vue'
import LayoutVue from '@/views/Layout.vue'
import ArticleCategoryVue from '@/views/article/ArticleCategory.vue'
import ArticleManageVue from '@/views/article/ArticleManage.vue'
import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'
import ArticleDetailInfoVue from '@/views/article/ArticleDetailInfo.vue'
import ChartVue from '@/views/others/Chart.vue'
import OpenAIDialogue from '../views/others/OpenAIDialogue.vue'
import TagCategory from '../views/article/TagCategory.vue'
import TagManage from '../views/article/TagManage.vue'
import Controller from '@/views/admin/controller.vue'

//定义路由关系
// const routes = [
//     { path: '/login', component: LoginVue },
//     { path: '/', component: LayoutVue }
// ]
const router= createRouter({
    history:createWebHashHistory(),
    routes:[
        {
            path:"/",
            redirect:"/login"
        },
        {
            path:"/layout",
            component:LayoutVue,redirect:'/article/manage',
            children:[
                { path:'/article/tagmanage',component:TagManage},
                { path:'/article/tagcategory',component:TagCategory},
                { path: '/article/category', component: ArticleCategoryVue },
                { path: '/article/manage', component: ArticleManageVue },
                {path: '/article/detail/:id', 
                 name: 'article-detail',
                 component: ArticleDetailInfoVue,
                  },
                { path: '/user/info', component: UserInfoVue },
                { path: '/user/avatar', component: UserAvatarVue },
                { path: '/user/resetPassword', component: UserResetPasswordVue },
                {path:'/others/chart',component:ChartVue},
                {path:'/others/openaidialogue',component:OpenAIDialogue},
                {path:'/admin/controller',component:Controller}
            ]
        },
        {
            path:"/login",
            component:LoginVue
        }
    ]
})
export default router
// const router= createRouter({
//     history:createWebHashHistory(),
//     routes:[
//         {
//             path:"/",
//             redirect:"/showSchedule"
//         },
//         {
//             path:"/showSchedule",
//             component:ShowSchedule
//         },
//         {
//             path:"/login",
//             component:Login
//         },
//         {
//             path:"/regist",
//             component:Regist
//         }
//     ]
// })
// export default router
//创建路由器
// const router = createRouter({
//     history: createWebHistory(),
//     routes: routes
// });

