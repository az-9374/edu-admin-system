<script setup>
import { ref, watch, onMounted } from "vue";
import {
  queryPageApi,
  addEmpApi,
  queryEmpByIdApi,
  updateEmpApi,
  deleteByIdApi,
} from "../../api/emp";
import { queryAllApi as queryAllDeptApi } from "@/api/dept";
import { ElMessage, ElMessageBox } from "element-plus";

// 员工搜索部分
const searchEmp = ref({
  name: "",
  gender: "",
  begin: "",
  end: "",
  date: [],
});
// 监听searchEmp的date给begin/end赋值
watch(
  () => {
    return searchEmp.value.date;
  },
  (newVal, oldVal) => {
    if (newVal.length == 2) {
      searchEmp.value.begin = newVal[0];
      searchEmp.value.end = newVal[1];
    } else {
      searchEmp.value.begin = "";
      searchEmp.value.end = "";
    }
  }
);

const search = async () => {
  console.log("Search:", searchEmp.value);
  const result = await queryPageApi(
    searchEmp.value.name,
    searchEmp.value.gender,
    searchEmp.value.begin,
    searchEmp.value.end,
    currentPage.value,
    pageSize.value
  );

  if (result.code) {
    empList.value = result.data.rows;
    total.value = result.data.total;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};
const clear = () => {
  searchEmp.value = {
    name: "",
    date: [],
  };
  search();
};
//新增员工
const employeeFormRef = ref(null);
const dialogVisible = ref(false);
const dialogTitle = ref("");
const employee = ref({
  username: "",
  name: "",
  gender: "",
  phone: "",
  job: "",
  salary: "",
  deptId: "",
  entryDate: "",
  image: "",
  exprList: [],
});

//职位列表数据
const jobs = ref([
  { name: "班主任", value: 1 },
  { name: "讲师", value: 2 },
  { name: "学工主管", value: 3 },
  { name: "教研主管", value: 4 },
  { name: "咨询师", value: 5 },
  { name: "其他", value: 6 },
]);
//性别列表数据
const genders = ref([
  { name: "男", value: 1 },
  { name: "女", value: 2 },
]);
//部门列表数据
const deptList = ref([]);
const addEmp = () => {
  //打开dialog
  dialogVisible.value = true;
  dialogTitle.value = "新增员工";
  employee.value = {
    username: "",
    name: "",
    gender: "",
    phone: "",
    job: "",
    salary: "",
    deptId: "",
    entryDate: "",
    image: "",
    exprList: [],
  };
  if (employeeFormRef.value) {
    employeeFormRef.value.resetFields();
  }
};

//头像上传
const handleAvatarSuccess = (response, uploadFile) => {
  employee.value.image = URL.createObjectURL(uploadFile.raw);
};

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
    ElMessage.error("只支持上传图片");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error("只能上传10M以内图片");
    return false;
  }
  return true;
};
//工作经历
//动态添加工作经历
const addExprItem = () => {
  //往exprList[]里面增加一个，值全为空
  employee.value.exprList.push({
    exprDate: [],
    begin: "",
    end: "",
    job: "",
    company: "",
  });
};
//动态删除工作经历 .
const delExprItem = (index) => {
  //根据传过来的index删除对应的数组元素--splice(0, 1)
  employee.value.exprList.splice(index, 1);
};
//watchexprDate的变化给begin/end赋值
watch(
  () => {
    // return employee.value.exprList.exprDate;
    // //直接监听exprDate就不知道是哪个元素的这个发生的改变

    return employee.value.exprList;
  },
  (newVal, oldVal) => {
    if (employee.value.exprList && employee.value.exprList.length > 0) {
      //循环赋值
      employee.value.exprList.forEach((expr) => {
        expr.begin = expr.exprDate[0];
        expr.end = expr.exprDate[1];
      });
    }
  },
  { deep: true }
);
//保存/保存修改的dialog
const save = async () => {
  //校验表单
  employeeFormRef.value.validate(async (valid) => {
    if (valid) {
      //调用api参考接口文档
      let result;
      if (employee.value.id) {
        //修改
        result = await updateEmpApi(employee.value);
      } else {
        //新增
        result = await addEmpApi(employee.value);
      }

      if (result.code) {
        //提示信息并关闭窗口且刷新校验信息
        ElMessage.success("修改员工成功");
        dialogVisible.value = false;
        // resetForm();
        //重新加载数据
        search();
      } else {
        //弹出错误信息
        ElMessage.error(result.msg);
      }
    }
  });
};

//校验表单
// 验证规则
const rules = ref({
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "用户名长度应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: "姓名长度应在2到10个字符之间",
      trigger: "blur",
    },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^1[3,9]\d{9}$/g,
      message: "请输入有效的手机号",
      trigger: "blur",
    },
  ],
});
//列表
const empList = ref([]);
//编辑员工信息--复用添加的dialog
const edit = async (id) => {
  //通过id查询回显employee--api
  const result = await queryEmpByIdApi(id);
  //--补充employee-exprList的exprDate
  if (result.code) {
    //打开dialog
    dialogVisible.value = true;
    dialogTitle.value = "修改员工";
    employee.value = result.data;

    //不安全，无判断
    /*     employee.value.exprList.forEach((expr) => {
      expr.exprDate = [expr.begin, expr.end];
    }); */
    let exprList = employee.value.exprList; // ① 把数组的引用赋值给 exprList
    if (exprList && exprList.length > 0) {
      exprList.forEach((expr) => {
        expr.exprDate = [expr.begin, expr.end];
      });
    }
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }

  //保存编辑
};
//删除员工-单个/批量
const deleteByid = (id) => {
  ElMessageBox.confirm("您确定要删除该员工吗 ?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteByIdApi(id);
      if (result.code) {
        //提示信息并关闭窗口且刷新校验信息
        ElMessage.success("操作成功");
        //重新加载数据
        search();
      } else {
        //弹出错误信息
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消删除 ",
      });
    });
};
//记录勾选的员工id
const selectedIds = ref([]);
//多选框发生变化时触发--获取当前选中的记录
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map((item) => item.id);
};
const deleteByIds = () => {
  ElMessageBox.confirm("您确定要删除该员工吗 ?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      if (selectedIds.value && selectedIds.value.length > 0) {
        const result = await deleteByIdApi(selectedIds.value);
        if (result.code) {
          //提示信息并关闭窗口且刷新校验信息
          ElMessage.success("操作成功");
          //重新加载数据
          search();
        } else {
          //弹出错误信息
          ElMessage.error(result.msg);
        }
      } else {
        ElMessage.info("您没有选择任何要删除的数据");
      }
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消删除 ",
      });
    });
};

//分页
const currentPage = ref(1);
const pageSize = ref(10);
const background = ref(true); //背景色
const total = ref(0); //总记录数

const handleSizeChange = () => {
  search();
};
const handleCurrentChange = () => {
  search();
};

onMounted(async () => {
  search();
  //加载所有部门数据
  const result = await queryAllDeptApi();
  if (result.code) {
    deptList.value = result.data;
  }
  getToken();
});

const token = ref("");
//获取token
const getToken = () => {
  const loginUser = JSON.parse(localStorage.getItem("loginUser"));
  if (loginUser && loginUser.token) {
    token.value = loginUser.token;
  }
};
</script>

<template>
  <!-- 员工管理 -->
  <!-- 1.行内表单 -->
  <div class="container">
    <h1>员工管理</h1>
    <el-form :inline="true" :model="searchEmp">
      <el-form-item label="姓名">
        <el-input
          v-model="searchEmp.name"
          placeholder="请输入员工姓名"
          clearable
        />
      </el-form-item>
      <el-form-item label="性别 ">
        <el-select v-model="searchEmp.gender" placeholder="请选择 " clearable>
          <el-option label="男" value="1" />
          <el-option label="女" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="入职时间">
        <el-date-picker
          v-model="searchEmp.date"
          type="daterange"
          range-separator="到"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 2.新增按钮 -->
  <div class="container">
    <el-button type="primary" @click="addEmp">+ 新增员工</el-button>
    <el-button type="primary" @click="deleteByIds">- 批量删除</el-button>
  </div>
  <!-- 3.列表 -->
  <div class="container">
    <el-table
      :data="empList"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column label="头像" width="160" align="center">
        <template #default="scope">
          <img :src="scope.row.image" alt="Avatar" class="avatar" />
        </template>
      </el-table-column>
      <el-table-column
        prop="deptName"
        label="所属部门"
        width="180"
        align="center"
      />
      <el-table-column label="职位" width="100" align="center">
        <template #default="scope">
          <span v-if="scope.row.job == 1">班主任</span>
          <span v-else-if="scope.row.job == 2">讲师</span>
          <span v-else-if="scope.row.job == 3">学工主管</span>
          <span v-else-if="scope.row.job == 4">教研主管</span>
          <span v-else-if="scope.row.job == 5">咨询师</span>
          <span v-else>其他</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="entryDate"
        label="入职日期"
        width="220"
        align="center"
      />
      <el-table-column
        prop="updateTime"
        label="最后操作时间"
        width="220"
        align="center"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" @click="edit(scope.row.id)"> 编辑 </el-button>
          <el-button
            size="small"
            type="danger"
            @click="deleteByid(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- 4.分页 -->
  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 50, 75, 100]"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <!-- 新增修改的dialog -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      :model="employee"
      ref="employeeFormRef"
      label-width="80px"
      :rules="rules"
    >
      <!-- 基本信息 -->
      <!-- {{ employee }} -->
      <!-- 第一行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="employee.username"
              placeholder="请输入员工用户名，2-20个字"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="employee.name"
              placeholder="请输入员工姓名，2-10个字"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第二行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select
              v-model="employee.gender"
              placeholder="请选择性别"
              style="width: 100%"
            >
              <el-option
                v-for="gender in genders"
                :key="gender.name"
                :label="gender.name"
                :value="gender.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="employee.phone"
              placeholder="请输入员工手机号"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第三行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="职位">
            <el-select
              v-model="employee.job"
              placeholder="请选择职位"
              style="width: 100%"
            >
              <el-option
                v-for="job in jobs"
                :key="job.name"
                :label="job.name"
                :value="job.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="薪资">
            <el-input
              v-model="employee.salary"
              placeholder="请输入员工薪资"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第四行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属部门">
            <el-select
              v-model="employee.deptId"
              placeholder="请选择部门"
              style="width: 100%"
            >
              <el-option
                v-for="dept in deptList"
                :key="dept.id"
                :label="dept.name"
                :value="dept.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker
              v-model="employee.entryDate"
              type="date"
              style="width: 100%"
              placeholder="选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第五行 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="头像">
            <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :headers="{ token: token }"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="employee.image" :src="employee.image" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 工作经历 -->
      <!-- 第六行 -->
      <el-row :gutter="10">
        <el-col :span="24">
          <el-form-item label="工作经历">
            <el-button type="success" size="small" @click="addExprItem"
              >+ 添加工作经历</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第七行 ...  工作经历 -->
      <el-row :gutter="3" v-for="(expr, index) in employee.exprList">
        <el-col :span="10">
          <el-form-item size="small" label="时间" label-width="80px">
            <el-date-picker
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              v-model="expr.exprDate"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item size="small" label="公司" label-width="60px">
            <el-input
              placeholder="请输入公司名称"
              v-model="expr.company"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item size="small" label="职位" label-width="60px">
            <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="2">
          <el-form-item size="small" label-width="0px">
            <el-button type="danger" @click="delExprItem(index)"
              >- 删除</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 10px 0;
}
.avatar {
  height: 40px;
}
.avatar-uploader .avatar {
  width: 78px;
  height: 78px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  /* 添加灰色的虚线边框 */
  border: 1px dashed var(--el-border-color);
}
</style>
