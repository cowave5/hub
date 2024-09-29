<template>
  <el-dialog :title="$t('post.button.select')" :visible.sync="visible" width="900px" top="5vh" append-to-body>
    <!-- 筛选栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item :label="$t('user.label.name')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('user.placeholder.name')" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('user.label.phone')" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t('user.placeholder.phone')" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{$t('commons.button.search')}}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  列表数据  -->
    <el-row>
      <el-table ref="table" :data="list" @selection-change="selectRow" :header-cell-style="{'text-align':'center'}" height="560px">
        <el-table-column type="selection" width="55"/>
        <el-table-column :label="$t('user.label.name')" prop="userName" align="center"/>
        <el-table-column :label="$t('user.label.phone')" prop="userPhone" align="center"/>
        <el-table-column :label="$t('user.label.rank')" prop="userRank" align="center" :show-overflow-tooltip="true">
          <template slot-scope="{row: {userRank}}">
            <template v-for="item in dict.type.post_level">
              <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
            </template>
          </template>
        </el-table-column>
        <el-table-column :label="$t('user.label.post')" prop="postId" align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.postId">
              <el-option v-for="item in posts" :key="item.postId" :value="item.postId" :label="item.postName"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('dept.text.default_user_post')" prop="isDefault" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.isDefault"
                       :active-value=1 :inactive-value=0 @change="(val)=>handleDefaultPost(val, scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('dept.text.leader')" prop="isLeader" align="center" width="150">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.isLeader"
                       :active-value=1 :inactive-value=0 @change="(val)=>handleDeptLeader(val, scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSubmit" :disabled="!checkPermit(['sys:dept:members'])">
        {{$t('commons.button.confirm')}}
      </el-button>
      <el-button @click="visible = false">{{$t('commons.button.cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import { addDeptMembers, getConfiguredPosts, getUnJoinedMembers } from '@/api/system/dept'
export default {
  dicts: ['post_level'],
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
        userName: undefined,
        userPhone: undefined
      },
      // 分页数据
      list: [],
      // 分页总数
      total: 0,
      // 岗位选项
      posts: [],
      // 选中人员
      users: []
    };
  },
  methods: {
    checkPermit,
    /** 打开 */
    show() {
      getConfiguredPosts({ deptId: this.deptId, pageSize: 100 }).then(resp => {
        this.posts = resp.data.list
      });
      this.getList();
      this.visible = true;
    },
    /** 列表数据 */
    getList() {
      this.queryParams.deptId = this.deptId;
      getUnJoinedMembers(this.queryParams).then(res => {
        this.list = res.data.list;
        this.total = res.data.total;
        // 岗位默认选择
        const defaultPost = this.posts.find(item => item.isDefault === 1);
        if (defaultPost) {
          this.list.forEach(item => {
            item.postId = defaultPost.postId;
          });
        }
      });
    },
    /** 多选框 */
    selectRow(selection) {
      this.users = selection.map(item => ({
        deptId: this.deptId,
        userId: item.userId,
        postId: item.postId,
        isDefault: item.isDefault,
        isLeader: item.isLeader
      }))
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 修改用户默认岗位 */
    handleDefaultPost(val, row) {
      const index = this.users.findIndex(v=>v.userId === row.userId)
      if(index !== -1) {
        this.users[index].isDefault = val
      }
    },
    /** 修改部门负责人 */
    handleDeptLeader(val, row) {
      const index = this.users.findIndex(v=>v.userId === row.userId)
      if(index !== -1) {
        this.users[index].isLeader = val
      }
    },
    /** 提交 */
    handleSubmit() {
      if (this.users.length === 0) {
        this.$modal.msgError(this.$t('user.text.unselect'));
        return;
      }
      addDeptMembers(this.users).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
        this.visible = false;
        this.$emit("ok");
      });
    }
  }
};
</script>
