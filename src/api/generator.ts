import request from '@/utils/request'

enum API {
  // 获取权限信息数据的接口
  GENERATE_URL = '/generate',
}

// 请求代码生成的方法
export const reqCodeGeneration = (info:any) =>{
    return request.post<any, any>(API.GENERATE_URL,info)
}