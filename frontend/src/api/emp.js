import request from "../utils/request.js";
//分页列表查询
// /emps?name=张&gender=1&begin=2007-09-01&end=2022-09-01&page=1&pageSize=10
export const queryPageApi = (name, gender, begin, end, page, pageSize) =>
  request.get(
    `/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`
  );
//新增员工
export const addEmpApi = (emp) => request.post("/emps", emp);

//修改员工
//1.通过id找到员工
export const queryEmpByIdApi = (id) => request.get(`/emps/${id}`);
//2.保存修改
export const updateEmpApi = (employee) => request.put("/emps", employee);
//删除员工--单个/批量
export const deleteByIdApi = (id) => request.delete(`/emps?ids=${id}`);

//查询所有员工
export const queryAllApi = () => request.get("/emps/list");
