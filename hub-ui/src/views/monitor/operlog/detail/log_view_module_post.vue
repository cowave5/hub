<template>
  <el-dialog title="岗位管理日志" :visible.sync="visible" width="900px" top="5vh">
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
          <el-form-item v-if="info.opAction === 'op_create'" label="岗位信息：">
            <el-form ref="form-add" :model="info.request.body" label-width="100px" style="background-color: #e5f3f3">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="岗位名称：">{{ info.request.body.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="info.request.body.postType === item.code">{{ $t(item.name) }}</span>
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
                    <template v-for="item in enable_disable">
                      <span v-if="info.request.body.postStatus === item.value">{{ $t(item.label) }}</span>
                    </template>
                  </el-form-item>
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
                  <el-form-item label="岗位名称：">{{ info.opContent.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="info.opContent.postType === item.code">{{ $t(item.name) }}</span>
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
                    <template v-for="item in enable_disable">
                      <span v-if="info.opContent.postStatus === item.value">{{ $t(item.label) }}</span>
                    </template>
                  </el-form-item>
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
                  <el-form-item label="岗位名称：">{{ info.request.body.postName }}</el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="岗位类型：">
                    <template v-for="item in dict.type.post_type">
                      <span v-if="info.request.body.postType === item.code">{{ $t(item.name) }}</span>
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
                    <template v-for="item in enable_disable">
                      <span v-if="info.request.body.postStatus === item.value">{{ $t(item.label) }}</span>
                    </template>
                  </el-form-item>
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
          <el-table-column prop="postName" align="center" label="岗位名称"/>
          <el-table-column prop="postType" align="center" label="岗位类型">
            <template slot-scope="{row: {postType}}">
              <template v-for="item in dict.type.post_type">
                <span v-if="postType === item.code">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="postStatus" align="center" label="岗位状态">
            <template slot-scope="{row: {postStatus}}">
              <template v-for="item in enable_disable">
                <span v-if="postStatus === item.value">{{ $t(item.label) }}</span>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script>
import { getPostName } from '@/api/system/post'
import { enable_disable, success_failed } from '@/utils/constants';
export default {
  dicts: ['post_type'],
  data() {
    return {
      enable_disable: enable_disable,
      success_failed: success_failed,
      visible: false,
      info: {},
      req: {
        parent: '',
      },
      resp: {
        parent: ''
      }
    };
  },
  methods: {
    show(info) {
      this.info = info;
      this.visible = true;
      if (this.info.opAction === 'op_create') {
        if (info.request.body.parentId) {
          getPostName(info.request.body.parentId).then(response => {
            this.req.parent = response.data
          })
        }
      }

      if (this.info.opAction === 'op_edit') {
        if (info.request.body.parentId) {
          getPostName(info.request.body.parentId).then(response => {
            this.req.parent = response.data
          })
        }
        if (info.opContent.parentId) {
          getPostName(info.opContent.parentId).then(response => {
            this.resp.parent = response.data
          })
        }
      }
    },
  }
};
</script>
