<template>
  <el-dialog :title="$t('post.button.select')" :visible.sync="visible" width="900px" top="5vh" append-to-body>
    <!--  筛选栏  -->
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true">
      <el-form-item :label="$t('post.label.name')" prop="postName">
        <el-input v-model="queryParams.postName" :placeholder="$t('post.placeholder.name')" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('post.label.type')" prop="postType">
        <el-select v-model="queryParams.postType" :placeholder="$t('post.placeholder.type')" clearable>
          <el-option v-for="dict in dict.type.post_type" :key="dict.code" :value="dict.code" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{$t('commons.button.search')}}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="handleReset">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  列表数据  -->
    <el-row>
      <el-table ref="table" :data="list" @selection-change="selectRow" height="480px">
        <el-table-column type="selection" width="50"/>
        <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
          <template slot-scope="scope">
            <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('post.label.name')" prop="postName" align="center"/>
        <el-table-column :label="$t('post.label.type')" prop="postType" align="center">
          <template slot-scope="{row: {postType}}">
            <template v-for="item in dict.type.post_type">
              <span v-if="postType === item.code">{{ $t(item.name) }}</span>
            </template>
          </template>
        </el-table-column>
        <el-table-column :label="$t('dept.text.default_post')" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.isDefault"
                       :active-value=1 :inactive-value=0 @change="(val)=>handleDefaultPost(val, scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize"
                  :page.sync="queryParams.page" @pagination="getList"/>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSubmit" :disabled="!checkPermit(['sys:dept:positions'])">
        {{$t('commons.button.confirm')}}
      </el-button>
      <el-button @click="visible = false">{{$t('commons.button.cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import { getUnConfiguredPosts, addDeptPosts } from '@/api/system/dept'
export default {
  dicts: ['post_type'],
  props: {
    deptId: {
      type: [Number, String]
    }
  },
  data() {
    return {
      // 遮罩层
      visible: false,
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
      // 选中岗位
      posts: []
    };
  },
  methods: {
    checkPermit,
    /** 打开 */
    show() {
      this.getList();
      this.visible = true;
    },
    /** 列表数据 */
    getList() {
      this.queryParams.deptId = this.deptId;
      getUnConfiguredPosts(this.queryParams).then(res => {
        this.list = res.data.list;
        this.total = res.data.total;
      });
    },
    /** 多选框 */
    selectRow(selection) {
      this.posts = selection.map(item => ({
        deptId: this.deptId,
        postId: item.postId,
        isDefault: item.isDefault
      }))
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    handleReset() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 修改默认岗位 */
    handleDefaultPost(val, row) {
      const index = this.posts.findIndex(v=>v.postId === row.postId)
      if(index !== -1) {
        this.posts[index].isDefault = val
      }
    },
    /** 提交 */
    handleSubmit() {
      if (this.posts.length === 0) {
        this.$modal.msgError(this.$t('post.text.unselect'));
        return;
      }
      addDeptPosts(this.posts).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
        this.visible = false;
        this.$emit("ok");
      });
    }
  }
};
</script>
