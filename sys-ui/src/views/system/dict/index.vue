<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t('dict.label.name')" prop="dictName">
        <el-input v-model="queryParams.dictName" :placeholder="$t('dict.placeholder.name')"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('dict.label.code')" prop="dictCode">
        <el-input v-model="queryParams.dictCode" :placeholder="$t('dict.placeholder.code')"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
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
                   :disabled="!checkPermit(['sys:dict:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:dict:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:dict:export'])">
          {{$t('commons.button.export')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:dict:cache'])">
          {{$t('dict.button.reset')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
    </el-row>

    <!--  列表数据  -->
    <el-table v-if="refreshTable" :data="list" row-key="dictCode" @selection-change="handleSelectionChange"
              :default-expand-all="isExpandAll" v-loading="loading"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column  type="selection" width="55" align="center" />
      <el-table-column v-if="cols[0].show" :label="$t('dict.label.name')" align="left">
        <template slot-scope="scope">
          <span v-if="scope.row.css === null" >{{$t(scope.row.dictName)}}</span>
          <el-tag v-if="scope.row.css !== null" :type="scope.row.css">{{$t(scope.row.dictName)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[1].show" :label="$t('dict.label.code')" align="center" prop="dictCode"/>
      <el-table-column v-if="cols[2].show" :label="$t('dict.label.value')" align="center" prop="dictValue"/>
      <el-table-column v-if="cols[3].show" :label="$t('dict.label.valueType')" align="center" prop="valueType"/>
      <el-table-column v-if="cols[4].show" :label="$t('dict.label.order')" align="center" prop="dictOrder"/>

      <el-table-column v-if="cols[5].show" :label="$t('dict.label.css')" align="center" prop="css"/>
      <el-table-column v-if="cols[6].show" :label="$t('commons.label.status')" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.enable_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[7].show" :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[8].show" :label="$t('commons.label.updateTime')" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     v-if="scope.row.groupCode === 'root' || scope.row.groupCode === 'group'">
            {{$t('commons.button.create')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:dict:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('dict.label.parent')">
              <el-input :value="`${$t(form.typeName)} | ${form.typeCode}`" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.name')" prop="dictName">
              <el-input v-model="form.dictName" :placeholder="$t('dict.placeholder.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.code')" prop="dictCode">
              <el-input v-model="form.dictCode" :placeholder="$t('dict.placeholder.code')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.value')" prop="dictValue">
              <el-input v-model="form.dictValue" :placeholder="$t('dict.placeholder.value')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.css')" prop="css">
              <el-select v-model="form.css" style="width: 100%;">
                <el-option v-for="item in cssOptions" :key="item.value" :value="item.value" :label="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.valueType')" prop="valueType">
              <el-select v-model="form.valueType" style="width: 100%;">
                <el-option v-for="item in valueOptions" :key="item.value" :value="item.value" :label="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.order')" prop="dictOrder">
              <el-input-number v-model="form.dictOrder" controls-position="right" :min="0" style="width: 100%;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="valueParser">
          <span slot="label">
            <el-tooltip :content="$t('dict.text.parser')" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{$t('dict.label.parser')}}
          </span>
          <el-input v-model="form.valueParser" :placeholder="$t('dict.placeholder.parser')" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('commons.label.status')" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.enable_disable" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dict.label.default')" prop="isDefault">
              <el-radio-group v-model="form.isDefault">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">
                  {{$t(dict.name)}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('commons.label.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..."></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:dict:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {checkPermit} from "@/utils/permission";
import {
  addDict,
  delDict,
  getDictInfo,
  getDictList,
  refreshDict,
  updateDict,
} from '@/api/system/dict'

export default {
  name: "Dict",
  dicts: ['enable_disable', 'yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 是否展开
      isExpandAll: false,
      // 总条数
      total: 0,
      // 字典表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 样式
      cssOptions: [
        {value: "primary"},
        {value: "success"},
        {value: "info"},
        {value: "warning"},
        {value: "danger"},
      ],
      // 类型
      valueOptions: [
        {value: "int32"},
        {value: "int64"},
        {value: "bool"},
        {value: "float"},
        {value: "double"},
        {value: "decimal"},
        {value: "datetime"},
        {value: "custom"},
      ],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        dictName: undefined,
        dictCode: undefined
      },
      // 表单参数
      form: {},
      cols: [
        {key: 0, label: 'dict.label.name', show: true},
        {key: 1, label: 'dict.label.code', show: true},
        {key: 2, label: 'dict.label.value', show: true},
        {key: 3, label: 'dict.label.valueType', show: true},
        {key: 4, label: 'dict.label.order', show: true},
        {key: 5, label: 'dict.label.css', show: true},
        {key: 6, label: 'commons.label.status', show: true},
        {key: 7, label: 'commons.label.createTime', show: false},
        {key: 8, label: 'commons.label.updateTime', show: false},
      ],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        groupCode: [
          { required: true, message: this.$t('dict.rules.groupcode'), trigger: "blur" }
        ],
        typeCode: [
          { required: true, message: this.$t('dict.rules.typecode'), trigger: "blur" }
        ],
        dictName: [
          { required: true, message: this.$t('dict.rules.name'), trigger: "blur" }
        ],
        dictCode: [
          { required: true, message: this.$t('dict.rules.code'), trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: this.$t('dict.rules.value'), trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 展开/折叠 */
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
      getDictList(this.queryParams).then(response => {
          this.list = this.handleTree(response.data, "dictCode", "typeCode");
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd(row) {
      this.form = {
        id: undefined,
        groupCode: undefined,
        typeCode: undefined,
        valueParser: undefined,
        valueType: undefined,
        remark: undefined,
        status: 1,
        isDefault: 0,
        dictOrder: 1,
      };
      this.title = this.$t('dict.dialog.new');
      this.form.typeCode = row.dictCode ? row.dictCode : "group";
      this.form.typeName = row.dictName ? row.dictName : this.$t('dict.name.root');
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const id = row.id || this.ids;
      this.title = this.$t('dict.dialog.edit');
      getDictInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const dictIds = row.id || this.ids;
      const msg = row.id
          ? this.$t('dict.confirm.delete', { arg1: row.dictCode })
          : this.$t('dict.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delDict(dictIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/dict/export', {}, this.$t('dict.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 刷新缓存 */
    handleRefreshCache() {
      refreshDict().then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.reset'));
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateDict(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addDict(this.form).then(() => {
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
