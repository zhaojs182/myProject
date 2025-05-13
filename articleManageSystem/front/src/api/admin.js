import request from '@/utils/request'; // 确保路径正确
import { useTokenStore } from '../stores/token';




export const userListService = (params)=>{
    return  request.get('/admin/getUsersInfo',{params:params})
}
export const userDeleteService = (id) => {
    return request.get('/admin/deleteInfo?id='+id)
}
export const resetPasswordService=(id)=>{
    // console.log("这回事要改密码的user的id",id)
    return request.post('/admin/resetpassword',{id})
}
export const DisableOrOpenUsers=(userlist)=>{
    return request.post('/admin/DisableOrOpenUsers',{userlist})

}


