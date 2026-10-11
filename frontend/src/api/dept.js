//把与服务端进行异步交互的逻辑封装在一个单独的API中
//对axios.get(xxx)的优化（部分2）

import request from "../utils/request.js";
//列表查询
export const queryAllApi = () => request.get("/depts");

//新增部门
export const addDeptApi = (dept) => request.post("/depts", dept);

//修改部门
//1.通过id找到部门
export const searchDeptByIdApi = (id) => request.get(`/depts/${id}`);
//2.保存修改
export const updateDeptApi = (dept) => request.put("/depts", dept);
//删除部门
export const deleteDeptApi = (id) => request.delete(`/depts?id=${id}`);
