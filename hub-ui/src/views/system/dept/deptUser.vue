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
    <h4 class="form-header h4">{{$t('user.text.list')}}</h4>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('user.label.name')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('user.placeholder.name')" clearable
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('user.label.phone')" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t('user.placeholder.phone')" clearable
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddMember">
          {{ $t('user.button.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-circle-close" size="mini" @click="handleRemoveMember"
                   :disabled="multiple || !checkPermit(['hub:dept:members:remove'])">
          {{ $t('user.button.remove') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">
          {{ $t('commons.button.close') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!--  列表数据  -->
    <el-table ref="table" :data="list" @selection-change="selectRow" v-loading="loading">
      <el-table-column type="selection" width="50"/>
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('user.label.name')" align="center" prop="userName"/>
      <el-table-column :label="$t('user.label.phone')" align="center" prop="userPhone"/>
      <el-table-column :label="$t('user.label.rank')" align="center">
        <template slot-scope="{row: {userRank}}">
          <template v-for="item in dict.type.post_level">
            <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('user.label.post')" align="center" prop="postName"/>
      <el-table-column :label="$t('dept.text.default_user_post')" align="center">
        <template slot-scope="{row: {isDefault}}">
          <template v-for="item in yes_no">
            <span v-if="isDefault === item.value">{{ $t(item.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dept.text.leader')" align="center">
        <template slot-scope="{row: {isLeader}}">
          <template v-for="item in yes_no">
            <span v-if="isLeader === item.value">{{ $t(item.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="handleRemoveMember(scope.row)"
                     :disabled="!checkPermit(['hub:dept:members:remove'])">
            {{ $t('user.button.remove') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <select-user ref="select" :dept-id="deptId" @ok="handleQuery"/>
  </div>
</template>

<script>
import { getDeptInfo, getJoinedMembers, removeDeptMembers } from '@/api/system/dept'
import { checkPermit } from '@/utils/permission'
import selectUser from '@/views/system/dept/selectUser.vue'
import { yes_no } from '@/utils/constants';
export default {
  name: 'DeptPost',
  components: { selectUser },
  dicts: ['post_level'],
  data() {
    return {
      yes_no: yes_no,
      // 遮罩层
      loading: false,
      // 非多个禁用
      multiple: true,
      // 显示筛选栏
      showSearch: true,
      // 筛选参数
      queryParams: {
        deptId: undefined,
        userName: undefined,
        userPhone: undefined,
        page: 1,
        pageSize: 10
      },
      // 分页数据
      list: [],
      // 分页总数
      total: 0,
      // 部门id
      deptId: undefined,
      // 部门信息
      deptInfo: {},
      // 选中成员
      userIds: []
    }
  },
  created() {
    this.deptId = this.$route.params && this.$route.params.deptId
    if (this.deptId) {
      getDeptInfo(this.deptId).then((resp) => {
        this.deptInfo = resp.data
      });
      this.getList()
    }
  },
  methods: {
    checkPermit,
    /** 成员列表 */
    getList() {
      this.queryParams.deptId = this.deptId;
      getJoinedMembers(this.queryParams).then((resp) => {
        this.list = resp.data.list
        this.total = resp.data.total
      })
    },
    /** 选中行 */
    selectRow(selection) {
      this.userIds = selection.map(item => item.userId);
      this.multiple = !selection.length;
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    handleReset() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 关闭 */
    handleClose() {
      const obj = { path: '/system/dept' }
      this.$tab.closeOpenPage(obj)
    },
    /** 添加成员 */
    handleAddMember() {
      this.$refs.select.show();
    },
    /** 移除成员 */
    handleRemoveMember(row) {
      const userIds = row.userId || this.userIds;
      const deptId = this.deptId;
      const msg = row.userId
          ? this.$t('user.confirm.remove', { arg1: row.userName })
          : this.$t('user.confirm.remove_select');
      this.$modal.confirm(msg).then(function() {
        return removeDeptMembers(deptId, userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch((e) => {console.log(e)});
    }
  }
}
</script>
