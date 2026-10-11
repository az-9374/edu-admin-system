//一个请求处理的工具类
// 在这个工具类中，对axios进行了封装
import axios from "axios";
import { ElMessage } from "element-plus";
import router from "../router";
//创建axios实例对象---对axios.get(xxx)的优化（部分1）
const request = axios.create({
  baseURL: "/api",
  timeout: 600000,
});

//加上token后请求
/* /axios的请求 request 拦截器, 每次请求获取localStorage中的loginUser,
从中获取到token, 在请求头token中携带到服务端 */
request.interceptors.request.use(
  (config) => {
    let loginUser = JSON.parse(localStorage.getItem("loginUser"));
    if (loginUser) {
      config.headers.token = loginUser.token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

//对于未登录访问行为--跳转到首页--拦截响应查看是否为401
//axios的响应 response 拦截器---对result.data.data的优化
request.interceptors.response.use(
  (response) => {
    //成功回调
    return response.data;
  },
  (error) => {
    //失败回调
    if (error.response.status === 401) {
      ElMessage.error("登录失效, 请重新登录");
      router.push("/login");
    } else {
      ElMessage.success("接口访问异常");
    }
    return Promise.reject(error);
  }
);

//使其他地方可以调用
export default request;

/* const search = async () => {
  const result = await axios.get(
    "https://m1.apifoxmock.com/m1/8571182-8348103-default/depts?apifoxApiId=495060192"
  );
  if (result.data.code == 1) {
    deptList.value = result.data.data;
  } else {
    //弹出错误信息
    ElMessage.error(result.data.msg);
  }
}; */
