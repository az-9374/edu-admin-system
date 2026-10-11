import request from "../utils/request.js";
//分页列表查询
// /clazzs?name=java&begin=2023-01-01&end=2023-06-30&page=1&pageSize=5
export const queryPageApi = (name, begin, end, page, pageSize) =>
  request.get(
    `/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`
  );

//新增班级
export const addClazzApi = (clazz) => request.post("/clazzs", clazz);
//修改
//1.通过id找到
export const queryClazzByIdApi = (id) => request.get(`/clazzs/${id}`);
//2.保存修改
export const updateClazzApi = (clazz) => request.put("/clazzs", clazz);
//删除
export const deleteByIdApi = (id) => request.delete(`/clazzs/${id}`);
//查询所有班级
export const queryAllApi = () => request.get("/clazzs/list");
