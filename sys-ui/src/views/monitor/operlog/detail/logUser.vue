<template>
  <el-dialog title="用户管理日志" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="log" label-width="100px" size="mini">
      <el-row>
        <el-col :span="12">
          <el-form-item label="操作人：">{{ log.userName }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="操作结果：">
            <template v-for="item in dict.type.sys_is_success">
              <span v-if="log.logStatus === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
              <span v-if="log.logStatus === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
            </template>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="访问ip：">{{ log.ip }}</el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="访问时间：">{{ log.logTime }}</el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="访问路径：">{{ log.url }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="操作描述：">{{ log.logDesc }} </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item v-if="log.actionCode === 'readonly'" label="操作内容：">
            <span v-if="log.logContent.req.readOnly === 1">设置用户[{{ log.logContent.req.userName }}]为只读</span>
            <span v-if="log.logContent.req.readOnly === 0">取消用户[{{ log.logContent.req.userName }}]为只读</span>
          </el-form-item>
          <el-form-item v-if="log.actionCode === 'status'" label="操作内容：">
            <span v-if="log.logContent.req.userStatus === 1">启用用户[{{ log.logContent.req.userName }}]</span>
            <span v-if="log.logContent.req.userStatus === 0">停用用户[{{ log.logContent.req.userName }}]</span>
          </el-form-item>
          <el-form-item v-if="log.actionCode === 'add'" label="用户信息：">
            <el-form ref="form-add" :model="log.logContent.req" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ log.logContent.req.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ log.logContent.req.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ log.logContent.req.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ log.logContent.req.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="log.logContent.req.rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
                      <span v-if="log.logContent.req.rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.user_sex">
                      <span v-if="log.logContent.req.userSex === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.userSex === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
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
                  <el-form-item label="备注：">{{ log.logContent.req.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>
          <el-form-item v-if="log.actionCode === 'edit'" label="修改前：">
            <el-form ref="form-before" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ log.logContent.resp.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ log.logContent.resp.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ log.logContent.resp.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ log.logContent.resp.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="log.logContent.resp.rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
                      <span v-if="log.logContent.resp.rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.user_sex">
                      <span v-if="log.logContent.resp.userSex === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.resp.userSex === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
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
                  <el-form-item label="备注：">{{ log.logContent.resp.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>
          <el-form-item v-if="log.actionCode === 'edit'" label="修改后：">
            <el-form ref="form-after" :model="log.logContent.req" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户名称：">{{ log.logContent.req.userName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户账号：">{{ log.logContent.req.userAccount }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户手机：">{{ log.logContent.req.userPhone }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="用户邮箱：">{{ log.logContent.req.userEmail }}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="职级：">
                    <template v-for="item in dict.type.post_level">
                      <span v-if="log.logContent.req.rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
                      <span v-if="log.logContent.req.rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="性别：">
                    <template v-for="item in dict.type.user_sex">
                      <span v-if="log.logContent.req.userSex === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.userSex === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
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
                  <el-form-item label="备注：">{{ log.logContent.req.remark }}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="log.actionCode === 'delete'">
        <el-table :data="log.logContent.resp" :header-cell-style="{'text-align':'center'}">
          <el-table-column :label="$t(`user.label.name`)" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column :label="$t(`user.label.account`)" align="center" key="userAccount" prop="userAccount" :show-overflow-tooltip="true"/>
          <el-table-column :label="$t(`user.label.sex`)" align="center">
            <template slot-scope="{row: {userSex}}">
              <template v-for="item in dict.type.user_sex">
                <span v-if="userSex === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                <span v-if="userSex === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`user.label.phone`)" align="left" key="userPhone" prop="userPhone"/>
          <el-table-column :label="$t(`user.label.email`)" align="left" key="userEmail" prop="userEmail" :show-overflow-tooltip="true"/>
          <el-table-column :label="$t(`user.label.rank`)" align="center" :show-overflow-tooltip="true">
            <template slot-scope="{row: {rank}}">
              <template v-for="item in dict.type.post_level">
                <span v-if="rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
                <span v-if="rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { info, userQuery } from "@/api/monitor/operlog";
export default {
  dicts: ['sys_is_success', 'user_sex', 'post_level'],
  data() {
    return {
      visible: false,
      log: {},
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
    show(id) {
      this.getDetail(id);
      this.visible = true;
    },
    getDetail(id) {
      info(id).then(res => {
        this.log = res.data;
        if(this.log.actionCode === 'add'){
          userQuery(this.log.logContent.req).then(response => {
            this.req = response.data;
          });
        }
        if(this.log.actionCode === 'edit'){
          userQuery(this.log.logContent.req).then(response => {
            this.req = response.data;
          });
          userQuery(this.log.logContent.resp).then(response => {
            this.resp = response.data;
          });
        }
      });
    },
  }
};
</script>
