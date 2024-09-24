<template>
  <el-dialog title="岗位管理日志" :visible.sync="visible" width="900px" top="5vh">
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
            <span v-if="log.logContent.req.readOnly === 1">设置岗位[{{ log.logContent.req.postName }}]为只读</span>
            <span v-if="log.logContent.req.readOnly === 0">取消岗位[{{ log.logContent.req.postName }}]为只读</span>
          </el-form-item>

          <el-form-item v-if="log.actionCode === 'add'" label="岗位信息：">
            <el-form ref="form-add" :model="log.logContent.req" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="岗位名称：">{{ log.logContent.req.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="log.logContent.req.postType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.postType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="上级岗位：">{{ req.parent }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位状态：">
                    <template v-for="item in dict.type.sys_is_enable">
                      <span v-if="log.logContent.req.postStatus === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.postStatus === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
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
                  <el-form-item label="岗位名称：">{{ log.logContent.resp.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="log.logContent.resp.postType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.resp.postType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="上级岗位：">{{ resp.parent }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位状态：">
                    <template v-for="item in dict.type.sys_is_enable">
                      <span v-if="log.logContent.resp.postStatus === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.resp.postStatus === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
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
                  <el-form-item label="岗位名称：">{{ log.logContent.req.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="log.logContent.req.postType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.postType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="上级岗位：">{{ req.parent }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位状态：">
                    <template v-for="item in dict.type.sys_is_enable">
                      <span v-if="log.logContent.req.postStatus === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                      <span v-if="log.logContent.req.postStatus === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
                    </template>
                  </el-form-item>
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
          <el-table-column prop="postName" align="center" label="岗位名称"/>
          <el-table-column prop="postType" align="center" label="岗位类型">
            <template slot-scope="{row: {postType}}">
              <template v-for="item in dict.type.post_type">
                <span v-if="postType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                <span v-if="postType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="postStatus" align="center" label="岗位状态">
            <template slot-scope="{row: {postStatus}}">
              <template v-for="item in dict.type.sys_is_enable">
                <span v-if="postStatus === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                <span v-if="postStatus === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import {info, postQuery} from "@/api/monitor/operlog";
export default {
  dicts: ['sys_is_success', 'post_type', 'sys_is_enable'],
  data() {
    return {
      visible: false,
      log: {},
      req: {
        parent: '',
      },
      resp: {
        parent: ''
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
          postQuery(this.log.logContent.req).then(response => {
            this.req = response.data;
          });
        }
        if(this.log.actionCode === 'edit'){
          postQuery(this.log.logContent.req).then(response => {
            this.req = response.data;
          });
          postQuery(this.log.logContent.resp).then(response => {
            this.resp = response.data;
          });
        }
      });
    },
  }
};
</script>
