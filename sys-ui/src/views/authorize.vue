<template>
  <div class="login">
    <el-form ref="form" :model="form" class="login-form">
      <h3 class="title">授权应用访问</h3>
      <el-form-item prop="clientName">
        <el-input v-model="form.clientName" type="text" autocomplete="new-password" label="客户端名称">
          <svg-icon slot="prefix" icon-class="model" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.authScope" type="text" autocomplete="new-password" label="授权范围">
          <svg-icon slot="prefix" icon-class="vscope" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;" v-if="form.code">
        <div class="gitlab" style="width:100%; height:34px; display: flex;align-items: center;justify-content: center" @click="handleRedirect">
          同意授权
        </div>
      </el-form-item>
    </el-form>
    <div class="el-login-footer">
      <span>{{ version }} Copyright ©2017-{{ year }} Cowave All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getOAuth2Code } from '@/api/auth'

export default {
  name: 'grant',
  data() {
    return {
      version: "",
      year: new Date().getFullYear(),
      form: {},
      param: {
        state: undefined,
        client_id: undefined,
        redirect_uri: undefined,
        response_type: undefined
      }
    };
  },
  created() {
    this.version = process.env.VUE_APP_VERSION;
    this.param.state = this.$route.query.state || '';
    this.param.client_id = this.$route.query.client_id;
    this.param.redirect_uri = this.$route.query.redirect_uri;
    this.param.response_type = this.$route.query.response_type;
    getOAuth2Code(this.param).then(resp => {
      this.form = resp.data;
    });
  },
  methods: {
    handleRedirect() {
      window.location.href = '/prod-api/admin/api/v1/oauth/client/redirect/' + this.form.code;
    }
  }
};
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-cowave.jpg");
  background-size: cover;
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

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #ffffff;
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

.gitlab {
  background-color: #fc6d26;
  border: none;
  border-radius: 5px;
  margin-top: 4px;
  cursor: pointer;
  display: inline-block;
}
</style>
