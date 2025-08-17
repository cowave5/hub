<template>
  <div class="register">
    <el-form ref="form" :model="form" :rules="registerRules" class="register-form">
      <h3 class="title">控维通信</h3>
      <el-form-item prop="userAccount">
        <el-input v-model="form.userAccount" type="text" placeholder="账号">
          <svg-icon slot="prefix" icon-class="guide" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="userName">
        <el-input v-model="form.userName" type="text" placeholder="昵称">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="userEmail">
        <el-input v-model="form.userEmail" type="text" placeholder="邮箱">
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input v-model="form.captcha" placeholder="验证码"
                  style="width: 63%" @keyup.enter.native="handleRegister">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="confirm-code">
          <confirmCode @getConfirmCodeClick="getConfirmCodeClick" @timeKeepingDone="timeKeepingDone"
                       :timeKeeping="timeKeeping" :seconds="30"/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleRegister">
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/cowave/login'">系统账号登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <div class="el-register-footer">
      <span>{{ version }} Copyright ©2017-{{ year }} Cowave All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import {getEmailCode, register} from "@/api/auth";
import confirmCode from "@/views/confirmCode.vue";

export default {
  name: "Register",
  components: { confirmCode },
  data() {
    return {
      version: "",
      year: new Date().getFullYear(),
      codeUrl: "",
      form: {
        tenantId: "cowave",
        userAccount: "",
        userName: "",
        userEmail: "",
        captcha: "",
      },
      registerRules: {
        userAccount: [
          { required: true, trigger: "blur", message: "请输入账号" },
          { min: 3, max: 20, message: '账号长度必须介于 3 和 20 之间', trigger: 'blur' }
        ],
        userName: [
          { required: true, trigger: "blur", message: "请输入昵称" }
        ],
        userEmail: [
          { required: true, trigger: "blur", message: "请输入邮箱" },
          {type: "email", message: this.$t('user.rules.email'), trigger: ["blur", "change"]}
        ],
        code: [{ required: true, trigger: "blur", message: "请输入验证码" }]
      },
      loading: false,
      captchaOnOff: true,
      timeKeeping: false,
    };
  },
  created() {
    this.version = process.env.VUE_APP_VERSION;
  },
  methods: {
    getConfirmCodeClick() {
      this.timeKeeping = true;
      getEmailCode(this.form.userEmail).then(res => {
        this.$modal.msgSuccess("验证码已发送至邮箱");
      });
    },
    timeKeepingDone(){
      this.timeKeeping = false;
    },
    handleRegister() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.form).then(res => {
            this.loading = false;
            this.$modal.msgSuccess("注册成功, 初始密码为: " + res.data);
            setInterval(() => {
              this.$router.push("/cowave/login");
            }, 2000 );
          }).catch(() => {
            this.form.captcha = "";
            this.loading = false;
          })
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.register {
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
.confirm-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.register-form {
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
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
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
.register-code-img {
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
