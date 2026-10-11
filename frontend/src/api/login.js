//  登录
//1.定义登录请求的api
import request from "../utils/request.js";

export const loginApi = (loginForm) => request.post("/login", loginForm);
