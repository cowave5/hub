<template>
  <div class="login">
    <el-form ref="form" :model="form" :rules="loginRules" class="login-form">
      <h3 class="title">控维通信</h3>
      <el-form-item prop="username">
        <el-input v-model="form.username" type="text" autocomplete="new-password" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" type="password" autocomplete="new-password" placeholder="密码" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaOnOff">
        <el-input v-model="form.code" autocomplete="new-password" placeholder="验证码" style="width: 63%" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="form.rememberMe" style="margin:0px 0px 25px 0px; color: #ffffff;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div v-if="gitlab_uri !== undefined && gitlab_uri !== null" class="gitlab" style="width:100%; height:34px; display: flex;align-items: center;justify-content: center" @click="redirectGitLab">
          <img src="@/assets/images/gitlab.svg" alt="gitlab" style="width: 100px; height: 28px;">
        </div>
        <div style="float: right;">
          <router-link v-if="register" class="link-type" :to="'/cowave/register'" style="margin-right: 12px;">注册账号</router-link>
          <router-link class="link-type" :to="'/cowave/ldap'">域账号登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <div class="el-login-footer">
      <span>{{ version }} Copyright ©2017-{{ year }} Cowave All Rights Reserved.</span>
    </div>
  </div>
</template>
<script>
import {getCodeImg} from "@/api/auth";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      version: "",
      year: new Date().getFullYear(),
      codeUrl: "",
      form: {
        tenantId: "cowave",
        username: undefined,
        password: undefined,
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "blur", message: "请输入验证码" }]
      },
      loading: false,
      captchaOnOff: true,
      register: true,
      redirect: undefined,
      gitlab_uri: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  mounted() {
    localStorage.setItem('tenant_login_route', '/cowave/login');
  },
  created() {
    this.version = process.env.VUE_APP_VERSION;
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg("cowave").then(res => {
        this.codeUrl = "data:image/gif;base64," + res.data.img;
        this.form.uuid = res.data.uuid;
        this.captchaOnOff = res.data.captchaOnOff;
        this.register = res.data.registerOnOff;
        this.gitlab_uri = res.data.oauthGitlabUrl;
      });
    },
    getCookie() {
      const tenantId = Cookies.get("tenantId");
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.form = {
        tenantId: tenantId === undefined ? this.form.tenantId : username,
        username: username === undefined ? this.form.username : username,
        password: password === undefined ? this.form.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    redirectGitLab() {
      window.location.href = this.gitlab_uri;
    },
    handleLogin() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.form.rememberMe) {
            Cookies.set("tenantId", this.form.tenantId, { expires: 7 });
            Cookies.set("username", this.form.username, { expires: 7 });
            Cookies.set("password", encrypt(this.form.password), { expires: 7 });
            Cookies.set('rememberMe', this.form.rememberMe, { expires: 7 });
          } else {
            Cookies.remove("tenantId");
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.form).then(async response => {
            if (response.mfaRequired) {
              await this.$router.push({path: '/mfa'}).catch(() => {});
            } else {
              await this.$router.push({path: this.redirect || "/"}).catch(() => {});
              await this.$store.dispatch('OpenNoticeSocket');
            }
          }).catch(() => {
            this.loading = false;
            if (this.captchaOnOff) {
              this.form.code = "";
              this.getCode();
            }
          });
        }
      });
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-cowave.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #ffffff;
}

.gitlab {
  background-color: #fc6d26;
  border: none;
  border-radius: 5px;
  margin-top: 4px;
  cursor: pointer;
  display: inline-block;
}

.login-form {
  border:1px solid  transparent;
  border-radius: 15px;
  padding: 20px 20px 0px 20px;
  /* 为其整体设置接近透明的效果*/
  background-color: rgba(0,0,0,0.5);
  /* 设置box-shadow使其有立体感 */
  box-shadow: 5px 5px 0 0  rgba(0,0,0,0.2);
  position: relative;
  width: 350px;

  .el-input {
    height: 38px;
    input {
      height: 38px;
      color: #ffffff;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
    color: #ffffff;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}

.el-form-item__label{
  /* 给el-form组件的label标签颜色修改 */
  color: #FFFFFF;
}

::v-deep .el-input__inner{
  /* 使input框的背景变透明 */
  background-color: transparent;
  /* 使边框也变透明 */
  border-color: transparent;
  /* 给边框加阴影能够使其有立体感 */
  box-shadow: 2px 2px 0 0 rgba(0,0,0,0.2);
  /* 改变获取焦点后的竖线颜色 */
  caret-color: rgba(0, 255, 255,0.8);
  color:#FFFFFF;
}

.el-input__inner:hover{
  border-color: rgb(0, 255, 255);
}

.el-input__inner:focus{
  border-color: aqua;
}

.el-button--primary {
  color: #FFFFFF;
  background-color: #363b40;
  border-color: #363b40;
  background-color: rgba(0,0,0,0.5);
}

.el-button--primary:hover {
  color: #FFFFFF;
  background-color: #363b40;
  border-color: #363b40;
  background-color: rgba(0,0,0.5,0.8);
}
</style>
