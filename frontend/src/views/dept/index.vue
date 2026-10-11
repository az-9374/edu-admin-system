<script setup>
import { ref, onMounted } from "vue";
// import axios from "axios";
import {
  queryAllApi,
  addDeptApi,
  searchDeptByIdApi,
  updateDeptApi,
  deleteDeptApi,
} from "../../api/dept";
import { ElMessage, ElMessageBox } from "element-plus";
// https://m1.apifoxmock.com/m1/8571182-8348103-default/depts?apifoxApiId=495060192

const search = async () => {
  // const result = await axios.get(
  //   "https://m1.apifoxmock.com/m1/8571182-8348103-default/depts?apifoxApiId=495060192"
  // );
  const result = await queryAllApi();

  if (result.code == 1) {
    deptList.value = result.data;
  } else {
    //弹出错误信息
    ElMessage.error(result.msg);
  }
};

const deptList = ref([]);
// 新增部门对话框的状态
const showDialog = ref(false);
// 表单标签宽度
const formLabelWidth = ref("120px");
// 表单数据
const deptForm = ref({ name: "" });
//表单校验
const formRules = ref({
  name: [
    { required: true, message: "请输入部门名称", trigger: ["blur", "change"] },
    {
      min: 2,
      max: 10,
      message: "长度在 2 到 10 个字符",
      trigger: ["blur", "change"],
    },
  ],
});

//表单打开刷新
const deptFormRef = ref(null);
const resetForm = () => {
  deptFormRef.value.resetFields();
};

const formTitle = ref("");
//add新增部门
const add = () => {
  formTitle.value = "新增部门";
  showDialog.value = true;
  deptForm.value = { name: "" }; //重置一下
};
//save新增确定
const save = async () => {
  //  预防校验失败但成功提交（校验结果要和save函数有关联）
  await deptFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error("表单校验不通过！");
      return;
    }
    let result = null;
    //判断是新增还是修改
    if (!deptForm.value.id) result = await addDeptApi(deptForm.value);
    else result = await updateDeptApi(deptForm.value);

    if (result.code == 1) {
      //提示信息并关闭窗口且刷新校验信息
      ElMessage.success("操作成功");
      showDialog.value = false;
      resetForm();
      //重新加载数据
      search();
    } else {
      //弹出错误信息
      ElMessage.error(result.msg);
    }
  });
};

//修改部门--1. 查询回显--复用新增dialog
const updateDept = async (id) => {
  formTitle.value = "修改部门";
  showDialog.value = true;
  //通过id找到部门名称
  const result = await searchDeptByIdApi(id);
  deptForm.value = result.data;
};
//修改部门--2. 保存修改-在前面的save里

//3.删除部门-消息弹框
const deleteDept = (id) => {
  ElMessageBox.confirm("您确定要删除该部门吗 ?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteDeptApi(id);
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

onMounted(() => {
  search();
});
</script>

<template>
  <h3>部门管理</h3>
  <div class="contain">
    <el-button plain @click="add()"> +新增部门 </el-button>
  </div>
  <div class="contain">
    <el-table :data="deptList" border style="width: 1130px">
      <el-table-column prop="id" label="序号" width="80" align="center" />
      <el-table-column
        prop="name"
        label="部门名称"
        width="350"
        align="center"
      />
      <el-table-column
        prop="updateTime"
        label="最后操作时间"
        width="350"
        align="center"
      />
      <el-table-column label="操作" width="350" align="center">
        <template #default="scope">
          <el-button size="small" @click="updateDept(scope.row.id)">
            编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="deleteDept(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- 新增弹窗 -->
  <el-dialog
    v-model="showDialog"
    :title="formTitle"
    width="500"
    @close="resetForm"
  >
    <el-form :model="deptForm" ref="deptFormRef" :rules="formRules">
      <el-form-item
        label="部门名称："
        :label-width="formLabelWidth"
        prop="name"
      >
        <el-input v-model="deptForm.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="save"> 确认 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.contain {
  margin: 10px 0;
}
</style>
