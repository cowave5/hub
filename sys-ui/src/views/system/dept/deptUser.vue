<template>
  <div class="app-container">
    <h4 class="form-header h4">{{$t('dept.info')}}</h4>
    <el-form ref="form" :model="form" label-width="80px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t(`dept.label.name`)" prop="userAccount">
            <el-input  v-model="form.deptName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t(`dept.label.phone`)" prop="userName">
            <el-input v-model="form.deptPhone" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">{{$t('dept.label.member')}}</h4>
    <el-table v-loading="loading" :row-key="getRowKey" ref="table" @select="selectSingle" :data="list">
      <el-table-column type="selection" :reserve-selection="true" width="50"></el-table-column>
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.name`)" align="center" prop="userName" />
      <el-table-column :label="$t(`dept.user.rank`)" align="center">
        <template slot-scope="{row: {rank}}">
          <template v-for="item in dict.type.post_level">
            <span v-if="rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
            <span v-if="rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.position`)" align="center" prop="postName">
        <template slot-scope="scope">
          <el-select v-model="scope.row.postId" :placeholder="$t(`user.placeholder.role`)">
            <el-option v-for="item in postOptions" :key="item.postId" :label="item.postName" :value="item.postId"/>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.default`)" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isDefault" :active-value=1 :inactive-value=0 @change="(val)=>handleDefaultChange(val,scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dept.user.leader`)" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isLeader" :active-value=1 :inactive-value=0 @change="(val)=>handleLeaderChange(val,scope.row)"/>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button type="primary" @click="submitForm()"
                   :disabled="!checkPermit(['sys:dept:members'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="close()">{{$t('button.cancel')}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {listUser} from "@/api/system/user";
import {getDept, getDeptPosts, getDeptUsersById, setDeptUsers} from "@/api/system/dept";
import {checkPermit} from "@/utils/permission";

export default {
  name: "DeptPost",
  dicts: ['post_type', 'post_level'],
  data() {
    return {
       // 遮罩层
      loading: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10
      },
      // 列表数据
      list: [],
      // 分页总数
      total: 0,
      // 选中数据
      chooseRows:[],
      // 对象id
      infoId: undefined,
      // 对象信息
      form: {},
      // 岗位选项
      postOptions: [],
      // 默认岗位
      defaultPostId: undefined
    };
  },
  created() {
    this.infoId = this.$route.params && this.$route.params.deptId;
    if (this.infoId) {
      getDept(this.infoId).then((resp) => {
        this.form = resp.data;
      });
      getDeptPosts(this.infoId).then((resp) => {
        this.postOptions = resp.data;
        this.postOptions.forEach((post) => {
          if(post.isDefault === 1){
            this.defaultPostId = post.postId;
          }
        });
      });
      getDeptUsersById(this.infoId).then((resp) => {
        this.chooseRows = resp.data;
      });
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    checkPermit,
    selectRow() {
      if (this.infoId) {
        this.$nextTick(()=>{
          this.list.forEach((row) => {
            const index = this.chooseRows.findIndex(v=>v.userId === row.userId);
            if(index !== -1){
              this.$refs.table.toggleRowSelection(row, true);
              row.isLeader = this.chooseRows[index].isLeader;
              row.isDefault = this.chooseRows[index].isDefault;
            }
          });
        })
      }
    },
    /** 列表数据 */
    getList() {
      listUser(this.queryParams).then((resp) => {
        this.list = resp.data.list
        this.total = resp.data.total;
        if(this.defaultPostId !== undefined){
          this.list.forEach((row) => {
            row.postId = this.defaultPostId;
          });
        }
        this.selectRow()
      });
    },
    /** 选中岗位变化 */
    selectSingle(selection,row) {
      if(selection.findIndex(v=>v.userId === row.userId) !== -1) {
        if(this.chooseRows.findIndex(v=>v.userId === row.userId) === -1) this.chooseRows.push(row)
      } else {
        const index = this.chooseRows.findIndex(v=>v.userId === row.userId)
        this.chooseRows.splice(index, 1)
      }
    },
    /** 单击选中行 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    /** 保存选中的数据编号 */
    getRowKey(row) {
      return row.postId;
    },
    /** 提交 */
    submitForm() {
      this.chooseRows.forEach((row) => {
        row.deptId = this.infoId;
      });
      setDeptUsers(this.chooseRows).then((response) => {
        this.$modal.msgSuccess(this.$t(`msg.success_edit`));
        this.close();
      });
    },
    /** 关闭 */
    close() {
      const obj = { path: "/system/dept" };
      this.$tab.closeOpenPage(obj);
    },
    /** 设置部门负责人 */
    handleLeaderChange(val, row) {
      const index = this.chooseRows.findIndex(v=>v.userId === row.userId)
      if(index !== -1) {
        this.chooseRows[index].isLeader = val;
      }
    },
    /** 设置用户默认岗位 */
    handleDefaultChange(val, row) {
      const index = this.chooseRows.findIndex(v=>v.userId === row.userId)
      if(index !== -1) {
        this.chooseRows[index].isDefault = val;
      }
    },
  }
};
</script>
