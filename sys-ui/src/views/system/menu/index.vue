<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('menu.label.name')" prop="menuName">
        <el-input v-model="queryParams.menuName" :placeholder="$t('menu.placeholder.name')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('menu.label.status')" prop="status">
        <el-select v-model="queryParams.menuStatus" :placeholder="$t('menu.placeholder.status')" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.enable_disable" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{$t('commons.button.search')}}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
          {{$t('commons.button.expand')}}/{{$t('commons.button.collapse')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:menu:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:menu:export'])">
          {{$t('commons.button.export')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
    </el-row>

    <!--  列表数据  -->
    <el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="menuId"
      :default-expand-all="isExpandAll" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column v-if="cols[0].show" prop="menuName" :label="$t('menu.label.name')" :show-overflow-tooltip="true" width="200">
        <template slot-scope="scope">
          <span>{{$t(scope.row.menuName)}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[1].show" prop="menuType" :label="$t('menu.label.type')" align="center" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.menu_type" :value="scope.row.menuType"/>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[2].show" prop="menuOrder" :label="$t('menu.label.order')" align="center" width="100"/>
      <el-table-column v-if="cols[3].show" prop="menuIcon" :label="$t('menu.label.icon')" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.menuIcon" />
        </template>
      </el-table-column>
      <el-table-column v-if="cols[4].show" prop="isProtected" :label="$t('menu.label.visibility')" align="center" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.public_protected" :value="scope.row.isProtected"/>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[5].show" prop="menuPath" :label="$t('menu.label.path')" align="center" :show-overflow-tooltip="true"/>
      <el-table-column v-if="cols[6].show" prop="menuPermit" :label="$t('menu.label.permission')" align="center" :show-overflow-tooltip="true"/>
      <el-table-column v-if="cols[7].show" prop="component" :label="$t('menu.label.component')" align="center" :show-overflow-tooltip="true"/>
      <el-table-column v-if="cols[8].show" prop="menuStatus" :label="$t('menu.label.status')" align="center" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.enable_disable" :value="scope.row.menuStatus"/>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[9].show" prop="createTime" :label="$t('commons.label.createTime')" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[10].show" :label="$t('commons.label.updateTime')" prop="updateTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     :disabled="!checkPermit(['sys:menu:create'])">
            {{$t('commons.button.create')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:menu:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('menu.label.parent')">
              <treeselect v-model="form.parentId" :options="menuOptions"
                          :normalizer="normalizer" :show-count="true" :placeholder="$t('menu.placeholder.parent')"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('menu.label.type')" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio v-for="dict in dict.type.menu_type" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('menu.label.name')" prop="menuName">
              <span slot="label">
                <el-tooltip :content="$t('menu.content.name')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.name')}}
              </span>
              <el-input v-model="form.menuName" :placeholder="$t('menu.placeholder.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('menu.label.order')" prop="menuOrder">
              <el-input-number v-model="form.menuOrder" controls-position="right" :min=0 style="width: 220px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item :label="$t('menu.label.icon')" prop="menuIcon">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.menuIcon" :placeholder="$t('menu.placeholder.icon')" readonly>
                  <svg-icon v-if="form.menuIcon" slot="prefix" :icon-class="form.menuIcon" class="el-input__icon" style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.status')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.status')}}
              </span>
              <el-radio-group v-model="form.menuStatus">
                <el-radio v-for="dict in dict.type.enable_disable" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.frame')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.frame')}}
              </span>
              <el-radio-group v-model="form.isFrame">
                <el-radio v-for="dict in dict.type.menu_frame" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.visible')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visible')}}
              </span>
              <el-radio-group v-model="form.isVisible">
                <el-radio v-for="dict in dict.type.show_hide" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType === 'M'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.visibility')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visibility')}}
              </span>
              <el-radio-group v-model="form.isProtected">
                <el-radio v-for="dict in dict.type.public_protected" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item prop="menuPath">
              <span slot="label">
                <el-tooltip :content="$t('menu.content.frame')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.path')}}
              </span>
              <el-input v-model="form.menuPath" :placeholder="$t('menu.placeholder.path')" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.cacheable')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.cacheable')}}
              </span>
              <el-radio-group v-model="form.isCache">
                <el-radio v-for="dict in dict.type.menu_cache" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item>
              <el-input v-model="form.menuParam" :placeholder="$t('menu.placeholder.param')" maxlength="255" />
              <span slot="label">
                <el-tooltip :content="$t('menu.content.param')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.param')}}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip :content="$t('menu.content.component')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.component')}}
              </span>
              <el-input v-model="form.component" :placeholder="$t('menu.placeholder.component')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'M'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.visibility')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visibility')}}
              </span>
              <el-radio-group v-model="form.isProtected">
                <el-radio v-for="dict in dict.type.public_protected" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'M'">
            <el-form-item>
              <el-input v-model="form.menuPermit" :placeholder="$t('menu.placeholder.permission')" maxlength="100"/>
              <span slot="label">
                <el-tooltip :content="$t('menu.content.permission')" placement="top">
                  <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.permission')}}
              </span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:menu:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getMenuList, getMenuInfo, delMenu, addMenu, updateMenu } from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import IconSelect from "@/components/IconSelect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Menu",
  dicts: ['show_hide', 'enable_disable', 'public_protected', 'menu_frame', 'menu_cache', 'menu_type'],
  components: { Treeselect, IconSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        menuName: undefined,
        menuStatus: undefined
      },
      // 表单参数
      form: {},
      cols: [
        {key: 0, label: 'menu.label.name', show: true},
        {key: 1, label: 'menu.label.type', show: true},
        {key: 2, label: 'menu.label.order', show: true},
        {key: 3, label: 'menu.label.icon', show: true},
        {key: 4, label: 'menu.label.visibility', show: true},
        {key: 5, label: 'menu.label.path', show: true},
        {key: 6, label: 'menu.label.permission', show: true},
        {key: 7, label: 'menu.label.component', show: true},
        {key: 8, label: 'menu.label.status', show: true},
        {key: 9, label: 'commons.label.createTime', show: false},
        {key: 10, label: 'commons.label.updateTime', show: false},
      ],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        menuName: [
          { required: true, message: this.$t('menu.rules.name'), trigger: "blur" }
        ],
        menuOrder: [
          { required: true, message: this.$t('menu.rules.order'), trigger: "blur" }
        ],
        menuPath: [
          { required: true, message: this.$t('menu.rules.path'), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: this.$t(node.menuName),
        children: node.children
      };
    },
    /** 上级菜单选择 */
    getTreeselect() {
      getMenuList().then(response => {
        this.menuOptions = [];
        const menu = { menuId: 0, menuName: this.$t('menu.label.root'), children: [] };
        menu.children = this.handleTree(response.data.list, "menuId");
        this.menuOptions.push(menu);
      });
    },
    /** 选择图标 */
    selected(name) {
      this.form.menuIcon = name;
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      getMenuList(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data.list, "menuId");
        this.loading = false;
      });
    },
    /** 新增 */
    handleAdd(row) {
      this.form = {
        menuId: undefined,
        menuName: undefined,
        menuPath: undefined,
        menuIcon: undefined,
        menuType: "M",
        parentId: 0,
        menuOrder: 1,
        menuStatus: undefined,
        isFrame: 1,
        isCache: 1,
        isVisible: 1,
        isProtected: 1
      };
      this.title = this.$t('menu.dialog.new');
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId;
      } else {
        this.form.parentId = 0;
      }
      this.form.menuStatus = 1;
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.getTreeselect();
      getMenuInfo(row.menuId).then(response => {
        this.title = this.$t('menu.dialog.edit');
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      this.$modal.confirm(this.$t('menu.confirm.delete', { arg1: this.$t(row.menuName) })).then(function() {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/menu/export', {}, this.$t('menu.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId !== undefined) {
            updateMenu(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
