<template>
  <el-dialog title="角色管理日志" :visible.sync="visible" width="900px" top="5vh">
    <el-form ref="form" :model="log" label-width="100px" size="mini">
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
            <template v-for="item in success_failed">
              <span v-if="info.opStatus === item.value">{{ $t(item.label) }}</span>
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
          <!--     新增     -->
          <el-form-item v-if="info.opAction === 'op_create'" label="角色信息：">
            <el-form ref="form-add" :model="info.request.body" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="角色名称：">{{ info.request.body.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ info.request.body.roleCode }}</el-form-item>
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
                  <el-form-item label="角色名称：">{{ info.opContent.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ info.opContent.roleCode }}</el-form-item>
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
                  <el-form-item label="角色名称：">{{ info.request.body.roleName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="角色编码：">{{ info.request.body.roleCode }}</el-form-item>
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
          <el-table-column prop="roleName" align="center" label="角色名称"/>
          <el-table-column prop="roleCode" align="center" label="角色编码"/>
          <el-table-column prop="remark" align="center" label="备注"/>
        </el-table>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { success_failed } from '@/utils/constants';
export default {
  dicts: [],
  data() {
    return {
      success_failed: success_failed,
      visible: false,
      info: {}
    };
  },
  methods: {
    show(info) {
      this.info = info;
      this.visible = true;
    },
  }
};
</script>
