<template>
  <div>
    <div ref="canvas" id="luckysheet" class="luckysheet-content"></div>
  </div>
</template>

<script>
export default {
  name: "Luckysheet",
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
    },
    /* excel名称 */
    title: {
      type: String,
      default: "",
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      luckysheet: null,
      options: {
        container: 'luckysheet', //容器id名
        title: this.title, // 表 头名
        lang: 'zh', // 中文
        showtoolbar: true, // 是否显示工具栏
        showinfobar: false, // 是否显示顶部信息栏
        showsheetbar: true, // 是否显示底部sheet按钮
        forceCalculation:true,//强制刷新公式
        showtoolbarConfig: {
          chart: false, // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
        },
      },
      sheetParams: [
        {
          name: "sheet1",
          color: "",
          index: 1,
          status: 0,
          order: 1,
          celldata: [],
          config: {},
        },
        {
          name: "Sheet2",
          color: "",
          index: 1,
          status: 0,
          order: 1,
          celldata: [],
          config: {},
        },
      ],
    };
  },
  created() {},
  mounted() {
    var initData = this.value?JSON.parse(this.value):this.sheetParams
    this.initLuckysheet(initData);
  },
  methods: {
    initLuckysheet(data) {
      var _this = this;//注意这里要重新指定下this对象。

      window.luckysheet.destroy();
      this.options.data = data;
      window.luckysheet.create(this.options);
      var that = this;
      this.$refs.canvas.addEventListener("click", async function(){
        try {
          const xml = await window.luckysheet.getluckysheetfile();
          that.$emit("input",JSON.stringify(xml))
        } catch (error) {
          that.$modal.msgError('实时保存失败'+error)
        }
      })

      // });
    },
  },
};
</script>
<style lang="css" scoped>
.luckysheet-content {
  margin: 0px;
  padding: 0px;
  width: 100%;
  height: calc(100vh - 230px);
  left: 0px;
  top: 40px;
  bottom: 0px;
}
</style>
