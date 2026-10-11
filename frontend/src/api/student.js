import request from "../utils/request.js";
//分页列表查询
// /students?name=张三&degree=1&clazzId=2&page=1&pageSize=5
export const queryPageApi = (name, degree, clazzId, page, pageSize) =>
  request.get(
    `/students?name=${name}&degree=${degree}&clazzId=${clazzId}&page=${page}&pageSize=${pageSize}`
  );

//新增班级
export const addStudentApi = (student) => request.post("/students", student);
//修改
//1.通过id找到
export const queryStudentByIdApi = (id) => request.get(`/students/${id}`);
//2.保存修改
export const updateStudentApi = (student) => request.put("/students", student);
//删除
export const deleteByIdApi = (id) => request.delete(`/students/${id}`);

//违纪处理--/students/violation/{id}/{score}
export const violationStuApi = (id, score) =>
  request.put(`/students/violation/${id}/${score}`);
