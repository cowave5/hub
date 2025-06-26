<template>
  <div class="component-upload-image">
    <el-upload
      multiple
      name="file"
      list-type="picture-card"
      :limit="limit"
      :headers="headers"
      :data="attachData"
      :file-list="fileList"
      :action="uploadImgUrl"
      :show-file-list="true"
      :before-upload="handleBeforeUpload"
      :on-preview="handlePictureCardPreview"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-remove="handleRemove"
      :class="{hide: this.fileList.length >= this.limit}"
    >
      <i class="el-icon-plus" />
    </el-upload>

    <!-- 上传提示 -->
    <div class="el-upload__tip" slot="tip" v-if="showTip">
      <template v-if="fileSize"> {{$t('commons.text.limit')}} <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
      <template v-if="fileType"> {{$t('commons.text.format')}} <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>
    </div>

    <el-dialog :visible.sync="dialogVisible" width="800" append-to-body>
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto"/>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    value: [String, Object, Array],
    headers: {
      type: Object,
      default: null,
    },
    limit: {
      type: Number,
      default: 1,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg"],
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 租户id
    tenantId: {
      type: String,
      default: null
    },
    // 宿主类型
    ownerType: {
      type: String,
      default: null
    },
    // 附件类型
    attachType: {
      type: String,
      default: null
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : this.value.split(',');
          // 然后将数组转为对象数组
          this.fileList = list.map(item => {
            return { url: item };
          });
        } else {
          this.fileList = [];
          return [];
        }
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    attachData() {
      return {
        tenantId: this.tenantId,
        ownerType: this.ownerType,
        attachType: this.attachType
      }
    },
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  data() {
    return {
      number: 0,
      dialogImageUrl: "",
      dialogVisible: false,
      hideUpload: false,
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/admin/api/v1/attach",
      fileList: []
    };
  },
  methods: {
    // 删除图片
    handleRemove(file, fileList) {
      this.$emit('removeCallback');
      const index = this.fileList.map(f => f.name).indexOf(file.name);
      if(index > -1) {
        this.fileList.splice(index, 1);
        this.$emit("input", this.listToString(this.fileList));
      }
    },
    // 上传成功回调
    handleUploadSuccess(response) {
      this.$emit('uploadCallback', response.data);
      this.fileList.push({
        name: response.data.attachName,
        url: response.data.viewUrl, // 根据你接口返回结构
        status: 'success',
      });
      this.$modal.closeLoading();
    },
    // 上传前loading加载
    handleBeforeUpload(file) {
      let isImg = false;
      if (this.fileType.length) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        isImg = this.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join("/")}图片格式文件!`);
        return false;
      }
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$modal.msgError(`上传头像图片大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      this.$modal.loading("正在上传图片，请稍候...");
      this.number++;
    },
    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传失败
    handleUploadError() {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },
    // 预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        strs += list[i].url.replace(this.baseUrl, "") + separator;
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    }
  }
};
</script>
<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
::v-deep.hide .el-upload--picture-card {
    display: none;
}
// 去掉动画效果
::v-deep .el-list-enter-active,
::v-deep .el-list-leave-active {
    transition: all 0s;
}

::v-deep .el-list-enter, .el-list-leave-active {
    opacity: 0;
    transform: translateY(0);
}
</style>

