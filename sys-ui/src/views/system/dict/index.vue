<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t(`dict.label.group`)" prop="groupCode">
        <el-select v-model="queryParams.groupCode" @change="handleGroupChange">
          <el-option v-for="item in groupOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t(`dict.label.type`)" prop="typeCode">
        <el-select v-model="queryParams.typeCode" @change="handleTypeChange">
          <el-option v-if="typeOptions" v-for="item in typeOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:dict:new'])">{{$t('route.system.dict.new')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:dict:edit'])">{{$t('route.system.dict.edit')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:dict:delete'])">{{$t('route.system.dict.delete')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:dict:export'])">{{$t('route.system.dict.export')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:dict:cache'])">{{$t('route.system.dict.cache')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
      <el-table-column :selectable='selectable' type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.group`)" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="$i18n.locale==='zh'">{{scope.row.groupName}}</span>
          <span v-if="$i18n.locale==='en'">{{scope.row.groupEn}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.groupcode`)" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.groupCode === 'dict_root' || scope.row.groupCode === 'Root'" style="font-weight: bold">{{scope.row.groupCode}}</span>
          <span v-else-if="scope.row.groupCode === 'dict_group'" style="color: #cc7e15">{{scope.row.groupCode}}</span>
          <span v-else style="color: #b91111">{{scope.row.groupCode}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.type`)" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="$i18n.locale==='zh'">{{scope.row.typeName}}</span>
          <span v-if="$i18n.locale==='en'">{{scope.row.typeEn}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.typecode`)" align="center" prop="typeCode" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.typeCode === 'dict_root'" style="font-weight: bold">{{scope.row.typeCode}}</span>
          <span v-else-if="scope.row.groupCode === 'dict_root'" style="color: #cc7e15">{{scope.row.typeCode}}</span>
          <span v-else-if="scope.row.groupCode === 'dict_group'" style="color: #b91111">{{scope.row.typeCode}}</span>
          <span v-else style="color: #4b18a6">{{scope.row.typeCode}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.name`)" align="center">
        <template slot-scope="scope">
          <span v-if="$i18n.locale==='zh' && scope.row.css === null" >{{scope.row.dictLabel}}</span>
          <el-tag v-if="$i18n.locale==='zh' && scope.row.css !== null" :type="scope.row.css">{{scope.row.dictLabel}}</el-tag>
          <span v-if="$i18n.locale==='en' && scope.row.css === null" >{{scope.row.dictEn}}</span>
          <el-tag v-if="$i18n.locale==='en' && scope.row.css !== null" :type="scope.row.css">{{scope.row.dictEn}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.code`)" align="center" prop="dictCode" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.dictCode === 'dict_group'" style="color: #cc7e15">{{scope.row.dictCode}}</span>
          <span v-else-if="scope.row.groupCode === 'dict_root'" style="color: #b91111">{{scope.row.dictCode}}</span>
          <span v-else-if="scope.row.groupCode === 'dict_group'" style="color: #4b18a6">{{scope.row.dictCode}}</span>
          <span v-else>{{scope.row.dictCode}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`dict.label.value`)" align="center" prop="dictValue" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t(`dict.label.order`)" align="center" prop="dictOrder" />
      <el-table-column :label="$t(`label.status`)" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_enable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.readonly`)" align="center" prop="readOnly">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"
                     :disabled="scope.row.dictCode === 'dict_group' || !checkPermit(['route.common.readonly'])"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.dict.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:dict:delete'])">{{$t('route.system.dict.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.group`)" prop="groupCode">
              <el-select v-model="form.groupCode" style="width: 230px" :disabled="optiondisable === true" @change="handleOptionChange">
                <el-option v-for="item in groupOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.type`)" prop="typeCode">
              <el-select v-model="form.typeCode" style="width: 230px" :disabled="optiondisable === true">
                <el-option v-if="editOptions" v-for="item in editOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.name`)" prop="dictLabel">
              <el-input v-model="form.dictLabel" :placeholder="$t(`dict.placeholder.name`)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.english`)" prop="dictEn">
              <el-input v-model="form.dictEn" :placeholder="$t(`dict.placeholder.english`)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.code`)" prop="dictCode">
              <el-input v-model="form.dictCode" :placeholder="$t(`dict.placeholder.code`)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.value`)" prop="dictValue">
              <el-input v-model="form.dictValue" :placeholder="$t(`dict.placeholder.value`)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.css`)" prop="css">
              <el-select v-model="form.css" style="width: 230px">
                <el-option v-if="$i18n.locale==='zh'"   v-for="item in cssOptions" :key="item.value" :value="item.value" :label="item.label"/>
                <el-option v-if="$i18n.locale==='en'"   v-for="item in cssOptions" :key="item.value" :value="item.value" :label="item.labelEn"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.order`)" prop="dictOrder">
              <el-input-number v-model="form.dictOrder" controls-position="right" :min="0" style="width: 230px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`label.status`)" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dict.label.default`)" prop="isDefault">
              <el-radio-group v-model="form.isDefault">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="valueParser">
          <span slot="label">
            <el-tooltip :content="$t(`config.content.parser`)" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{$t('config.label.parser')}}
          </span>
          <el-input v-model="form.valueParser" :placeholder="$t(`config.placeholder.parser`)" />
        </el-form-item>
        <el-form-item prop="valueParam">
          <span slot="label">
            <el-tooltip :content="$t(`config.content.param`)" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{ $t('config.label.param') }}
          </span>
          <el-input v-model="form.valueParam" :placeholder="$t(`config.placeholder.param`)"/>
        </el-form-item>
        <el-form-item :label="$t(`label.remark`)" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..."></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:dict:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listType, getType, delType, addType, updateType, refreshCache, changeReadonly } from "@/api/system/dict/type";
import { optionselect as getDictOptionselect } from "@/api/system/dict/type";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Dict",
  dicts: ['sys_is_enable', 'sys_yes_no'],
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
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 字典标签样式
      cssOptions: [
        {value: null, label: "默认", labelEn: "default"},
        {value: "primary", label: "主要", labelEn: "primary"},
        {value: "success", label: "成功", labelEn: "success"},
        {value: "info", label: "信息", labelEn: "info"},
        {value: "warning", label: "警告", labelEn: "warning"},
        {value: "danger", label: "危险", labelEn: "danger"},
      ],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        groupCode: undefined,
        typeCode: undefined
      },
      // 表单参数
      form: {},
      groupOptions: [],
      typeOptions: [],
      editOptions: [],
      optiondisable: true
    };
  },
  created() {
    this.getOptions();
    this.getList();
  },
  computed: {
    rules() {
      return {
        groupCode: [
          { required: true, message: this.$t(`dict.rules.groupcode`), trigger: "blur" }
        ],
        groupName: [
          { required: true, message: this.$t(`dict.rules.groupname`), trigger: "blur" }
        ],
        typeCode: [
          { required: true, message: this.$t(`dict.rules.typecode`), trigger: "blur" }
        ],
        typeName: [
          { required: true, message: this.$t(`dict.rules.typename`), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    selectable(row, rowIndex){
      return row.dictCode !== 'dict_group';
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        groupCode: undefined,
        typeCode: undefined,
        status: 1,
        isDefault: 0,
        dictOrder: 1,
        valueParser: undefined,
        valueParam: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 获取分组类型选项 */
    getOptions() {
      getDictOptionselect().then(response => {
        this.groupOptions = response.data;
      });
    },
    /** 选择分组 */
    handleGroupChange() {
      const selectedOption = this.groupOptions.find(item => item.key === this.queryParams.groupCode);
      if (selectedOption && selectedOption.children) {
        this.typeOptions = selectedOption.children;
        this.queryParams.typeCode = undefined;
      } else {
        this.typeOptions = [];
      }
      this.handleQuery();
    },
    /** 新增弹窗选择分组 */
    handleOptionChange(){
      const selectedOption = this.groupOptions.find(item => item.key === this.form.groupCode);
      if (selectedOption && selectedOption.children) {
        this.editOptions = selectedOption.children;
        this.form.typeCode = undefined;
      } else {
        this.editOptions = [];
      }
    },
    /** 选择分类 */
    handleTypeChange(){
      this.handleQuery();
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listType(this.queryParams).then(response => {
          this.typeList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.optiondisable = false;
      this.open = true;
      this.title = this.$t(`dict.dialog.title_new`);
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      const selectedOption = this.groupOptions.find(item => item.key === row.groupCode);
      this.editOptions = selectedOption.children;
      this.optiondisable = true;
      getType(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t(`dict.dialog.title_edit`);
      });
    },
    /** 删除 */
    handleDelete(row) {
      const typeIds = row.id || this.ids;
      const msg = row.id ? this.$t(`dict.msg.confirm_delete`, { var1: row.dictCode }) : this.$t(`dict.msg.select_delete`);
      this.$modal.confirm(msg).then(function() {
        return delType(typeIds);
      }).then(() => {
        this.getOptions();
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 类型只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1 ? this.$t(`content.set`) : this.$t(`content.cancel`);
      this.$modal.confirm(this.$t(`dict.msg.type_confirm_readonly`, { var1: text, var2: this.$t(row.dictCode) })).then(function() {
        return changeReadonly(row.id, row.readOnly, row.dictCode);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
      });
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/dict/export', {}, this.$t(`dict.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 刷新缓存 */
    handleRefreshCache() {
      refreshCache().then(() => {
        this.$modal.msgSuccess(this.$t(`msg.success_refresh`));
      });
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
          if (this.form.id !== undefined) {
            updateType(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              if(this.form.groupCode === 'dict_root' || this.form.groupCode === 'dict_group'){
                this.getOptions();
              }
              this.getList();
            });
          } else {
            addType(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
              this.open = false;
              if(this.form.groupCode === 'dict_root' || this.form.groupCode === 'dict_group'){
                this.getOptions();
              }
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
