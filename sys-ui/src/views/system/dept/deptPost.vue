<template>
  <div class="app-container">
    <h4 class="form-header h4">{{$t('dept.text.info')}}</h4>
    <el-form ref="form" :model="deptInfo" label-width="80px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('dept.label.name')">
            <el-input  v-model="deptInfo.deptName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('dept.label.phone')">
            <el-input v-model="deptInfo.deptPhone" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!--  筛选栏  -->
    <h4 class="form-header h4">{{$t('post.text.list')}}</h4>
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('post.label.name')" prop="postName">
        <el-input v-model="queryParams.postName" :placeholder="$t('post.placeholder.name')" clearable
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('post.label.type')" prop="postType">
        <el-select v-model="queryParams.postType" :placeholder="$t('post.placeholder.type')" clearable>
          <el-option v-for="dict in dict.type.post_type" :key="dict.code" :value="dict.code" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{ $t('commons.button.search') }}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="handleReset">{{ $t('commons.button.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddPost">
          {{$t('post.button.add')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-circle-close" size="mini" @click="handleRemovePost"
                   :disabled="multiple || !checkPermit(['sys:dept:positions:remove'])">
          {{$t('post.button.remove')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">
          {{$t('commons.button.close')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!--  列表数据  -->
    <el-table ref="table" :data="list" @selection-change="selectRow" v-loading="loading">
      <el-table-column type="selection" width="50"/>
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('post.label.name')" align="center" prop="postName" />
      <el-table-column :label="$t('post.label.type')" align="center" prop="postType">
        <template slot-scope="{row: {postType}}">
          <template v-for="item in dict.type.post_type">
            <span v-if="postType === item.code">{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dept.text.default_post')" align="center">
        <template slot-scope="{row: {isDefault}}">
          <template v-for="item in dict.type.yes_no">
            <span v-if="isDefault === item.value">
              <el-tag :type="item.css">{{ $t(item.name) }}</el-tag>
            </span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="handleRemovePost(scope.row)"
                     :disabled="!checkPermit(['sys:dept:positions:remove'])">
            {{$t('post.button.remove')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize"
                :page.sync="queryParams.page" @pagination="getList"/>
    <select-post ref="select" :dept-id="deptId" @ok="handleQuery"/>
  </div>
</template>

<script>
import { getDeptInfo, getConfiguredPosts, removeDeptPosts } from '@/api/system/dept'
import {checkPermit} from "@/utils/permission";
import selectPost from '@/views/system/dept/selectPost.vue'

export default {
  name: "DeptPost",
  components: { selectPost },
  dicts: ['post_type', 'yes_no'],
  data() {
    return {
       // 遮罩层
      loading: false,
      // 非多个禁用
      multiple: true,
      // 显示筛选栏
      showSearch: true,
      // 筛选参数
      queryParams: {
        page: 1,
        pageSize: 10,
        deptId: undefined,
        postName: undefined,
        postType: undefined
      },
      // 分页数据
      list: [],
      // 分页总数
      total: 0,
      // 部门id
      deptId: undefined,
      // 部门信息
      deptInfo: {},
      // 选中岗位
      postIds: []
    };
  },
  created() {
    this.deptId = this.$route.params && this.$route.params.deptId;
    if (this.deptId) {
      getDeptInfo(this.deptId).then((resp) => {
        this.deptInfo = resp.data;
      });
      this.getList();
    }
  },
  methods: {
    checkPermit,
    /** 岗位列表 */
    getList() {
      this.queryParams.deptId = this.deptId;
      getConfiguredPosts(this.queryParams).then((resp) => {
        this.list = resp.data.list
        this.total = resp.data.total;
      });
    },
    /** 选中行 */
    selectRow(selection) {
      this.postIds = selection.map(item => item.postId);
      this.multiple = !selection.length;
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    handleReset() {
      this.$refs.queryForm.resetFields();
      this.handleQuery()
    },
    /** 关闭 */
    handleClose() {
      const obj = { path: '/system/dept' }
      this.$tab.closeOpenPage(obj)
    },
    /** 添加岗位 */
    handleAddPost() {
      this.$refs.select.show();
    },
    /** 取消岗位 */
    handleRemovePost(row) {
      const postIds = row.postId || this.postIds;
      const deptId = this.deptId;
      const msg = row.postId
          ? this.$t('post.confirm.remove', { arg1: row.postName })
          : this.$t('post.confirm.remove_select');
      this.$modal.confirm(msg).then(function() {
        return removeDeptPosts(deptId, postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch((e) => {console.log(e)});
    },
  },
};
</script>
