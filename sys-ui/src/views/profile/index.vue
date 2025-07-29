<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="9" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{$t('user.text.profile')}}</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user"/> {{$t('user.label.name')}}
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone"/> {{$t('user.label.phone')}}
                <div class="pull-right">{{ user.userPhone }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email"/> {{$t('user.label.email')}}
                <div class="pull-right">{{ user.userEmail }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples"/> {{$t('user.label.role')}}
                <div class="pull-right" v-if="user.roles && user.roles.length>0">{{ user.roles }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/> {{$t('user.label.dept')}}
                <div class="pull-right" v-if="user.depts && user.depts.length>0">{{ user.depts }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/> {{$t('user.label.report')}}
                <div class="pull-right" v-if="user.parents && user.parents.length>0">{{ user.parents }} </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/> {{$t('commons.label.createTime')}}
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="15" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{$t('commons.button.edit')}}</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane :label="$t('user.text.basic')" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane v-if="user.userType === 'sys'" :label="$t('user.button.passwd')" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
            <el-tab-pane v-if="user.userType === 'sys'" :label="$t('user.text.mfa')" name="configMFA">
              <el-form ref="form" :model="mfa" @submit.native.prevent label-width="auto">
                <el-row>
                  <el-col :span="9" style="text-align: center;">
                    <mfa-qrcode v-if="mfa.mfaUrl != null" v-model="mfa.mfaUrl" :otpAuthUrl="mfa.mfaUrl"/>
                  </el-col>
                  <el-col :span="15">
                    <p>【推荐】微信小程序搜索 数盾OTP</p>
                    <p>【推荐】腾讯身份验证码 简单好用 <a class="link"
                        href="https://a.app.qq.com/o/simple.jsp?pkgname=com.tencent.authenticator">Android</a></p>
                    <p>Authy 功能丰富 <a class="link"
                        href="https://authy.com/download/">iOS/Android/Windows/Mac/Linux</a> 、 <a class="link"
                        href="https://chrome.google.com/webstore/detail/authy/gaedmjdfmmahhbjefcbgaolhhanlaolb?hl=cn">Chrome
                      扩展</a></p>
                    <p>Google Authenticator 简单易用，但不支持密钥导出备份 <a class="link"
                        href="https://apps.apple.com/us/app/google-authenticator/id388497605">iOS</a> 、 <a class="link"
                        href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&amp;hl=cn">Android</a>
                    </p>
                    <p>Microsoft Authenticator 微软全家桶 <a class="link"
                        href="https://www.microsoft.com/zh-cn/account/authenticator">iOS/Android</a></p>
                    <p>1Password 强大安全的密码管理付费应用 <a class="link" href="https://1password.com/zh-cn/downloads/">iOS/Android/Windows/Mac/Linux/ChromeOS</a>
                    </p>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9" style="text-align: center;">
                    <el-form-item label="口令:">
                      <div style="display: flex; align-items: center; width: 100%;">
                        <el-input v-model="mfa.mfaCode" maxlength="50" style="width: 50%; flex: 1; margin-right: 10px;"/>
                        <el-button v-if="mfa.mfaUrl != null" type="primary" size="mini" @click="submit">开启MFA认证</el-button>
                        <el-button v-else type="danger" size="mini" @click="cancel">关闭MFA认证</el-button>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar.vue";
import userInfo from "./userInfo.vue";
import resetPwd from "./resetPwd.vue";
import {disableMFA, enableMFA, getMFA, getUserProfile} from "@/api/profile";
import MfaQrcode from "@/components/MFA/index.vue";

export default {
  name: "Profile",
  components: {MfaQrcode, userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      mfa: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
    this.acquireMFA();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    acquireMFA() {
      getMFA().then(response => {
        this.mfa = response.data;
      });
    },
    submit() {
      enableMFA(this.mfa).then(() => {
        this.acquireMFA();
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      });
    },
    cancel(){
      disableMFA(this.mfa).then(() => {
        this.acquireMFA();
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      });
    }
  }
};
</script>

<style scoped>
.link {
  color: #409EFF; /* Element UI 主色 */
  cursor: pointer;
}
</style>
