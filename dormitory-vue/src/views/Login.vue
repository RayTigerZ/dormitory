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
            <!-- <span>管理员帐号：20100001，密码：admin123</span> -->
            <el-form-item style="padding-top:25px;">
              <el-button
                type="primary"
                class="login-button"
                @click="submitLogin"
                >登 录</el-button
              >
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { login } from "../api/login";
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
          login(this.user.account, this.user.password)
            .then(res => {
              if (res.data && res.data.code && res.data.code == 200) {
                let redirect = this.$route.query.redirect;

                if (redirect == "" || redirect == undefined) {
                  redirect = "manage";
                }

                this.$store.commit("setToken", res.data.data.token);
                this.$store.commit("setUserName", res.data.data.name);
                this.$router.push({ path: redirect });
              }
            })
            .catch(err => {
              console.log(err);
            });
        }
      });
    }
  },
  created() {
    console.log("管理员帐号：20100001，密码：admin123");
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
  min-height: 600px;
  min-width: 720px;
  background-size: cover;
  background-position: center center;
}

.login-title {
  text-align: center;
  font-size: 28px;
  font-weight: 600;
  font-family: Arial, "Microsoft YaHei", sans-serif;
  padding: 40px 0px 20px 0px;
  color: rgb(93, 93, 93);
  text-transform: capitalize;
}
.login-card {
  border: 1px;
  width: 350px;
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
