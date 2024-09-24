<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t(`menu.label.name`)" prop="menuName">
        <el-input v-model="queryParams.menuName" :placeholder="$t(`menu.placeholder.name`)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`menu.label.status`)" prop="status">
        <el-select v-model="queryParams.menuStatus" :placeholder="$t(`user.placeholder.status`)" clearable style="width: 240px">
          <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.label" :value="dict.value"/>
          <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.labelEn" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
                   {{$t('button.expand')}}/{{$t('button.collapse')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:menu:new'])">{{$t('route.system.menu.new')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:menu:export'])">{{$t('route.system.menu.export')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="menuId"
      :default-expand-all="isExpandAll" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="menuName" :label="$t(`menu.label.name`)" :show-overflow-tooltip="true" width="200">
        <template slot-scope="scope">
          <span>{{$t(scope.row.menuName)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="menuType" :label="$t(`menu.label.type`)" align="center" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.menu_type" :value="scope.row.menuType"/>
        </template>
      </el-table-column>
      <el-table-column prop="menuOrder" :label="$t(`menu.label.order`)" align="center" width="90"/>
      <el-table-column prop="menuIcon" :label="$t(`menu.label.icon`)" align="center" width="90">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.menuIcon" />
        </template>
      </el-table-column>
      <el-table-column prop="isProtected" :label="$t(`menu.label.visibility`)" align="center" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_protected" :value="scope.row.isProtected"/>
        </template>
      </el-table-column>
      <el-table-column prop="menuPermit" :label="$t(`menu.label.permission`)" align="left" :show-overflow-tooltip="true"/>
      <el-table-column prop="component" :label="$t(`menu.label.component`)" align="center" :show-overflow-tooltip="true"/>
      <el-table-column prop="menuStatus" :label="$t(`menu.label.status`)" align="center" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_enable" :value="scope.row.menuStatus"/>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t(`label.date_create`)" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.readonly`)" align="center" prop="readOnly">
        <template slot-scope="scope">
          <el-switch :disabled="!checkPermit(['route.common.readonly'])" v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.menu.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     :disabled="!checkPermit(['sys:menu:new'])">{{$t('route.system.menu.new')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:menu:delete'])">{{$t('route.system.menu.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t(`menu.label.parent`)">
              <treeselect v-model="form.parentId" :options="menuOptions"
                          :normalizer="normalizer" :show-count="true" :placeholder="$t(`menu.placeholder.parent`)"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t(`menu.label.type`)" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.menu_type" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.menu_type" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`menu.label.name`)" prop="menuName">
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.name`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.name')}}
              </span>
              <el-input v-model="form.menuName" :placeholder="$t(`menu.placeholder.name`)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`menu.label.order`)" prop="menuOrder">
              <el-input-number v-model="form.menuOrder" controls-position="right" :min=0 style="width: 220px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item :label="$t(`menu.label.icon`)" prop="menuIcon">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.menuIcon" :placeholder="$t(`menu.placeholder.icon`)" readonly>
                  <svg-icon v-if="form.menuIcon" slot="prefix" :icon-class="form.menuIcon"
                            class="el-input__icon" style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.status`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.status')}}
              </span>
              <el-radio-group v-model="form.menuStatus">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.frame`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.frame')}}
              </span>
              <el-radio-group v-model="form.isFrame">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.menu_frame" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.menu_frame" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.visiable`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visiable')}}
              </span>
              <el-radio-group v-model="form.isVisible">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_show_hide" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_show_hide" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType === 'M'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.visibility`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visibility')}}
              </span>
              <el-radio-group v-model="form.isProtected">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_protected" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_protected" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'B'">
            <el-form-item prop="menuPath">
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.frame`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.path')}}
              </span>
              <el-input v-model="form.menuPath" :placeholder="$t(`menu.placeholder.path`)" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.cacheable`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.cacheable')}}
              </span>
              <el-radio-group v-model="form.isCache">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.menu_cache" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.menu_cache" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item>
              <el-input v-model="form.menuParam" :placeholder="$t(`menu.placeholder.param`)" maxlength="255" />
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.param`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.param')}}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.component`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.component')}}
              </span>
              <el-input v-model="form.component" :placeholder="$t(`menu.placeholder.component`)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="form.menuType !== 'M'">
            <el-form-item>
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.visibility`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.visibility')}}
              </span>
              <el-radio-group v-model="form.isProtected">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_protected" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_protected" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType !== 'M'">
            <el-form-item>
              <el-input v-model="form.menuPermit" :placeholder="$t(`menu.placeholder.permission`)" maxlength="100" />
              <span slot="label">
                <el-tooltip :content="$t(`menu.content.permission`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('menu.label.permission')}}
              </span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:menu:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu, changeReadonly } from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import IconSelect from "@/components/IconSelect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Menu",
  dicts: ['sys_show_hide', 'sys_is_enable', 'sys_is_protected', 'menu_frame', 'menu_cache', 'menu_type'],
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
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        menuName: [
          { required: true, message: this.$t(`menu.rules.name`), trigger: "blur" }
        ],
        menuOrder: [
          { required: true, message: this.$t(`menu.rules.order`), trigger: "blur" }
        ],
        menuPath: [
          { required: true, message: this.$t(`menu.rules.path`), trigger: "blur" }
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
        // label: node.menuName,
        label: this.$t(node.menuName),
        children: node.children
      };
    },
    /** 上级菜单选择 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = { menuId: 0, menuName: this.$t(`menu.label.root`), children: [] };
        menu.children = this.handleTree(response.data.list, "menuId");
        this.menuOptions.push(menu);
      });
    },
    /** 选择图标 */
    selected(name) {
      this.form.menuIcon = name;
    },
    /** 表单重置 */
    reset() {
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
      this.resetForm("form");
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
      this.queryParams = {};
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data.list, "menuId");
        this.loading = false;
      });
    },
    /** 新增 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      this.title = this.$t(`menu.dialog.title_new`);
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
      this.reset();
      this.getTreeselect();
      getMenu(row.menuId).then(response => {
        this.title = this.$t(`menu.dialog.title_edit`);
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      this.$modal.confirm(this.$t(`menu.msg.confirm_delete`, { var1: this.$t(row.menuName) })).then(function() {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 菜单只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1 ? this.$t(`content.set`) : this.$t(`content.cancel`);
      this.$modal.confirm(this.$t(`menu.msg.confirm_readonly`, { var1: text, var2: this.$t(row.menuName) })).then(function() {
        return changeReadonly(row.menuId, row.readOnly, row.menuName);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
      });
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/menu/export', {}, this.$t(`menu.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId !== undefined) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
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
