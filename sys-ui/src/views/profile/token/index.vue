<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix" style="display: flex; justify-content: space-between; align-items: center;">
            <el-button type="primary" size="small" @click="submitTokenScope" style="margin-left: auto;">创建</el-button>
          </div>
          <el-form ref="form" :model="token" :rules="rules" label-width="90px">
            <el-form-item label="令牌名称:" prop="tokenName">
              <el-input v-model="token.tokenName" maxlength="50"/>
            </el-form-item>
            <el-form-item label="到期时间:" prop="expire">
              <el-date-picker v-model="token.expire" style="width: 100%"
                              value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="令牌到期时间"/>
            </el-form-item>
            <el-form-item label="ip限制:" prop="ipRule">
              <el-input v-model="token.ipRule" maxlength="50" placeholder="如：10.64.43.0/24,192.168.68.64"/>
            </el-form-item>
            <el-form-item label="访问权限:">
              <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">
                {{ $t('commons.button.expand') }}/{{ $t('commons.button.collapse') }}
              </el-checkbox>
              <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">
                {{ $t('commons.button.check') }}
              </el-checkbox>
              <el-checkbox v-model="menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">
                {{ $t('commons.button.parent') }}
              </el-checkbox>
              <el-tree ref="menuTree" :data="menuOptions" node-key="id" class="tree-border" show-checkbox
                       :check-strictly="!menuCheckStrictly" empty-text="..." :props="defaultProps">
                <span class="el-tree-node__label" slot-scope="{data}"
                      style="display: flex; justify-content: space-between; width: 100%;">
                  {{ $t(data.label) }}
                  <el-select v-if="data.menuType === 'B'" v-model="data.scope"
                             placeholder="选择数据权限" size="small" class="tree-node-select">
                    <el-option v-for="option in dataPermits" :key="option.value" :label="option.label" :value="option.value"/>
                  </el-select>
                </span>
              </el-tree>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>令牌信息</span>
          </div>
          <el-table :data="list">
            <el-table-column label="令牌名称" prop="tokenName" align="center"/>
            <el-table-column label="创建时间" prop="createTime" align="center" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="到期时间" prop="expire" align="center" width="180">
              <template slot-scope="scope">
                <span>{{ scope.row.expire ? parseTime(scope.row.expire) : '永久' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="ip限制" prop="ipRule" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <span>{{ scope.row.ipRule ? scope.row.ipRule : '0.0.0.0' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="最近访问时间" prop="accessTime" align="center" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.accessTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="最近访问ip" prop="accessIp" align="center" :show-overflow-tooltip="true"/>
            <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handlePermit(scope.row)">
                  <svg-icon icon-class="pscope"/>权限
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">
                  {{ $t('commons.button.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="令牌信息" :visible.sync="showToken" width="800px">
      <p style="color: #666; line-height: 1.5;">
        <i class="el-icon-info" style="color: #409EFF;"></i>
        提示：请妥善保管令牌信息，只显示一次
      </p>
      <el-input type="textarea" :rows="10" v-model="tokenValue" readonly style="width: 100%;"/>
      <template #footer>
        <el-button type="primary" @click="closeToken">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="令牌权限" :visible.sync="showPermit" width="800px">
      <el-checkbox v-model="menuExpand2" @change="handleCheckedTreeExpand2($event, 'menu')">
        {{ $t('commons.button.expand') }}/{{ $t('commons.button.collapse') }}
      </el-checkbox>
      <el-tree ref="menuTree2" :data="menuOptions2" node-key="id" class="tree-border" show-checkbox
               :check-strictly="!menuCheckStrictly2" empty-text="..." :props="defaultProps2">
                <span class="el-tree-node__label" slot-scope="{data}"
                      style="display: flex; justify-content: space-between; width: 100%;">
                  {{ $t(data.label) }}
                  <el-select v-if="data.menuType === 'B'" v-model="data.scope"
                             placeholder="选择数据权限" size="small" class="tree-node-select">
                    <el-option v-for="option in dataPermits" :key="option.value" :label="option.label"
                               :value="option.value"/>
                  </el-select>
                </span>
      </el-tree>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProfile, creatApiToken, deleteApiToken, getAuthApiTree, listApiToken } from '@/api/profile'

export default {
  name: "userToken",
  data() {
    return {
      user: {},
      list: [],
      token: {
        tokenName: undefined,
        ipRule: undefined,
        expire: undefined,
        userCode: undefined,
        menuIds: [],
      },
      menuOptions: [],
      menuExpand: false,
      menuNodeAll: false,
      menuCheckStrictly: true,
      defaultProps: {
        children: "children",
        label: "label"
      },
      dataPermits: [
        { value: 'option1', label: '数据权限1' },
        { value: 'option2', label: '数据权限2' }
      ],
      tokenValue: undefined,
      showToken: false,

      showPermit: false,
      menuOptions2: [],
      menuExpand2: false,
      menuCheckStrictly2: true,
      defaultProps2: {
        children: "children",
        label: "label",
        disabled: "disabled"
      },
    };
  },
  created() {
    this.getUser();
    this.getMenuOptions();
    this.getList();
  },
  computed: {
    rules() {
      return {
        tokenName: [
          { required: true, message: "令牌名称不能为空", trigger: "never" }
        ]
      };
    }
  },
  methods: {
    /** 用户信息 */
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    /** 用户权限 */
    getMenuOptions(){
      getAuthApiTree().then(response => {
        this.menuOptions = response.data;
      });
    },
    /** 用户权限 */
    getList(){
      listApiToken().then(response => {
        this.list = response.data;
      });
    },
    /** 创建令牌 */
    submitTokenScope() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.token.userCode = this.user.userCode
          this.token.menuIds = this.getCheckedMenus()
          creatApiToken(this.token).then((resp) => {
            this.getList();
            this.tokenValue = resp.data;
            this.token = {
              tokenName: undefined,
              expire: undefined,
              userCode: undefined,
              menuIds: []
            };
            this.showToken = true;
          })
        }
      })
    },
    handlePermit(row) {
      this.menuOptions2 = this.deepCopyWithDisabled(this.menuOptions);
      this.$nextTick(() => {
        this.$refs.menuTree2.setCheckedKeys(row.menuIds);
      });
      this.showPermit = true;
    },
    deepCopyWithDisabled(nodes) {
      return nodes.map(node => {
        const newNode = {
          ...node,
          disabled: true
        }
        if (node.children && node.children.length > 0) {
          newNode.children = this.deepCopyWithDisabled(node.children)
        }
        return newNode
      })
    },
    /** 关闭令牌 */
    closeToken() {
      this.showToken = false;
    },
    /** 选中的菜单 */
    getCheckedMenus() {
      // 目前被选中的节点
      let checkedKeys = this.$refs.menuTree.getCheckedKeys();
      // 半选中的节点
      let halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      // let checkedMenus = checkedKeys.map(id => {
      //   let node = this.findNodeById(id, this.menuOptions) // 查找对应节点
      //   return {
      //     id: id,
      //     scope: node ? node.scope || '' : '' // 确保 scope 有值
      //   }
      // })
      return checkedKeys;
    },
    /** 展开/折叠 */
    handleCheckedTreeExpand(value, type) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menuTree.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    /** 展开/折叠 */
    handleCheckedTreeExpand2(value, type) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menuTree2.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    /** 全选/全不选 */
    handleCheckedTreeNodeAll(value, type) {
      this.$refs.menuTree.setCheckedNodes(value ? this.menuOptions: []);
    },
    /** 父子联动 */
    handleCheckedTreeConnect(value, type) {
      this.menuCheckStrictly = value ? true: false;
    },
    /** 删除 */
    handleDelete(row){
      const tokenId = row.tokenId;
      this.$modal.confirm("确认删除令牌'" + row.tokenName + "'?").then(function() {
        return deleteApiToken(tokenId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    }
  }
};
</script>
