// 消息提示组件
import { ElMessage } from 'element-plus'

// 进行axios的二次封装：使用请求与响应拦截器
import axios from 'axios'


// 第一步，利用axios的create方法，创建axios实例（这样方便去进行一些配置：基础路径、超时时间）
// 这里的request就是一个axios对象，不过进行了一些配置
let request = axios.create({
    baseURL:'http://localhost:8080',
    headers: {
      'Content-Type': 'application/json',  // 确保请求体是 JSON 格式
    },
})

// 第二步，request实例添加请求与响应拦截器
request.interceptors.request.use((config) => {
  // 可以在这里配置请求头
  // 返回配置对象
  return config
})

// 第三步：配置响应拦截器
request.interceptors.response.use(
  (response) => {
    // 成功的回调
    // 简化数据
    // return response.data
    return response
  },
  (error) => {
    // 失败的回调，处理http网络错误
    let message = '出现问题了'
    // 提示错误信息
    ElMessage({
      type: 'error',
      message,
    })
    // 返回一个失败的Promise对象去终止Promise链
    return Promise.reject(error)
  },
)

// 将配置好的对象对外暴露
export default request
