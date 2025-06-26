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
                <div class="pull-right" v-if="user.roles.length>0">{{ user.roles }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/> {{$t('user.label.dept')}}
                <div class="pull-right" v-if="user.depts.length>0">{{ user.depts }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/> {{$t('user.label.report')}}
                <div class="pull-right" v-if="user.parents.length>0">{{ user.parents }} </div>
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
import { getUserProfile } from "@/api/profile";

export default {
  name: "Profile",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    }
  }
};
</script>
