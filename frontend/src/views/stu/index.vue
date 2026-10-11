<script setup>
import { ref, onMounted } from "vue";
import {
  addStudentApi,
  deleteByIdApi,
  queryPageApi,
  queryStudentByIdApi,
  updateStudentApi,
  violationStuApi,
} from "../../api/student";
import { queryAllApi as queryAllClazzApi } from "../../api/clazz";
import { ElMessage, ElMessageBox } from "element-plus";

// 搜索表单
const searchStudent = ref({ name: "", degree: "", clazzId: "" });

const search = async () => {
  console.log("Search:", searchStudent.value);
  const result = await queryPageApi(
    searchStudent.value.name,
    searchStudent.value.degree,
    searchStudent.value.clazzId,
    currentPage.value,
    pageSize.value
  );

  if (result.code) {
    studentList.value = result.data.rows;
    total.value = result.data.total;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};
const clear = () => {
  searchStudent.value = { name: "", degree: "", clazzId: "" };
  // watch 会检测到 date 变化，自动清空 begin/end
  search();
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

//列表
const studentList = ref([]);

//dialog
const dialogVisible = ref(false);
const dialogTitle = ref("");
const dialogVisible2 = ref(false);
const dialogTitle2 = ref("");

//表格数据
const clazzList = ref([]); //查询所有
//1: 初中, 2: 高中 , 3: 大专 , 4: 本科 , 5: 硕士 , 6: 博士
const degreeList = ref([
  { name: "初中", value: 1 },
  { name: "高中", value: 2 },
  { name: "大专", value: 3 },
  { name: "本科", value: 4 },
  { name: "硕士", value: 5 },
  { name: "博士", value: 6 },
]);
const genderList = ref([
  { name: "男", value: 1 },
  { name: "女", value: 2 },
]);
// 1: 是, 0: 否
const isCollegeList = ref([
  { name: "是", value: 1 },
  { name: "否", value: 0 },
]);

//新增学员
const student = ref({
  name: "",
  no: "",
  gender: "",
  idCard: "",
  isCollege: "",
  address: "",
  degree: "",
  graduationDate: "",
  clazzId: "",
});
const studentFormRef = ref(null);
const addStudent = () => {
  dialogVisible.value = true;
  dialogTitle.value = "添加学员";

  student.value = {
    name: "",
    no: "",
    gender: "",
    idCard: "",
    isCollege: "",
    address: "",
    degree: "",
    graduationDate: "",
    clazzId: "",
  };
  if (studentFormRef.value) {
    studentFormRef.value.resetFields();
  }
};
//校验
const rules = ref({
  name: [
    { required: true, message: "请输入学员姓名", trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: "学员姓名长度应在2到10个字符之间",
      trigger: "blur",
    },
  ],
  no: [
    { required: true, message: "请输入学号", trigger: "blur" },
    {
      pattern: /^[A-Za-z0-9]{10}$/,
      message: "学号必须为10位数字或字母组合",
      trigger: "blur",
    },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "blur" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    {
      pattern: /^[0-9]{11}$/,
      message: "手机号必须为11位数字组合",
      trigger: "blur",
    },
  ],
  idCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    {
      pattern: /^[A-Za-z0-9]{18}$/,
      message: "身份证号必须为18位数字或字母组合",
      trigger: "blur",
    },
  ],
  isCollege: [{ required: true, message: "请选择", trigger: "blur" }],
  clazzId: [{ required: true, message: "请选择所属班级", trigger: "blur" }],
});
//编辑--复用添加的dialog
const edit = async (id) => {
  //通过id查询回显
  const result = await queryStudentByIdApi(id);
  if (result.code) {
    //打开dialog
    dialogVisible.value = true;
    dialogTitle.value = "修改学员";
    student.value = result.data;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};
//保存/保存修改的dialog
const save = async () => {
  //校验表单
  studentFormRef.value.validate(async (valid) => {
    if (valid) {
      //调用api参考接口文档
      let result;
      if (student.value.id) {
        //修改
        result = await updateStudentApi(student.value);
      } else {
        //新增
        result = await addStudentApi(student.value);
      }
      console.log("===== result =====", result); // ← 加这行
      console.log("result.code:", result.code); // ← 加这行
      if (result.code) {
        //提示信息并关闭窗口且刷新校验信息
        ElMessage.success("修改成功");
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

//违纪处理
const violationScoreDialog = ref({ studentId: null, score: 0 });
const vioScoreDialogFormRef = ref(null);
//校验
const rules2 = ref({
  score: [
    {
      pattern: /^[1-9]\d*$/,
      message: "请输入正整数",
      trigger: "blur",
    },
  ],
});
const violate = (studentId) => {
  dialogVisible2.value = true;
  dialogTitle2.value = "学员违纪处理";
  violationScoreDialog.value.studentId = studentId;
  violationScoreDialog.value.score = 0;
  if (vioScoreDialogFormRef.value) {
    vioScoreDialogFormRef.value.resetFields();
  }
};
//保存dialog
const save2 = async () => {
  //校验表单
  vioScoreDialogFormRef.value.validate(async (valid) => {
    if (valid) {
      const result = await violationStuApi(
        violationScoreDialog.value.studentId,
        violationScoreDialog.value.score
      );

      if (result.code) {
        //提示信息并关闭窗口且刷新校验信息
        ElMessage.success("扣分成功");
        dialogVisible2.value = false;
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
//删除-单个
const deleteByid = (id) => {
  ElMessageBox.confirm("您确定要删除该学员吗 ?", "提示", {
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
//删除-批量
//记录勾选的学员id
const selectedIds = ref([]);
//多选框发生变化时触发--获取当前选中的记录
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map((item) => item.id);
};
const deleteByIds = () => {
  ElMessageBox.confirm("您确定要删除该学员吗 ?", "提示", {
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

onMounted(async () => {
  search();
  //queryAllEmpApi
  const result = await queryAllClazzApi();
  if (result.code) {
    clazzList.value = result.data;
  }
});
</script>

<template>
  <!-- 学员管理 -->
  <!-- 1.行内表单 -->
  <div class="container">
    <h1>学员管理</h1>
    <!-- {{ searchStudent }} -->
    <el-form :inline="true" :model="searchStudent">
      <el-form-item label="姓名">
        <el-input
          v-model="searchStudent.name"
          placeholder="请输入学员名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="最高学历" prop="degree">
        <el-select
          v-model="searchStudent.degree"
          placeholder="请选择"
          style="width: 100%"
        >
          <el-option
            v-for="degree in degreeList"
            :key="degree.value"
            :label="degree.name"
            :value="degree.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属班级" prop="clazzId">
        <el-select
          v-model="searchStudent.clazzId"
          placeholder="请选择"
          style="width: 100%"
        >
          <el-option
            v-for="clazzId in clazzList"
            :key="clazzId.id"
            :label="clazzId.name"
            :value="clazzId.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 2.新增按钮 -->
  <div class="container">
    <el-button type="primary" @click="addStudent">+ 添加学员</el-button>
    <el-button type="primary" @click="deleteByIds">- 批量删除</el-button>
  </div>
  <!-- 3.列表 -->
  <div class="container">
    <!-- {{ selectedIds }} -->
    <el-table
      :data="studentList"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="no" label="学号" width="120" align="center" />
      <el-table-column
        prop="clazzName"
        label="班级"
        width="150"
        align="center"
      />
      <el-table-column prop="gender" label="性别" width="60" align="center">
        <template #default="scope">
          <span v-if="scope.row.gender == 1">男</span>
          <span v-else-if="scope.row.gender == 2">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="120" align="center" />
      <el-table-column prop="degree" label="最高学历" width="90" align="center">
        <template #default="scope">
          <span v-if="scope.row.degree == 1">初中</span>
          <span v-else-if="scope.row.degree == 2">高中</span>
          <span v-else-if="scope.row.degree == 3">大专</span>
          <span v-else-if="scope.row.degree == 4">本科</span>
          <span v-else-if="scope.row.degree == 5">硕士</span>
          <span v-else-if="scope.row.degree == 6">博士</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="violationCount"
        label="违纪次数"
        width="90"
        align="center"
      />
      <el-table-column
        prop="violationScore"
        label="违纪扣分"
        width="90"
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
          <el-button size="small" @click="violate(scope.row.id)">
            违纪
          </el-button>
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
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
    <el-form
      :model="student"
      ref="studentFormRef"
      label-width="120px"
      :rules="rules"
    >
      <!-- 基本信息 -->
      <!-- {{ student }} -->
      <!-- 班级名称 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="student.name"
              placeholder="请输入姓名"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" prop="no">
            <el-input v-model="student.no" placeholder="请输入学号"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 班级教室 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select
              v-model="student.gender"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="gender in genderList"
                :key="gender.value"
                :label="gender.name"
                :value="gender.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="student.phone"
              placeholder="请输入手机号"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 开课时间 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input
              v-model="student.idCard"
              placeholder="请输入身份证号"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否院校学员" prop="isCollege">
            <el-select
              v-model="student.isCollege"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="isCollege in isCollegeList"
                :key="isCollege.value"
                :label="isCollege.name"
                :value="isCollege.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 结课时间 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="联系地址" prop="address">
            <el-input
              v-model="student.address"
              placeholder="请输入联系地址"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历" prop="degree">
            <el-select
              v-model="student.degree"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="degree in degreeList"
                :key="degree.value"
                :label="degree.name"
                :value="degree.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 毕业时间 ,所属班级-->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="毕业时间" prop="graduationDate">
            <el-date-picker
              v-model="student.graduationDate"
              type="date"
              style="width: 100%"
              placeholder="请选择毕业时间"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属班级" prop="clazzId">
            <el-select
              v-model="student.clazzId"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="clazzId in clazzList"
                :key="clazzId.id"
                :label="clazzId.name"
                :value="clazzId.id"
              ></el-option>
            </el-select>
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
  <!-- 违纪处理的dialog -->
  <el-dialog v-model="dialogVisible2" :title="dialogTitle2" width="800px">
    <el-form
      :model="violationScoreDialog"
      ref="vioScoreDialogFormRef"
      label-width="120px"
      :rules="rules2"
    >
      <!-- 基本信息 -->
      <!-- {{ violationScoreDialog }} -->
      <!-- 违纪处理 -->
      <el-form-item label="违纪扣分" prop="score">
        <el-input v-model="violationScoreDialog.score"></el-input>
      </el-form-item>
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取消</el-button>
        <el-button type="primary" @click="save2">保存</el-button>
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
