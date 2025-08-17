<template>
  <div class="register">
    <el-form ref="form" :model="ldap" :rules="ldapRules" class="register-form">
      <h3 class="title">控维通信</h3>
      <el-form-item prop="username">
        <el-input v-model="ldap.username" type="text" placeholder="域账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="ldap.password" type="password" placeholder="密码" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
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

export default {
  name: "Ldap",
  data() {
    return {
      version: "",
      year: new Date().getFullYear(),
      ldap: {
        tenantId: "cowave",
        username: "",
        password: "",
      },
      ldapRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的域账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
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
    handleLogin() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch("Ldap", this.ldap).then(async () => {
            this.$router.push({path: this.redirect || "/"}).catch(() => {});
            await this.$store.dispatch('OpenNoticeSocket');
          }).catch(() => {
            this.loading = false;
          });
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
