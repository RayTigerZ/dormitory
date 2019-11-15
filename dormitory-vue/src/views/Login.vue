<template>
  <el-row>
    <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
      <div class="login-body">
        <el-card shadow="never" class="login-card">
          <el-form ref="loginForm" :model="user" :rules="rules">
            <el-form-item>
              <legend class="login-title">学生宿舍管理系统</legend>
            </el-form-item>
            <el-form-item prop="account">
              <el-input
                type="text"
                placeholder="请输入帐号"
                prefix-icon="el-icon-user"
                v-model="user.account"
              ></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                type="password"
                placeholder="请输入登录密码"
                prefix-icon="el-icon-lock"
                v-model="user.password"
              ></el-input>
            </el-form-item>
            <el-form-item style="padding-top:25px;">
              <el-button type="primary" class="login-button" @click="submitLogin">登 录</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { postRequest } from "../utils/request";
import md5 from "js-md5";
export default {
  data() {
    return {
      user: {
        account: "",
        password: ""
      },
      rules: {
        account: [
          {
            required: true,
            message: "请输入帐号",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur"
          },
          {
            min: "6",
            max: "18",
            message: "密码长度在6-18位",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submitLogin() {
      this.$refs["loginForm"].validate(valid => {
        if (valid) {
          let params = {
            account: this.user.account,
            password: md5(this.user.password)
          };
          console.log(params);
          postRequest("/user/login", params)
            .then(res => {
              if (res.data && res.data.code && res.data.code == 200) {
                this.$router.push({ path: "manage" });
              }
            })
            .catch(err => {
              console.log(err);
            });
        }
      });
    }
  }
};
</script>


<style scoped>
.el-row,
.el-col {
  height: 100%;
}
.login-body {
  background: url(../assets/images/bg.jpg) no-repeat fixed;
  margin: 0px auto;
  overflow: hidden;
  height: 100%;
  min-width: 720px;
  background-size: cover;
  background-position: center center;
}

.login-title {
  font-size: 28px;
  font-weight: 600;
  font-family: Arial, "Microsoft YaHei", sans-serif;
  padding: 40px 0px 20px 0px;
  color: rgb(93, 93, 93);
  text-transform: capitalize;
}
.login-card {
  border: 1px;
  width: 320px;
  height: 370px;
  background: rgb(255, 255, 255);
  margin: 80px auto;
  padding: 0px 30px;
}
.el-card__body {
  padding: 0px;
}
.login-button {
  width: 100%;
  font-size: 18px;
}
</style>