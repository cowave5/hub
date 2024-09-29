<template>
  <el-dialog title="用户管理日志" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="info" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="操作人：">{{ info.access.accessUserName }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作时间：">{{ info.opTime }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="访问ip：">{{ info.ip }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作结果：">
            <template v-for="item in dict.type.success_failed">
              <span v-if="info.opStatus === item.value">{{ $t(item.name) }}</span>
            </template>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="访问路径：">{{ info.url }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="操作描述：">{{ info.opDesc }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <!--     状态修改     -->
          <el-form-item v-if="info.opAction === 'op_status'" label="操作内容：">
            <span v-if="info.request.body.userStatus === 1">启用用户"{{ info.request.body.userName }}"</span>
            <span v-if="info.request.body.userStatus === 0">停用用户"{{ info.request.body.userName }}"</span>
          </el-form-item>

          <!--    新增      -->
          <el-form-item v-if="info.opAction === 'op_create'" label="用户信息：">
            <el-form ref="form-add" :model="info.request.body" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ info.request.body.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ info.request.body.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ info.request.body.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ info.request.body.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="info.request.body.userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.sex">
                      <span v-if="info.request.body.userSex === item.value">{{$t(item.name)}}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="用户角色：">{{ req.roles }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="部门岗位：">{{ req.posts }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="汇报对象：">{{ req.parents }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ info.request.body.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>

          <!--     修改前     -->
          <el-form-item v-if="info.opAction === 'op_edit'" label="修改前：">
            <el-form ref="form-before" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ info.opContent.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ info.opContent.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ info.opContent.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ info.opContent.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="info.opContent.userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.sex">
                      <span v-if="info.opContent.userSex === item.value">{{$t(item.name)}}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="用户角色：">{{ resp.roles }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="部门岗位：">{{ resp.posts }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="汇报对象：">{{ resp.parents }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ info.opContent.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>

          <!--     修改后     -->
          <el-form-item v-if="info.opAction === 'op_edit'" label="修改后：">
            <el-form ref="form-after" :model="info.request.body" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ info.request.body.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ info.request.body.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ info.request.body.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ info.request.body.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="info.request.body.userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.sex">
                      <span v-if="info.request.body.userSex === item.value">{{$t(item.name)}}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="用户角色：">{{ req.roles }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="部门岗位：">{{ req.posts }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="汇报对象：">{{ req.parents }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
                  <el-form-item label="备注：">{{ info.request.body.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>
        </el-col>
      </el-row>

      <!--   删除   -->
      <el-row v-if="info.opAction === 'op_delete'">
        <el-table :data="info.opContent" :header-cell-style="{'text-align':'center'}">
          <el-table-column :label="$t('user.label.name')" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column :label="$t('user.label.account')" align="center" key="userAccount" prop="userAccount" :show-overflow-tooltip="true"/>
          <el-table-column :label="$t('user.label.sex')" align="center">
            <template slot-scope="{row: {userSex}}">
              <template v-for="item in dict.type.sex">
                <span v-if="userSex === item.value">{{$t(item.name)}}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t('user.label.phone')" align="left" key="userPhone" prop="userPhone"/>
          <el-table-column :label="$t('user.label.email')" align="left" key="userEmail" prop="userEmail" :show-overflow-tooltip="true"/>
          <el-table-column :label="$t('user.label.rank')" align="center" :show-overflow-tooltip="true">
            <template slot-scope="{row: {userRank}}">
              <template v-for="item in dict.type.post_level">
                <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-form>
  </el-dialog>
</template>
<script>
import {getUserNames} from "@/api/system/user";
import {getRoleNames} from "@/api/system/role";
import {getDeptPostNames} from "@/api/system/post";
export default {
  dicts: ['success_failed', 'sex', 'post_level'],
  data() {
    return {
      visible: false,
      info: {},
      req: {
        roles: '',
        posts: '',
        parents: '',
      },
      resp: {
        roles: '',
        posts: '',
        parents: ''
      }
    };
  },
  methods: {
    show(info) {
      this.info = info;
      this.req.parents = '';
      this.req.roles = '';
      this.req.posts = '';
      this.resp.parents = '';
      this.resp.roles = '';
      this.resp.posts = '';
      if (this.info.opAction === 'op_create') {
        if (info.request.body.parentIds && info.request.body.parentIds.length > 0) {
          getUserNames(info.request.body.parentIds).then(response => {
            this.req.parents = response.data;
          });
        }
        if (info.request.body.roleIds && info.request.body.roleIds.length > 0) {
          getRoleNames(info.request.body.roleIds).then(response => {
            this.req.roles = response.data;
          });
        }
        if (info.request.body.deptPostIds && info.request.body.deptPostIds.length > 0) {
          getDeptPostNames(info.request.body.deptPostIds).then(response => {
            this.req.posts = response.data;
          });
        }
      }
      if (this.info.opAction === 'op_edit') {
        if (info.request.body.parentIds && info.request.body.parentIds.length > 0) {
          getUserNames(info.request.body.parentIds).then(response => {
            this.req.parents = response.data;
          });
        }
        if (info.request.body.roleIds && info.request.body.roleIds.length > 0) {
          getRoleNames(info.request.body.roleIds).then(response => {
            this.req.roles = response.data;
          });
        }
        if (info.request.body.deptPostIds && info.request.body.deptPostIds.length > 0) {
          getDeptPostNames(info.request.body.deptPostIds).then(response => {
            this.req.posts = response.data;
          });
        }
        if (info.opContent.parentIds && info.opContent.parentIds.length > 0) {
          getUserNames(info.opContent.parentIds).then(response => {
            this.resp.parents = response.data;
          });
        }
        if (info.opContent.roleIds && info.opContent.roleIds.length > 0) {
          getRoleNames(info.opContent.roleIds).then(response => {
            this.resp.roles = response.data;
          });
        }
        if (info.opContent.deptPostIds && info.opContent.deptPostIds.length > 0) {
          getDeptPostNames(info.opContent.deptPostIds).then(response => {
            this.resp.posts = response.data;
          });
        }
      }
      this.visible = true;
    },
  }
};
</script>
