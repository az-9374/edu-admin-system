import { createRouter, createWebHistory } from "vue-router";
import IndexView from "../views/index/index.vue";
import ClazzView from "@/views/clazz/index.vue";
import StuView from "@/views/stu/index.vue";
import DeptView from "@/views/dept/index.vue";
import EmpView from "@/views/emp/index.vue";
import EmpReportView from "@/views/report/emp/index.vue";
import StuReportView from "@/views/report/stu/index.vue";
import LogView from "@/views/log/index.vue";
import LoginView from "@/views/login/index.vue";
import LayoutView from "../views/layout/index.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    /*     { path: "/index", component: IndexView },
    { path: "/clazz", component: ClazzView },
    { path: "/stu", component: StuView },
    { path: "/dept", component: DeptView },
    { path: "/emp", component: EmpView },
    { path: "/report/emp", component: EmpReportView },
    { path: "/report/stu", component: StuReportView },
    { path: "/log", component: LogView },
    { path: "/login", component: LoginView }, */

    {
      path: "/",
      component: LayoutView,
      redirect: "/index",
      children: [
        { path: "/index", name: "index", component: IndexView },
        { path: "/clazz", name: "clazz", component: ClazzView },
        { path: "/stu", name: "stu", component: StuView },
        { path: "/dept", name: "dept", component: DeptView },
        { path: "/emp", name: "emp", component: EmpView },
        { path: "/report/emp", name: "empReport", component: EmpReportView },
        { path: "/report/stu", name: "stuReport", component: StuReportView },
        { path: "/log", name: "log", component: LogView },
      ],
    },

    { path: "/login", name: "login", component: LoginView },

    /*     {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue')
    } */
  ],
});

//路由守卫
router.beforeEach((to, from, next) => {
  // console.log("=== 守卫触发 ===", to.path);

  if (to.path === "/login") {
    // console.log("去登录页，放行");
    next();
    return;
  }

  const loginUser = localStorage.getItem("loginUser");
  console.log("loginUser:", loginUser);

  if (loginUser) {
    // console.log("已登录，放行");
    next();
  } else {
    // console.log('未登录，执行 next("/login")');
    next("/login");
  }
});

export default router;
