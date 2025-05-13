import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from '@/router'
import { createPinia } from 'pinia'
import {createPersistedState} from'pinia-persistedstate-plugin'
import locale from 'element-plus/dist/locale/zh-cn.js'
import * as echarts from 'echarts'




const app=createApp(App);
app.config.globalProperties.$echarts = echarts
const pinia=createPinia();
const persist = createPersistedState();
pinia.use(persist)
app.use(router)
// app.use(ElementPlus);
app.use(ElementPlus,{locale});
app.use(pinia)
app.mount('#app')


