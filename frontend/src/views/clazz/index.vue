<script setup>
import { ref, onMounted, watch } from "vue";
import {
  addClazzApi,
  deleteByIdApi,
  queryClazzByIdApi,
  queryPageApi,
  updateClazzApi,
} from "../../api/clazz";
import { queryAllApi as queryAllEmpApi } from "../../api/emp";
import { ElMessage, ElMessageBox } from "element-plus";

// 搜索表单
const searchClazz = ref({ name: "", begin: "", end: "", date: [] });

// 监听date给begin/end赋值
watch(
  () => {
    return searchClazz.value.date;
  },
  (newVal, oldVal) => {
    if (newVal.length == 2) {
      searchClazz.value.begin = newVal[0];
      searchClazz.value.end = newVal[1];
    } else {
      searchClazz.value.begin = "";
      searchClazz.value.end = "";
    }
  }
);

const search = async () => {
  console.log("Search:", searchClazz.value);
  const result = await queryPageApi(
    searchClazz.value.name,
    searchClazz.value.begin,
    searchClazz.value.end,
    currentPage.value,
    pageSize.value
  );

  if (result.code) {
    clazzList.value = result.data.rows;
    total.value = result.data.total;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};
const clear = () => {
  searchClazz.value.name = "";
  searchClazz.value.date = [];
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
const clazzList = ref([]);

//dialog
const dialogVisible = ref(false);
const dialogTitle = ref("");
const empList = ref([]); //查询所有员工
const subList = ref([
  { name: "java", value: 1 },
  { name: "前端", value: 2 },
  { name: "大数据", value: 3 },
  { name: "Python", value: 4 },
  { name: "Go", value: 5 },
  { name: "嵌入式", value: 6 },
]);

//新增班级
const clazz = ref({
  name: "",
  room: "",
  beginDate: "",
  endDate: "",
  masterId: "",
  subject: "",
});
const clazzFormRef = ref(null);
const addClazz = () => {
  dialogVisible.value = true;
  dialogTitle.value = "新增班级";

  clazz.value = {
    name: "",
    room: "",
    beginDate: "",
    endDate: "",
    masterId: "",
    subject: "",
  };
  if (clazzFormRef.value) {
    clazzFormRef.value.resetFields();
  }
};

//校验表单
// 验证规则
const rules = ref({
  name: [
    { required: true, message: "请输入班级名称", trigger: "blur" },
    {
      min: 4,
      max: 30,
      message: "班级名称长度应在4到30个字符之间",
      trigger: "blur",
    },
  ],
  beginDate: [{ required: true, message: "请输入开课时间", trigger: "blur" }],
  endDate: [{ required: true, message: "请输入结课时间", trigger: "blur" }],
  subject: [{ required: true, message: "请选择学科", trigger: "blur" }],
});

//编辑--复用添加的dialog
const edit = async (id) => {
  //通过id查询回显clazz
  const result = await queryClazzByIdApi(id);
  if (result.code) {
    //打开dialog
    dialogVisible.value = true;
    dialogTitle.value = "修改班级";
    clazz.value = result.data;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};

//保存/保存修改的dialog
const save = async () => {
  //校验表单
  clazzFormRef.value.validate(async (valid) => {
    if (valid) {
      //调用api参考接口文档
      let result;
      if (clazz.value.id) {
        //修改
        result = await updateClazzApi(clazz.value);
      } else {
        //新增
        result = await addClazzApi(clazz.value);
      }
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

//删除-单个
const deleteByid = (id) => {
  ElMessageBox.confirm("您确定要删除该班级吗 ?", "提示", {
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

onMounted(async () => {
  search();
  //queryAllEmpApi
  const result = await queryAllEmpApi();
  if (result.code) {
    empList.value = result.data;
  }
});
</script>

<template>
  <!-- 班级管理 -->
  <!-- 1.行内表单 -->
  <div class="container">
    <h1>班级管理</h1>
    <!-- {{ searchClazz }} -->
    <el-form :inline="true" :model="searchClazz">
      <el-form-item label="姓名">
        <el-input
          v-model="searchClazz.name"
          placeholder="请输入班级名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker
          v-model="searchClazz.date"
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
    <el-button type="primary" @click="addClazz">+ 新增班级</el-button>
  </div>
  <!-- 3.列表 -->
  <div class="container">
    <el-table :data="clazzList" border style="width: 100%">
      <el-table-column prop="id" label="序号" width="100" align="center" />
      <el-table-column
        prop="name"
        label="班级名称"
        width="180"
        align="center"
      />
      <el-table-column
        prop="room"
        label="班级教室"
        width="120"
        align="center"
      />
      <el-table-column
        prop="masterName"
        label="班主任"
        width="120"
        align="center"
      />
      <el-table-column
        prop="beginDate"
        label="开课时间"
        width="150"
        align="center"
      />
      <el-table-column
        prop="endDate"
        label="结课时间"
        width="150"
        align="center"
      />
      <el-table-column prop="status" label="状态" width="120" align="center" />
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
      :model="clazz"
      ref="clazzFormRef"
      label-width="80px"
      :rules="rules"
    >
      <!-- 基本信息 -->
      <!-- {{ clazz }} -->
      <!-- 班级名称 -->
      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item label="班级名称" prop="name">
            <el-input
              v-model="clazz.name"
              placeholder="请输入班级名称，如：Java就业100期"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 班级教室 -->
      <el-row>
        <el-col :span="18">
          <el-form-item label="班级教室" prop="room">
            <el-input
              v-model="clazz.room"
              placeholder="请填写班级教室"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 开课时间 -->
      <el-row>
        <el-col :span="18">
          <el-form-item label="开课时间" prop="beginDate">
            <el-date-picker
              v-model="clazz.beginDate"
              type="date"
              style="width: 100%"
              placeholder="选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 结课时间 -->
      <el-row>
        <el-col :span="18">
          <el-form-item label="结课时间" prop="endDate">
            <el-date-picker
              v-model="clazz.endDate"
              type="date"
              style="width: 100%"
              placeholder="选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 班主任 -->
      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item label="班主任" prop="masterId">
            <el-select
              v-model="clazz.masterId"
              placeholder="请选择班主任"
              style="width: 100%"
            >
              <el-option
                v-for="emp in empList"
                :key="emp.id"
                :label="emp.name"
                :value="emp.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 学科 -->
      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item label="学科" prop="subject">
            <el-select
              v-model="clazz.subject"
              placeholder="请选择学科"
              style="width: 100%"
            >
              <el-option
                v-for="sub in subList"
                :key="sub.value"
                :label="sub.name"
                :value="sub.value"
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
