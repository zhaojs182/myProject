import request from '@/utils/request'; // 确保路径正确
import { useTokenStore } from '../stores/token';


export const categoryFrequency=()=>{
    return request.post('/chart/categoryFrequency');
}